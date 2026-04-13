package com.last_battle.kalah.usecases.lobbies;

import com.last_battle.kalah.core.domain.model.Lobby;
import com.last_battle.kalah.domain.lobbies.LobbiesRepository;

import java.util.List;

public class GetLobbiesUC {
    private final LobbiesRepository lobbiesRepository;

    public GetLobbiesUC(LobbiesRepository lobbiesRepository) {
        this.lobbiesRepository = lobbiesRepository;
    }

    public List<Lobby> invoke() {
        return lobbiesRepository.getLobbies();
    }
}
