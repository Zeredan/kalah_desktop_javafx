// JoinLobbyUC.java
package com.last_battle.kalah.usecases.lobbies;

import com.last_battle.kalah.core.domain.model.Lobby;
import com.last_battle.kalah.domain.lobbies.LobbiesRepository;

public class JoinLobbyUC {
    private final LobbiesRepository lobbyRepository;

    public JoinLobbyUC(LobbiesRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    public Lobby invoke(String lobbyId) {
        return lobbyRepository.joinLobby(lobbyId);
    }
}