package com.last_battle.kalah.data.lobbies_d;

import com.last_battle.kalah.core.domain.model.Lobby;

import java.util.List;
import java.util.function.Consumer;

public interface LobbiesRemoteDataSource {
    public List<Lobby> getLobbies();

    public Lobby createLobby(String name, int stones, int holes, String userName, String password);

    public Lobby joinLobby(String lobbyId, String userName, String password);

    public void trackLobby(String lobbyId, String userName, String password, Consumer<Lobby> onUpdate);

    public void setReady(boolean ready);

    public void startGame();

    public void leaveLobby();
}
