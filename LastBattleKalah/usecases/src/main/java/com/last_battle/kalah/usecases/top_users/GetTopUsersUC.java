package com.last_battle.kalah.usecases.top_users;

import com.last_battle.kalah.core.domain.model.User;
import com.last_battle.kalah.domain.users.UsersRepository;

import java.util.List;

public class GetTopUsersUC {
    private final UsersRepository usersRepository;
    public GetTopUsersUC(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    public List<User> invoke(int limit) {
        return usersRepository.getTopUsers(limit);
    }
}
