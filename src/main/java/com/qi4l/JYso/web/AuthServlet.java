package com.qi4l.JYso.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qi4l.JYso.web.config.JYsoWebPasswordProvider;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthServlet extends HttpServlet {

    static final ConcurrentHashMap<String, String> tokens = new ConcurrentHashMap<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        JSONObject body = JSON.parseObject(sb.toString());

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
        tokens.put(token, username);
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        result.put("username", username);
        resp.getWriter().write(JSON.toJSONString(result));
    }

    static boolean validateToken(String token) {
        return token != null && tokens.containsKey(token);
    }
}
