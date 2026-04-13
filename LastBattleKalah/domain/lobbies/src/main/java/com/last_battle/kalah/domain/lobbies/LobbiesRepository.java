package com.last_battle.kalah.domain.lobbies;

import com.last_battle.kalah.core.domain.model.Lobby;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public interface LobbiesRepository {
    public List<Lobby> getLobbies();

    public Lobby createLobby(String name, int stones, int holes);

    public Lobby joinLobby(String lobbyId);

    void trackLobby(String lobbyId, Consumer<Lobby> onUpdate);

    void setReady(boolean ready);

    void startGame();

    void leaveLobby();
}
