package com.last_battle.kalah.usecases.auth;

import com.last_battle.kalah.core.domain.model.User;
import com.last_battle.kalah.domain.auth.AuthRepository;

public class LogInUserUC {
    private final AuthRepository authRepository;
    public LogInUserUC(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
    public User invoke(String username, String password) {
        return authRepository.loginUser(username, password);
    }
}
