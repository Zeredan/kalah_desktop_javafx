package com.last_battle.kalah.core.domain.model;

import java.util.List;

public class GameState {
    private int holesCount;
    private int initialStonesCount;
    private List<Integer> player1Holes;
    private List<Integer> player2Holes;
    private int player1Kalah;
    private int player2Kalah;
    private int currentPlayerInd;
    private boolean isMakingMove;
    private String status;
    private String winnerId;

    // Конструкторы
    public GameState() {}

    public GameState(int holesCount, int initialStonesCount) {
        this.holesCount = holesCount;
        this.initialStonesCount = initialStonesCount;
        this.status = "IN_PROGRESS";
        this.currentPlayerInd = 1;
        this.isMakingMove = false;
    }

    // Геттеры и сеттеры
    public int getHolesCount() { return holesCount; }
    public void setHolesCount(int holesCount) { this.holesCount = holesCount; }

    public int getInitialStonesCount() { return initialStonesCount; }
    public void setInitialStonesCount(int initialStonesCount) { this.initialStonesCount = initialStonesCount; }

    public List<Integer> getPlayer1Holes() { return player1Holes; }
    public void setPlayer1Holes(List<Integer> player1Holes) { this.player1Holes = player1Holes; }

    public List<Integer> getPlayer2Holes() { return player2Holes; }
    public void setPlayer2Holes(List<Integer> player2Holes) { this.player2Holes = player2Holes; }

    public int getPlayer1Kalah() { return player1Kalah; }
    public void setPlayer1Kalah(int player1Kalah) { this.player1Kalah = player1Kalah; }

    public int getPlayer2Kalah() { return player2Kalah; }
    public void setPlayer2Kalah(int player2Kalah) { this.player2Kalah = player2Kalah; }

    public int getCurrentPlayerInd() { return currentPlayerInd; }
    public void setCurrentPlayerInd(int currentPlayerInd) { this.currentPlayerInd = currentPlayerInd; }

    public boolean isMakingMove() { return isMakingMove; }
    public void setMakingMove(boolean makingMove) { isMakingMove = makingMove; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getWinnerId() { return winnerId; }
    public void setWinnerId(String winnerId) { this.winnerId = winnerId; }

    public boolean isFinished() {
        return !"IN_PROGRESS".equals(status);
    }

    public boolean isMyTurn(String currentUserId, String player1Id, String player2Id) {
        if (isFinished()) return false;
        if (currentPlayerInd == 1 && currentUserId.equals(player1Id)) return true;
        if (currentPlayerInd == 2 && currentUserId.equals(player2Id)) return true;
        return false;
    }
}