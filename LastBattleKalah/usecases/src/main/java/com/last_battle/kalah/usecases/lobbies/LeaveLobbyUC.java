// LeaveLobbyUC.java
package com.last_battle.kalah.usecases.lobbies;

import com.last_battle.kalah.domain.lobbies.LobbiesRepository;

public class LeaveLobbyUC {
    private final LobbiesRepository lobbyRepository;

    public LeaveLobbyUC(LobbiesRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    public void invoke() {
        lobbyRepository.leaveLobby();
    }
}