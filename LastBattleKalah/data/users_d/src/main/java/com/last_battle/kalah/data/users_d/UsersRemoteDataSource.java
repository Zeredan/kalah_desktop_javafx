package com.last_battle.kalah.data.users_d;

import com.last_battle.kalah.core.domain.model.User;

import java.util.List;

public interface UsersRemoteDataSource {
    public List<User> getTopUsers(int limit);
}
