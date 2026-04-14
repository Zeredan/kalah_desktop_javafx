package com.last_battle.kalah.domain_impl.game_impl;


import com.last_battle.kalah.core.domain.model.GameInfo;
import com.last_battle.kalah.data.game_d.GameRemoteDataSource;
import com.last_battle.kalah.domain.game.GameRepository;
import com.last_battle.kalah.usecases.auth.GetSavedCredentialsUC;
import com.last_battle.kalah.usecases.model.UserCredentials;

import java.util.function.Consumer;

public class GameRepositoryRemoteImpl implements GameRepository {
    private final GameRemoteDataSource gameRemoteDataSource;
    private final GetSavedCredentialsUC getSavedCredentialsUC;

    public GameRepositoryRemoteImpl(
            GameRemoteDataSource gameRemoteDataSource,
            GetSavedCredentialsUC getSavedCredentialsUC
    ) {
        this.gameRemoteDataSource = gameRemoteDataSource;
        this.getSavedCredentialsUC = getSavedCredentialsUC;
    }
    @Override
    public void trackGame(String gameId, Consumer<GameInfo> onUpdate) {
        UserCredentials uc = getSavedCredentialsUC.invoke();
        gameRemoteDataSource.trackGame(gameId, uc.getUsername(), uc.getPassword(), onUpdate);
    }
    @Override
    public void makeMove(int holeIndex) {
        gameRemoteDataSource.makeMove(holeIndex);
    }
    @Override
    public void leaveGame() {
        gameRemoteDataSource.leaveGame();
    }
}
