package com.last_battle.kalah.domain.auth;

import com.last_battle.kalah.core.domain.model.User;

public interface AuthRepository {
    public User registerUser(
            String username,
            String email,
            String password
    );
    public User loginUser(
            String username,
            String password
    );
}
