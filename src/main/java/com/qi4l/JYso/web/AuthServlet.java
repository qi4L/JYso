package com.qi4l.JYso.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qi4l.JYso.web.config.JYsoWebPasswordProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(AuthServlet.class);
    private static final long TOKEN_TTL_MS = 24 * 60 * 60 * 1000; // 24 hours

    static final ConcurrentHashMap<String, TokenEntry> tokens = new ConcurrentHashMap<>();

    static class TokenEntry {
        final String username;
        final long expireAt;

        TokenEntry(String username, long expireAt) {
            this.username = username;
            this.expireAt = expireAt;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        JSONObject body = WebUtils.readJson(req);
        String username = body.getString("username");
        String password = body.getString("password");

        if (!"qi".equals(username) || !JYsoWebPasswordProvider.getPassword().equals(password)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid credentials");
            resp.setStatus(401);
            resp.getWriter().write(JSON.toJSONString(error));
            return;
        }

        String token = UUID.randomUUID().toString();
        tokens.put(token, new TokenEntry(username, System.currentTimeMillis() + TOKEN_TTL_MS));
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        result.put("username", username);
        resp.getWriter().write(JSON.toJSONString(result));
    }

    static boolean validateToken(String token) {
        if (token == null) return false;
        TokenEntry entry = tokens.get(token);
        if (entry == null) return false;
        if (System.currentTimeMillis() > entry.expireAt) {
            tokens.remove(token);
            log.debug("Token expired and removed");
            return false;
        }
        return true;
    }
}
