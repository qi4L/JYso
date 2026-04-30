package com.qi4l.JYso.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class StaticResourceServlet extends HttpServlet {

    private static final Map<String, String> MIME_TYPES = new HashMap<>();

    static {
        MIME_TYPES.put("html", "text/html");
        MIME_TYPES.put("htm", "text/html");
        MIME_TYPES.put("css", "text/css");
        MIME_TYPES.put("js", "application/javascript");
        MIME_TYPES.put("mjs", "application/javascript");
        MIME_TYPES.put("json", "application/json");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("gif", "image/gif");
        MIME_TYPES.put("svg", "image/svg+xml");
        MIME_TYPES.put("ico", "image/x-icon");
        MIME_TYPES.put("woff", "font/woff");
        MIME_TYPES.put("woff2", "font/woff2");
        MIME_TYPES.put("ttf", "font/ttf");
        MIME_TYPES.put("eot", "application/vnd.ms-fontobject");
        MIME_TYPES.put("txt", "text/plain");
        MIME_TYPES.put("xml", "application/xml");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getRequestURI();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        URL resource = getClass().getClassLoader().getResource(path);
        if (resource == null) {
            resource = getClass().getClassLoader().getResource("static/" + path);
        }
        if (resource == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String ext = "";
        int dot = path.lastIndexOf('.');
        if (dot >= 0) {
            ext = path.substring(dot + 1).toLowerCase();
        }
        String mime = MIME_TYPES.getOrDefault(ext, "application/octet-stream");
        resp.setContentType(mime);

        try (InputStream is = resource.openStream();
             OutputStream os = resp.getOutputStream()) {
            byte[] buf = new byte[8192];
            int n;
            while ((n = is.read(buf)) != -1) {
                os.write(buf, 0, n);
            }
        }
    }
}
