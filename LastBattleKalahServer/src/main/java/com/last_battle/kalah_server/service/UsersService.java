package com.last_battle.kalah_server.service;

import com.last_battle.kalah_server.model.User;
import com.last_battle.kalah_server.repository.UsersRepository;

import java.util.List;

public class UsersService {
    private final UsersRepository usersRepository;
    
    public UsersService() {
        this.usersRepository = new UsersRepository();
    }
    
    public List<User> getTopUsers(int limit) {
        return usersRepository.getTopUsers(limit);
    }

    public User authenticate(String username, String password) {
        return usersRepository.authenticate(username, password).orElse(null);
    }

    public void incrementWinsVsPlayer(Long userId) {
        usersRepository.incrementWinsVsPlayer(userId);
    }
}