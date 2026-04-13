package com.last_battle.kalah.core.domain.model;

public class User {
    private String id;
    private String username;
    private String email;
    private String password;

    private Integer winsVsBot;
    private Integer winsVsPlayer;

    public User(String id, String username, String email, String password, Integer winsVsBot, Integer winsVsPlayer) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.winsVsBot = winsVsBot;
        this.winsVsPlayer = winsVsPlayer;
    }


    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getWinsVsBot() {
        return winsVsBot;
    }

    public Integer getWinsVsPlayer() {
        return winsVsPlayer;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", winsVsBot=" + winsVsBot +
                ", winsVsPlayer=" + winsVsPlayer +
                '}';
    }
}
