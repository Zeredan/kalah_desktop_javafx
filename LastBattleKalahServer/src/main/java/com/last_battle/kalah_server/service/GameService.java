package com.last_battle.kalah_server.service;

import com.last_battle.kalah_server.model.GameSession;
import com.last_battle.kalah_server.model.GameState;
import com.last_battle.kalah_server.model.Lobby;
import com.last_battle.kalah_server.model.User;

import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameService {
    private static GameService instance;
    private final ConcurrentHashMap<String, GameSession> games = new ConcurrentHashMap<>();

    private GameService() {}

    public static synchronized GameService getInstance() {
        if (instance == null) {
            instance = new GameService();
        }
        return instance;
    }

    public GameSession createGame(Lobby lobby, User player1, User player2) {
        String gameId = UUID.randomUUID().toString();
        GameState state = new GameState(lobby.getHoles(), lobby.getStones());
        GameSession session = new GameSession(gameId, player1, player2, state);
        games.put(gameId, session);
        return session;
    }

    public GameSession getGame(String id) {
        return games.get(id);
    }

    public void addSocket(String gameId, Socket socket) {
        GameSession session = games.get(gameId);
        if (session != null) {
            session.getSockets().add(socket);
        }
    }

    public void removeSocket(String gameId, Socket socket) {
        GameSession session = games.get(gameId);
        if (session != null) {
            session.getSockets().remove(socket);
        }
    }

    public void updateGameState(String gameId, GameState newState) {
        GameSession session = games.get(gameId);
        if (session != null) {
            session.setState(newState);
        }
    }
}