package com.last_battle.kalah.core.domain.model;

import java.util.HashMap;
import java.util.Map;

public class Lobby {
    private String id;
    private String name;
    private int stones;
    private int holes;
    private User leader;
    private User guest;
    private Integer gameId;
    private Map<String, Boolean> readyStatus; // userId -> isReady (leader всегда true)

    public Lobby() {
        this.readyStatus = new HashMap<>();
    }

    public Lobby(String id, String name, int stones, int holes, User leader, User guest, Integer gameId) {
        this.id = id;
        this.name = name;
        this.stones = stones;
        this.holes = holes;
        this.leader = leader;
        this.guest = guest;
        this.gameId = gameId;
        this.readyStatus = new HashMap<>();
        if (leader != null) {
            this.readyStatus.put(leader.getId(), true); // лидер всегда готов
        }
        if (guest != null) {
            this.readyStatus.put(guest.getId(), false);
        }
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
    public void setLeader(User leader) {
        this.leader = leader;
        if (leader != null && !readyStatus.containsKey(leader.getId())) {
            readyStatus.put(leader.getId(), true);
        }
    }

    public User getGuest() { return guest; }
    public void setGuest(User guest) {
        this.guest = guest;
        if (guest != null && !readyStatus.containsKey(guest.getId())) {
            readyStatus.put(guest.getId(), false);
        }
    }

    public Integer getGameId() { return gameId; }
    public void setGameId(Integer gameId) { this.gameId = gameId; }

    public Map<String, Boolean> getReadyStatus() { return readyStatus; }
    public void setReadyStatus(Map<String, Boolean> readyStatus) { this.readyStatus = readyStatus; }

    public int getCurrentPlayers() {
        int count = 1;
        if (guest != null) count++;
        return count;
    }

    public boolean isFull() {
        return guest != null;
    }

    public boolean isReady(String userId) {
        return readyStatus != null && readyStatus.getOrDefault(userId, false);
    }

    public boolean areBothReady() {
        if (guest == null) return false;
        return isReady(leader.getId()) && isReady(guest.getId());
    }
}