package com.last_battle.kalah.data_impl.game_d_impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.last_battle.kalah.core.domain.model.GameInfo;
import com.last_battle.kalah.core.domain.model.GameState;
import com.last_battle.kalah.core.domain.model.User;
import com.last_battle.kalah.data.game_d.GameRemoteDataSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

public class GameRemoteDataSourceSocketImpl implements GameRemoteDataSource {
    private final String socketHost;
    private final int socketPort;
    private final Gson gson;
    
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Consumer<GameInfo> onGameUpdate;
    private String currentGameId;
    private String currentUsername;
    private String currentPassword;
    private Thread listenerThread;
    private volatile boolean connected = false;

    public GameRemoteDataSourceSocketImpl(String socketHost, int socketPort) {
        this.socketHost = socketHost;
        this.socketPort = socketPort;
        this.gson = new Gson();
    }

    @Override
    public void trackGame(String gameId, String username, String password, Consumer<GameInfo> onUpdate) {
        disconnect();
        
        this.currentGameId = gameId;
        this.currentUsername = username;
        this.currentPassword = password;
        this.onGameUpdate = onUpdate;
        
        new Thread(() -> {
            try {
                socket = new Socket(socketHost, socketPort);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                connected = true;
                
                JsonObject trackMessage = new JsonObject();
                trackMessage.addProperty("type", "track");
                trackMessage.addProperty("gameId", gameId);
                trackMessage.addProperty("username", username);
                trackMessage.addProperty("password", password);
                out.println(gson.toJson(trackMessage));
                
                String line;
                while ((line = in.readLine()) != null && connected) {
                    JsonObject response = gson.fromJson(line, JsonObject.class);
                    String type = response.get("type").getAsString();
                    
                    if ("game_update".equals(type)) {
                        GameState state = gson.fromJson(response.get("gameState"), GameState.class);
                        User player1 = gson.fromJson(response.get("player1"), User.class);
                        User player2 = gson.fromJson(response.get("player2"), User.class);
                        
                        GameInfo gameInfo = new GameInfo(state, player1, player2);
                        if (onGameUpdate != null) {
                            onGameUpdate.accept(gameInfo);
                        }
                    } else if ("error".equals(type)) {
                        System.err.println("Game socket error: " + response.get("message").getAsString());
                    }
                }
            } catch (Exception e) {
                System.err.println("Game socket error: " + e.getMessage());
            } finally {
                disconnect();
            }
        }).start();
    }

    @Override
    public void makeMove(int holeIndex) {
        if (out == null || !connected) {
            System.err.println("Not connected to game socket");
            return;
        }
        
        JsonObject message = new JsonObject();
        message.addProperty("type", "make_move");
        message.addProperty("holeIndex", holeIndex);
        out.println(gson.toJson(message));
    }

    @Override
    public void leaveGame() {
        if (out != null && connected) {
            JsonObject message = new JsonObject();
            message.addProperty("type", "leave");
            out.println(gson.toJson(message));
        }
        disconnect();
    }

    private void disconnect() {
        connected = false;
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