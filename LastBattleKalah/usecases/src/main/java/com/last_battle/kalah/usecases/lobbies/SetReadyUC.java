// SetReadyUC.java
package com.last_battle.kalah.usecases.lobbies;

import com.last_battle.kalah.domain.lobbies.LobbiesRepository;

public class SetReadyUC {
    private final LobbiesRepository lobbyRepository;

    public SetReadyUC(LobbiesRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    public void invoke(boolean ready) {
        lobbyRepository.setReady(ready);
    }
}