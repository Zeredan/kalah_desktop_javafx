package com.last_battle.kalah.usecases.lobbies;

import com.last_battle.kalah.core.domain.model.Lobby;
import com.last_battle.kalah.domain.lobbies.LobbiesRepository;

import java.util.List;

public class CreateLobbyUC {
    private final LobbiesRepository lobbiesRepository;

    public CreateLobbyUC(LobbiesRepository lobbiesRepository) {
        this.lobbiesRepository = lobbiesRepository;
    }

    public Lobby invoke(String name, int stones, int holes) {
        return lobbiesRepository.createLobby(name, stones, holes);
    }
}
