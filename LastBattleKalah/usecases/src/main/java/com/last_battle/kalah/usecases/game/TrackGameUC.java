package com.last_battle.kalah.usecases.game;

import com.last_battle.kalah.core.domain.model.GameInfo;
import com.last_battle.kalah.domain.game.GameRepository;

import java.util.function.Consumer;

public class TrackGameUC {
    private final GameRepository gameRepository;

    public TrackGameUC(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void invoke(String gameId, Consumer<GameInfo> onUpdate) {
        gameRepository.trackGame(gameId, onUpdate);
    }
}