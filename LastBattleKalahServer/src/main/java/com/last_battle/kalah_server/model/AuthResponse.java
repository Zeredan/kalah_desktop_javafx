package com.last_battle.kalah_server.model;

public class AuthResponse {
    private boolean success;
    private String message;
    private User user;

    public AuthResponse(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    // Геттеры и сеттеры
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}