package com.last_battle.kalah_server.service;


import com.last_battle.kalah_server.model.User;
import com.last_battle.kalah_server.repository.UsersRepository;

import java.util.Optional;

public class AuthService {
    private final UsersRepository usersRepository;

    public AuthService() {
        this.usersRepository = new UsersRepository();
    }

    public User register(String username, String email, String password) {
        // Проверка существования
        if (usersRepository.findByUsername(username).isPresent()) {
            return null;
        }

        if (usersRepository.findByEmail(email).isPresent()) {
            return null;
        }

        // Простая валидация
        if (username == null || username.trim().isEmpty()) return null;
        if (email == null || !email.contains("@")) return null;
        if (password == null || password.length() < 3) return null;

        return usersRepository.createUser(username, email, password);
    }

    public User login(String username, String password) {
        Optional<User> userOpt = usersRepository.authenticate(username, password);
        return userOpt.orElse(null);
    }
}