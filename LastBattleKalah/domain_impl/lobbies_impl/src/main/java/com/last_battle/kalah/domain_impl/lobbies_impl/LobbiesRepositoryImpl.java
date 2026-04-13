package com.last_battle.kalah.domain_impl.lobbies_impl;

import com.last_battle.kalah.core.domain.model.Lobby;
import com.last_battle.kalah.data.lobbies_d.LobbiesRemoteDataSource;
import com.last_battle.kalah.domain.lobbies.LobbiesRepository;
import com.last_battle.kalah.usecases.auth.GetSavedCredentialsUC;
import com.last_battle.kalah.usecases.model.UserCredentials;

import java.util.List;
import java.util.function.Consumer;

public class LobbiesRepositoryImpl implements LobbiesRepository {
    private final LobbiesRemoteDataSource lobbiesRemoteDataSource;
    private final GetSavedCredentialsUC getSavedCredentialsUC;

    public LobbiesRepositoryImpl(
            LobbiesRemoteDataSource lobbiesRemoteDataSource,
            GetSavedCredentialsUC getSavedCredentialsUC
    ) {
        this.lobbiesRemoteDataSource = lobbiesRemoteDataSource;
        this.getSavedCredentialsUC = getSavedCredentialsUC;
    }

    @Override
    public List<Lobby> getLobbies() {
        return lobbiesRemoteDataSource.getLobbies();
    }

    @Override
    public Lobby createLobby(String name, int stones, int holes) {
        UserCredentials uc = getSavedCredentialsUC.invoke();
        return lobbiesRemoteDataSource.createLobby(name, stones, holes, uc.getUsername(), uc.getPassword());
    }

    @Override
    public Lobby joinLobby(String lobbyId) {
        UserCredentials uc = getSavedCredentialsUC.invoke();
        return lobbiesRemoteDataSource.joinLobby(lobbyId, uc.getUsername(), uc.getPassword());
    }

    @Override
    public void trackLobby(String lobbyId, Consumer<Lobby> onUpdate) {
        UserCredentials uc = getSavedCredentialsUC.invoke();
        lobbiesRemoteDataSource.trackLobby(lobbyId, uc.getUsername(), uc.getPassword(), onUpdate);
    }

    @Override
    public void setReady(boolean ready) {
        UserCredentials uc = getSavedCredentialsUC.invoke();
        lobbiesRemoteDataSource.setReady(ready);
    }

    @Override
    public void startGame() {
        lobbiesRemoteDataSource.startGame();
    }

    @Override
    public void leaveLobby() {
        lobbiesRemoteDataSource.leaveLobby();
    }
}
