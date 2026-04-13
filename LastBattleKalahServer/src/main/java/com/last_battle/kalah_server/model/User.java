package com.last_battle.kalah_server.model;

public class User {
    private Long id;
    private String username;
    private String email;
    private String password;  // ← храним напрямую
    private int winsVsPlayer;
    private int winsVsBot;

    public User() {}

    public User(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.winsVsPlayer = 0;
        this.winsVsBot = 0;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getWinsVsPlayer() { return winsVsPlayer; }
    public void setWinsVsPlayer(int winsVsPlayer) { this.winsVsPlayer = winsVsPlayer; }

    public int getWinsVsBot() { return winsVsBot; }
    public void setWinsVsBot(int winsVsBot) { this.winsVsBot = winsVsBot; }
}