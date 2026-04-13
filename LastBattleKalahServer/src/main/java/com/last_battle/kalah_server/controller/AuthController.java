package com.last_battle.kalah_server.controller;

import com.last_battle.kalah_server.model.AuthResponse;
import com.last_battle.kalah_server.model.LoginRequest;
import com.last_battle.kalah_server.model.RegisterRequest;
import com.last_battle.kalah_server.model.User;
import com.last_battle.kalah_server.service.AuthService;
import io.javalin.http.Context;

public class AuthController {
    private final AuthService authService;
    
    public AuthController() {
        this.authService = new AuthService();
    }
    
    public void register(Context ctx) {
        System.out.println("onRegister" + ctx.body());
        RegisterRequest request = ctx.bodyAsClass(RegisterRequest.class);
        
        User user = authService.register(
            request.getUsername(),
            request.getEmail(),
            request.getPassword()
        );
        
        if (user != null) {
            ctx.json(new AuthResponse(true, "Registration successful", user));
        } else {
            ctx.status(400).json(new AuthResponse(false, "Registration failed", null));
        }
    }
    
    public void login(Context ctx) {
        System.out.println("onLogin" + ctx.body());
        LoginRequest request = ctx.bodyAsClass(LoginRequest.class);
        
        User user = authService.login(request.getUsername(), request.getPassword());
        
        if (user != null) {
            ctx.json(new AuthResponse(true, "Login successful", user));
        } else {
            ctx.status(401).json(new AuthResponse(false, "Invalid credentials", null));
        }
    }
}