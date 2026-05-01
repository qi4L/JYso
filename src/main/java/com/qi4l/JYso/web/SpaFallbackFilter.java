package com.qi4l.JYso.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class SpaFallbackFilter implements Filter {

    private byte[] indexContent;

    @Override
    public void init(FilterConfig filterConfig) {
        try {
            URL indexUrl = getClass().getClassLoader().getResource("static/index.html");
            if (indexUrl != null) {
                try (InputStream is = indexUrl.openStream()) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buf = new byte[4096];
                    int n;
                    while ((n = is.read(buf)) != -1) bos.write(buf, 0, n);
                    indexContent = bos.toByteArray();
                }
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI();
        if (path.startsWith("/api/")) {
            chain.doFilter(request, response);
            return;
        }

        String lower = path.toLowerCase();
        boolean hasFileExt = lower.contains(".") && !lower.endsWith(".html");

        if (!hasFileExt && indexContent != null) {
            resp.setContentType("text/html");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(200);
            resp.setContentLength(indexContent.length);
            OutputStream os = resp.getOutputStream();
            os.write(indexContent);
            os.flush();
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
