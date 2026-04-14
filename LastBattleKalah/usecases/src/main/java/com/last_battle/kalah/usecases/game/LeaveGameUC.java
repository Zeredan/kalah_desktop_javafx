package com.last_battle.kalah.usecases.game;

import com.last_battle.kalah.domain.game.GameRepository;

public class LeaveGameUC {
    private final GameRepository gameRepository;

    public LeaveGameUC(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void invoke() {
        gameRepository.leaveGame();
    }
}