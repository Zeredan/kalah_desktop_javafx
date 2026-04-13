// TrackLobbyUC.java
package com.last_battle.kalah.usecases.lobbies;

import com.last_battle.kalah.core.domain.model.Lobby;
import com.last_battle.kalah.domain.lobbies.LobbiesRepository;

import java.util.function.Consumer;

public class TrackLobbyUC {
    private final LobbiesRepository lobbyRepository;

    public TrackLobbyUC(LobbiesRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    public void invoke(String lobbyId, Consumer<Lobby> onUpdate) {
        lobbyRepository.trackLobby(lobbyId, onUpdate);
    }
}