package com.last_battle.kalah.data_impl.users_d_impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.last_battle.kalah.core.domain.model.User;
import com.last_battle.kalah.data.users_d.UsersRemoteDataSource;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class UsersRemoteDataSourceHttpImpl implements UsersRemoteDataSource {
    private final String BASE_URL;
    private final HttpClient httpClient;
    private final Gson gson;


    public UsersRemoteDataSourceHttpImpl(
            HttpClient httpClient,
            String baseURL
    ) {
        this.httpClient = httpClient;
        this.BASE_URL = baseURL;
        this.gson = new Gson();
    }


    @Override
    public List<User> getTopUsers(int limit) {
        try {
            List<User> a = getTopUsersAsync(limit).get();
            System.out.println(a.get(0).toString());
            return a;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private CompletableFuture<List<User>> getTopUsersAsync(int limit) {
        try {
            var request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/users/top?limit=" + limit))
                    .GET()
                    .build();

            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        if (response.statusCode() == 200) {
                            return gson.fromJson(response.body(), new TypeToken<List<User>>() {}.getType());
                        } else {
                            throw new RuntimeException("Fetch failed: " + response.statusCode());
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch top users", e);
        }
    }
}
