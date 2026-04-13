package com.last_battle.kalah.domain_impl.users_impl;

import com.last_battle.kalah.core.domain.model.User;
import com.last_battle.kalah.data.users_d.UsersRemoteDataSource;
import com.last_battle.kalah.domain.users.UsersRepository;

import java.util.List;

public class UsersRepositoryRemoteImpl implements UsersRepository {
    private final UsersRemoteDataSource usersRemoteDataSource;

    public UsersRepositoryRemoteImpl(
            UsersRemoteDataSource usersRemoteDataSource
    ) {
        this.usersRemoteDataSource = usersRemoteDataSource;
    }

    @Override
    public List<User> getTopUsers(int limit) {
        return usersRemoteDataSource.getTopUsers(limit);
    }
}
