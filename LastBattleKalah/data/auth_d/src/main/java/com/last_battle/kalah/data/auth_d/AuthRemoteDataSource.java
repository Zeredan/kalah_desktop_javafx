package com.last_battle.kalah.data.auth_d;

import com.last_battle.kalah.core.domain.model.User;

public interface AuthRemoteDataSource {
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
