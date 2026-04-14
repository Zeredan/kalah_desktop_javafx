package com.last_battle.kalah.data_impl.lobbies_d_impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.last_battle.kalah.core.domain.model.Lobby;
import com.last_battle.kalah.data.lobbies_d.LobbiesRemoteDataSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class LobbiesRemoteDataSourceHttpImpl implements LobbiesRemoteDataSource {

    private final String baseUrl;
    private final String socketHost;
    private final int socketPort;
    private final HttpClient httpClient;
    private final Gson gson;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Thread listenerThread;
    private Consumer<Lobby> onLobbyUpdate;
    private String currentLobbyId;
    private String currentUsername;
    private String currentPassword;

    public LobbiesRemoteDataSourceHttpImpl(
            HttpClient httpClient,
            String baseUrl,
            String socketHost,
            int socketPort
    ) {
        this.httpClient = httpClient;
        this.baseUrl = baseUrl;
        this.socketHost = socketHost;
        this.socketPort = socketPort;
        this.gson = new Gson();
    }

    // ==================== REST методы ====================

    @Override
    public List<Lobby> getLobbies() {
        try {
            return getLobbiesAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get lobbies", e);
        }
    }

    @Override
    public Lobby createLobby(String name, int stones, int holes, String userName, String password) {
        try {
            return createLobbyAsync(name, stones, holes, userName, password).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to create lobby", e);
        }
    }

    @Override
    public Lobby joinLobby(String lobbyId, String userName, String password) {
        try {
            return joinLobbyAsync(lobbyId, userName, password).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to join lobby", e);
        }
    }

    // ==================== Socket методы ====================

    @Override
    public void trackLobby(String lobbyId, String userName, String password, Consumer<Lobby> onUpdate) {
        disconnect();
        this.currentLobbyId = lobbyId;
        this.currentUsername = userName;
        this.currentPassword = password;
        this.onLobbyUpdate = onUpdate;

        new Thread(() -> {
            try {
                socket = new Socket(socketHost, socketPort);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Отправляем track команду с аутентификацией
                JsonObject trackMessage = new JsonObject();
                trackMessage.addProperty("type", "track");
                trackMessage.addProperty("lobbyId", lobbyId);
                trackMessage.addProperty("username", userName);
                trackMessage.addProperty("password", password);
                out.println(gson.toJson(trackMessage));

                // Слушаем входящие сообщения
                String line;
                while ((line = in.readLine()) != null) {
                    JsonObject response = gson.fromJson(line, JsonObject.class);
                    String type = response.get("type").getAsString();

                    if ("lobby_update".equals(type)) {
                        Lobby lobby = gson.fromJson(response.get("lobby"), Lobby.class);
                        if (onLobbyUpdate != null) {
                            onLobbyUpdate.accept(lobby);
                        }
                    } else if ("lobby_closed".equals(type)) {
                        if (onLobbyUpdate != null) {
                            onLobbyUpdate.accept(null);
                        }
                        break;
                    } else if ("error".equals(type)) {
                        System.err.println("Socket error: " + response.get("message").getAsString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void setReady(boolean ready) {
        if (out == null || socket == null || socket.isClosed()) {
            throw new RuntimeException("Not connected to lobby socket");
        }

        JsonObject message = new JsonObject();
        message.addProperty("type", "ready");
        message.addProperty("ready", ready);
        out.println(gson.toJson(message));
    }

    @Override
    public void startGame() {
        if (out == null || socket == null || socket.isClosed()) {
            throw new RuntimeException("Not connected to lobby socket");
        }

        JsonObject message = new JsonObject();
        message.addProperty("type", "start_game");
        out.println(gson.toJson(message));
    }

    @Override
    public void leaveLobby() {
        if (out == null || socket == null || socket.isClosed()) {
            return;
        }

        JsonObject message = new JsonObject();
        message.addProperty("type", "leave");
        out.println(gson.toJson(message));
        disconnect();
    }


    // ==================== Приватные REST методы ====================

    private CompletableFuture<List<Lobby>> getLobbiesAsync() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/lobbies"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == 200) {
                        return gson.fromJson(response.body(), new TypeToken<List<Lobby>>() {}.getType());
                    } else {
                        throw new RuntimeException("Failed to get lobbies: HTTP " + response.statusCode());
                    }
                });
    }

    private CompletableFuture<Lobby> createLobbyAsync(String name, int stones, int holes, String userName, String password) {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("name", name);
        requestBody.addProperty("stones", stones);
        requestBody.addProperty("holes", holes);
        requestBody.addProperty("username", userName);
        requestBody.addProperty("password", password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/lobbies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == 200 || response.statusCode() == 201) {
                        return gson.fromJson(response.body(), Lobby.class);
                    } else {
                        throw new RuntimeException("Failed to create lobby: HTTP " + response.statusCode());
                    }
                });
    }

    private CompletableFuture<Lobby> joinLobbyAsync(String lobbyId, String userName, String password) {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("username", userName);
        requestBody.addProperty("password", password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/lobbies/" + lobbyId + "/join"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == 200) {
                        return gson.fromJson(response.body(), Lobby.class);
                    } else {
                        throw new RuntimeException("Failed to join lobby: HTTP " + response.statusCode());
                    }
                });
    }

    private void disconnect() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        socket = null;
        out = null;
        in = null;
    }
}