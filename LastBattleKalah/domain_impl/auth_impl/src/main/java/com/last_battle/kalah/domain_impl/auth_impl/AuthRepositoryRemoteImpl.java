package com.last_battle.kalah.domain_impl.auth_impl;

import com.last_battle.kalah.core.domain.model.User;
import com.last_battle.kalah.data.auth_d.AuthRemoteDataSource;
import com.last_battle.kalah.domain.auth.AuthRepository;

public class AuthRepositoryRemoteImpl implements AuthRepository {
    private final AuthRemoteDataSource authRemoteDataSource;

    public AuthRepositoryRemoteImpl(AuthRemoteDataSource authRemoteDataSource) {
        this.authRemoteDataSource = authRemoteDataSource;
    }

    @Override
    public User registerUser(String username, String email, String password) {
        return authRemoteDataSource.registerUser(username, email, password);
    }

    @Override
    public User loginUser(String username, String password) {
        return authRemoteDataSource.loginUser(username, password);
    }
}
