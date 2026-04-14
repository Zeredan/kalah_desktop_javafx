package com.last_battle.kalah.usecases.game;

import com.last_battle.kalah.domain.game.GameRepository;

public class MakeMoveUC {
    private final GameRepository gameRepository;

    public MakeMoveUC(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void invoke(int holeIndex) {
        gameRepository.makeMove(holeIndex);
    }
}