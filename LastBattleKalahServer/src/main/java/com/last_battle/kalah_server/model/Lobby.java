package com.last_battle.kalah_server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class Lobby {
    private String id;
    private String name;
    private int stones;
    private int holes;
    private User leader;
    private User guest;
    private Map<Long, Boolean> readyStatus; // userId -> isReady
    @JsonIgnore
    private transient Set<Socket> sockets; // активные сокет-подписки
    private String gameId;

    public Lobby() {
        this.readyStatus = new ConcurrentHashMap<>();
        this.sockets = new CopyOnWriteArraySet<>();
    }

    public Lobby(String id, String name, int stones, int holes, User leader) {
        this();
        this.id = id;
        this.name = name;
        this.stones = stones;
        this.holes = holes;
        this.leader = leader;
        this.readyStatus.put(leader.getId(), true);
    }

    // Геттеры и сеттеры
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getStones() { return stones; }
    public void setStones(int stones) { this.stones = stones; }
    public int getHoles() { return holes; }
    public void setHoles(int holes) { this.holes = holes; }
    public User getLeader() { return leader; }
    public void setLeader(User leader) { this.leader = leader; }
    public User getGuest() { return guest; }
    public void setGuest(User guest) { this.guest = guest; }
    public Map<Long, Boolean> getReadyStatus() { return readyStatus; }
    public Set<Socket> getSockets() { return sockets; }
    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }

    public int getCurrentPlayers() { return guest == null ? 1 : 2; }
    public boolean isFull() { return guest != null; }

    public boolean areBothReady() {
        if (guest == null) return false;
        return readyStatus.getOrDefault(leader.getId(), true) &&
                readyStatus.getOrDefault(guest.getId(), false);
    }
}