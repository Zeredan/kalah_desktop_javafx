package com.last_battle.kalah.domain.users;

import com.last_battle.kalah.core.domain.model.User;

import java.util.List;

public interface UsersRepository {
    public List<User> getTopUsers(int limit);
}
