package com.last_battle.kalah_server.socket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.last_battle.kalah_server.game.GameLogic;
import com.last_battle.kalah_server.model.GameSession;
import com.last_battle.kalah_server.model.GameState;
import com.last_battle.kalah_server.model.User;
import com.last_battle.kalah_server.service.GameService;
import com.last_battle.kalah_server.service.UsersService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameSocketHandler {
    private static final int PORT = 8082;
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();
    private static final UsersService userService = new UsersService();
    private static final Gson gson = new Gson();

    public static void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Game Socket Server started on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(() -> handleClient(clientSocket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        String gameId = null;
        User user = null;
        PrintWriter out = null;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String firstMessage = in.readLine();
            System.out.println("game socket Finput: " + firstMessage);
            JsonObject firstJson = gson.fromJson(firstMessage, JsonObject.class);

            if (!"track".equals(firstJson.get("type").getAsString())) {
                socket.close();
                return;
            }

            gameId = firstJson.get("gameId").getAsString();
            String username = firstJson.get("username").getAsString();
            String password = firstJson.get("password").getAsString();

            user = userService.authenticate(username, password);
            if (user == null) {
                out.println(gson.toJson(errorResponse("Authentication failed")));
                socket.close();
                return;
            }

            GameService.getInstance().addSocket(gameId, socket);

            GameSession session = GameService.getInstance().getGame(gameId);
            if (session != null) {
                // Отправляем полную информацию об игре (включая данные игроков)
                out.println(gson.toJson(gameUpdateResponse(session)));
                broadcastToGame(session, socket);
            }

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("game socket input: " + line);
                JsonObject command = gson.fromJson(line, JsonObject.class);
                String type = command.get("type").getAsString();

                switch (type) {
                    case "make_move":
                        int holeIndex = command.get("holeIndex").getAsInt();

                        int playerInd = user.getId().equals(session.getPlayer1().getId()) ? 1 : 2;

                        if (playerInd != session.getState().getCurrentPlayerInd()) {
                            out.println(gson.toJson(errorResponse("Not your turn")));
                            break;
                        }

                        String finalGameId = gameId;
                        GameLogic.StateCallback callback = newState -> {
                            GameService.getInstance().updateGameState(finalGameId, newState);
                            broadcastToGame(session);

                            // Если игра закончилась, обновляем статистику
                            if (!"IN_PROGRESS".equals(newState.getStatus())) {
                                System.out.println("UUS: CALLBACK");
                                updateUserStats(session, newState);
                            }
                        };

                        if (playerInd == 1) {
                            GameLogic.makeMovePlayer1(session.getState(), holeIndex, callback);
                        } else {
                            GameLogic.makeMovePlayer2(session.getState(), holeIndex, callback);
                        }
                        break;

                    case "leave":
                        handleGameLeave(user, session);
                        socket.close();
                        return;

                    default:
                        out.println(gson.toJson(errorResponse("Unknown command: " + type)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (gameId != null && user != null) {
                GameSession session = GameService.getInstance().getGame(gameId);
                if (session != null) {
                    handleGameLeave(user, session);
                }
            }
            GameService.getInstance().removeSocket(gameId, socket);
            try {
                if (socket != null) socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleGameLeave(User user, GameSession session) {
        if (session == null) return;

        if (!"IN_PROGRESS".equals(session.getState().getStatus())) {
            return;
        }

        // Определяем победителя (тот, кто остался)
        User winner;
        if (user.getId().equals(session.getPlayer1().getId())) {
            winner = session.getPlayer2();
        } else {
            winner = session.getPlayer1();
        }

        GameState state = session.getState();
        state.setStatus("PLAYER1_WIN".equals(state.getStatus()) ? "PLAYER2_WIN" : "PLAYER1_WIN");
        if (winner.getId().equals(session.getPlayer1().getId())) {
            state.setStatus("PLAYER1_WIN");
        } else {
            state.setStatus("PLAYER2_WIN");
        }
        state.setWinnerId(winner.getId().toString());

        // Обновляем статистику
        System.out.println("UUS: LEAVE");
        updateUserStats(session, state);

        broadcastToGame(session);
    }

    private static void updateUserStats(GameSession session, GameState state) {
        String status = state.getStatus();

        if (session.getWinRecorded()) {
            return;
        }
        session.setWinRecorded(true);

        if ("PLAYER1_WIN".equals(status)) {
            userService.incrementWinsVsPlayer(session.getPlayer1().getId());
        } else if ("PLAYER2_WIN".equals(status)) {
            userService.incrementWinsVsPlayer(session.getPlayer2().getId());
        }
        // При ничьей ничего не добавляем
    }

    private static void broadcastToGame(GameSession session) {
        broadcastToGame(session, null);
    }

    private static void broadcastToGame(GameSession session, Socket excludeSocket) {
        if (session == null) return;
        JsonObject response = gameUpdateResponse(session);
        String message = gson.toJson(response);

        for (Socket socket : session.getSockets()) {
            if (excludeSocket != null && socket.equals(excludeSocket)) continue;
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

    private static JsonObject gameUpdateResponse(GameSession session) {
        JsonObject response = new JsonObject();
        response.addProperty("type", "game_update");
        response.add("gameState", gson.toJsonTree(session.getState()));
        response.add("player1", gson.toJsonTree(session.getPlayer1()));
        response.add("player2", gson.toJsonTree(session.getPlayer2()));
        return response;
    }

    private static JsonObject errorResponse(String message) {
        JsonObject error = new JsonObject();
        error.addProperty("type", "error");
        error.addProperty("message", message);
        return error;
    }
}