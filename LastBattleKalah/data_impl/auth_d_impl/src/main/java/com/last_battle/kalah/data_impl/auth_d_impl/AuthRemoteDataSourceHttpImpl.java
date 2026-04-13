package com.last_battle.kalah.data_impl.auth_d_impl;

import com.last_battle.kalah.core.domain.model.User;
import com.last_battle.kalah.data.auth_d.AuthRemoteDataSource;

import com.last_battle.kalah.core.domain.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AuthRemoteDataSourceHttpImpl implements AuthRemoteDataSource {

    private final String BASE_URL;
    private final HttpClient httpClient;
    private final Gson gson;


    public AuthRemoteDataSourceHttpImpl(
            HttpClient httpClient,
            String baseURL
    ) {
        this.httpClient = httpClient;
        this.BASE_URL = baseURL;
        this.gson = new Gson();
    }

    @Override
    public User registerUser(String username, String email, String password) {
        try {
            return registerUserAsync(username, email, password).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User loginUser(String username, String password) {
        try {
            return loginUserAsync(username, password).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private CompletableFuture<User> registerUserAsync(String username, String email, String password) {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("username", username);
        requestBody.addProperty("email", email);
        requestBody.addProperty("password", password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/auth/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    AuthResponse result = gson.fromJson(response.body(), AuthResponse.class);
                    if (!result.isSuccess() || result.getUser() == null) {
                        throw new RuntimeException("Registration failed: " + result.getMessage());
                    }
                    return result.getUser();
                });
    }

    private CompletableFuture<User> loginUserAsync(String username, String password) {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("username", username);
        requestBody.addProperty("password", password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    AuthResponse result = gson.fromJson(response.body(), AuthResponse.class);
                    if (!result.isSuccess() || result.getUser() == null) {
                        throw new RuntimeException("Login failed: " + result.getMessage());
                    }
                    return result.getUser();
                });
    }
}