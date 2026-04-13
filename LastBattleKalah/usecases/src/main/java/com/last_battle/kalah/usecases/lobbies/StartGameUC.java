// StartGameUC.java
package com.last_battle.kalah.usecases.lobbies;

import com.last_battle.kalah.domain.lobbies.LobbiesRepository;

public class StartGameUC {
    private final LobbiesRepository lobbyRepository;

    public StartGameUC(LobbiesRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    public void invoke() {
        lobbyRepository.startGame();
    }
}