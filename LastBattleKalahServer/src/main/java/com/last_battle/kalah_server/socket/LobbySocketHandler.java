package com.last_battle.kalah_server.socket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.last_battle.kalah_server.model.Lobby;
import com.last_battle.kalah_server.model.User;
import com.last_battle.kalah_server.service.LobbyService;
import com.last_battle.kalah_server.service.UsersService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LobbySocketHandler {
    private static final int PORT = 8081;
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();
    private static final UsersService userService = new UsersService();
    private static final Gson gson = new Gson();

    public static void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Lobby Socket Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(() -> handleClient(clientSocket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        String lobbyId = null;
        User user = null;
        PrintWriter out = null;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String firstMessage = in.readLine();
            JsonObject firstJson = gson.fromJson(firstMessage, JsonObject.class);

            if (!"track".equals(firstJson.get("type").getAsString())) {
                socket.close();
                return;
            }

            lobbyId = firstJson.get("lobbyId").getAsString();
            String username = firstJson.get("username").getAsString();
            String password = firstJson.get("password").getAsString();

            user = userService.authenticate(username, password);
            if (user == null) {
                out.println(gson.toJson(errorResponse("Authentication failed")));
                socket.close();
                return;
            }

            LobbyService.getInstance().addSocket(lobbyId, socket);

            Lobby lobby = LobbyService.getInstance().getLobby(lobbyId);
            if (lobby != null) {
                String response = gson.toJson(lobbyUpdateResponse(lobby));
                out.println(response);
                broadcastToLobby(lobby, socket);
            }

            String line;
            while ((line = in.readLine()) != null) {
                JsonObject command = gson.fromJson(line, JsonObject.class);
                String type = command.get("type").getAsString();

                switch (type) {
                    case "ready":
                        boolean ready = command.get("ready").getAsBoolean();
                        lobby = LobbyService.getInstance().setReady(lobbyId, user, ready);
                        if (lobby != null) broadcastToLobby(lobby);
                        break;

                    case "start_game":
                        lobby = LobbyService.getInstance().startGame(lobbyId, user);
                        if (lobby != null && lobby.getGameId() != null) {
                            broadcastToLobby(lobby);
                        }
                        break;

                    case "leave":
                        lobby = LobbyService.getInstance().leaveLobby(lobbyId, user);
                        if (lobby != null) {
                            broadcastToLobby(lobby);
                        } else {
                            broadcastLobbyClosed(lobbyId);
                        }
                        socket.close();
                        return;

                    default:
                        out.println(gson.toJson(errorResponse("Unknown command: " + type)));
                }
            }
            System.out.println("AFTER LOOP");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("FINALLY");
            if (lobbyId != null && user != null) {
                Lobby lobby = LobbyService.getInstance().leaveLobby(lobbyId, user);
                if (lobby != null) {
                    broadcastToLobby(lobby);
                } else {
                    broadcastLobbyClosed(lobbyId);
                }
            }
            LobbyService.getInstance().removeSocket(lobbyId, socket);
            try {
                if (socket != null) socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void broadcastToLobby(Lobby lobby) {
        broadcastToLobby(lobby, null);
    }

    private static void broadcastToLobby(Lobby lobby, Socket excludeSocket) {
        if (lobby == null) return;

        JsonObject response = lobbyUpdateResponse(lobby);
        String message = gson.toJson(response);

        for (Socket socket : lobby.getSockets()) {
            if (socket.equals(excludeSocket)) continue;
            try {
                if (!socket.isClosed()) {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void broadcastLobbyClosed(String lobbyId) {
        JsonObject response = new JsonObject();
        response.addProperty("type", "lobby_closed");
        response.addProperty("lobbyId", lobbyId);
        String message = gson.toJson(response);

        Lobby lobby = LobbyService.getInstance().getLobby(lobbyId);
        if (lobby != null) {
            for (Socket socket : lobby.getSockets()) {
                try {
                    if (!socket.isClosed()) {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static JsonObject lobbyUpdateResponse(Lobby lobby) {
        JsonObject response = new JsonObject();
        response.addProperty("type", "lobby_update");
        response.add("lobby", gson.toJsonTree(lobby));
        return response;
    }

    private static JsonObject errorResponse(String message) {
        JsonObject error = new JsonObject();
        error.addProperty("type", "error");
        error.addProperty("message", message);
        return error;
    }
}