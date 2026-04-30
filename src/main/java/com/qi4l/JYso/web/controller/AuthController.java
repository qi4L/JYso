package com.qi4l.JYso.web.controller;

import com.qi4l.JYso.web.config.JYsoWebPasswordProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final ConcurrentHashMap<String, String> tokens = new ConcurrentHashMap<>();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (!"qi".equals(username) || !JYsoWebPasswordProvider.getPassword().equals(password)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid credentials");
            return ResponseEntity.status(401).body(error);
        }

        String token = UUID.randomUUID().toString();
        tokens.put(token, username);
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        result.put("username", username);
        return ResponseEntity.ok(result);
    }

    public static boolean validateToken(String token) {
        return token != null && tokens.containsKey(token);
    }
}
