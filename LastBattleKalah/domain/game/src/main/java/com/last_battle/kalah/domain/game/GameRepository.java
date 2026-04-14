package com.last_battle.kalah.domain.game;

import com.last_battle.kalah.core.domain.model.GameInfo;

import java.util.function.Consumer;

public interface GameRepository {
    void trackGame(String gameId, Consumer<GameInfo> onUpdate);
    void makeMove(int holeIndex);
    void leaveGame();
}
