package com.last_battle.kalah.data.game_d;

import com.last_battle.kalah.core.domain.model.GameInfo;

import java.util.function.Consumer;

public interface GameRemoteDataSource {
    void trackGame(String gameId, String username, String password, Consumer<GameInfo> onUpdate);
    void makeMove(int holeIndex);
    void leaveGame();
}
