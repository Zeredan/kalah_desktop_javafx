package com.last_battle.kalah.core.domain.model;

public class GameInfo {
    private GameState gameState;
    private User player1;
    private User player2;

    public GameInfo() {}

    public GameInfo(GameState gameState, User player1, User player2) {
        this.gameState = gameState;
        this.player1 = player1;
        this.player2 = player2;
    }

    public GameState getGameState() { return gameState; }
    public void setGameState(GameState gameState) { this.gameState = gameState; }

    public User getPlayer1() { return player1; }
    public void setPlayer1(User player1) { this.player1 = player1; }

    public User getPlayer2() { return player2; }
    public void setPlayer2(User player2) { this.player2 = player2; }

    public User getOpponent(String currentUserId) {
        if (player1.getId().equals(currentUserId)) return player2;
        return player1;
    }

    public boolean isPlayer1(String userId) {
        return player1.getId().equals(userId);
    }
}