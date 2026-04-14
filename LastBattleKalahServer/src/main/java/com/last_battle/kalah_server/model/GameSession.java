package com.last_battle.kalah_server.model;

import java.net.Socket;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class GameSession {
    private String id;
    private User player1;
    private User player2;
    private GameState state;
    private Set<Socket> sockets;
    //private Boolean isWinRecorded;

    public GameSession(String id, User player1, User player2, GameState state) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.state = state;
        this.sockets = new CopyOnWriteArraySet<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public User getPlayer1() { return player1; }
    public void setPlayer1(User player1) { this.player1 = player1; }
    
    public User getPlayer2() { return player2; }
    public void setPlayer2(User player2) { this.player2 = player2; }
    
    public GameState getState() { return state; }
    public void setState(GameState state) { this.state = state; }
    
    public Set<Socket> getSockets() { return sockets; }
    public void setSockets(Set<Socket> sockets) { this.sockets = sockets; }
}