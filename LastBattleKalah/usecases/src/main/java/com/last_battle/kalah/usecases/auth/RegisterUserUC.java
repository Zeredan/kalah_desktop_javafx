package com.last_battle.kalah.usecases.auth;

import com.last_battle.kalah.core.domain.model.User;
import com.last_battle.kalah.domain.auth.AuthRepository;

public class RegisterUserUC {
    private final AuthRepository authRepository;

    public RegisterUserUC(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
    public User invoke(String username, String email, String password) {
        return authRepository.registerUser(username, email, password);
    }
}
