package com.qi4l.JYso.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WebUtils {

    private static final Path BASE_DIR = Paths.get(".").toAbsolutePath().normalize();

    public static Path resolveSafePath(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }
        Path resolved = BASE_DIR.resolve(name).normalize();
        if (!resolved.startsWith(BASE_DIR)) {
            throw new IllegalArgumentException("path traversal detected");
        }
        return resolved;
    }

    public static JSONObject readJson(HttpServletRequest req) throws IOException {
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

    public static String escapeJson(String s) {
        if (s == null) return "null";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
