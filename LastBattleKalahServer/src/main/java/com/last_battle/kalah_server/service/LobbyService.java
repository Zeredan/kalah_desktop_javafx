package com.last_battle.kalah_server.service;

import com.last_battle.kalah_server.model.GameSession;
import com.last_battle.kalah_server.model.Lobby;
import com.last_battle.kalah_server.model.User;

import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LobbyService {
    
    // Singleton instance
    private static LobbyService instance;
    
    private final Map<String, Lobby> lobbies = new ConcurrentHashMap<>();
    
    private LobbyService() {}
    
    public static synchronized LobbyService getInstance() {
        if (instance == null) {
            instance = new LobbyService();
        }
        return instance;
    }
    
    public List<Lobby> getAllLobbies() {
        return new ArrayList<>(lobbies.values());
    }

    public Lobby getLobby(String id) {
        return lobbies.get(id);
    }

    public Lobby createLobby(String name, int stones, int holes, User leader) {
        String id = UUID.randomUUID().toString();
        Lobby lobby = new Lobby(id, name, stones, holes, leader);
        lobbies.put(id, lobby);
        System.out.println("[LobbyService] Created lobby: " + id + " (" + name + "), total: " + lobbies.size());
        return lobby;
    }

    public Lobby joinLobby(String lobbyId, User guest) {
        Lobby lobby = lobbies.get(lobbyId);
        if (lobby == null || lobby.isFull()) return null;
        
        lobby.setGuest(guest);
        lobby.getReadyStatus().put(guest.getId(), false);
        return lobby;
    }

    public Lobby leaveLobby(String lobbyId, User user) {
        Lobby lobby = lobbies.get(lobbyId);
        if (lobby == null) return null;

        if (lobby.getLeader().getId().equals(user.getId())) {
            User guest = lobby.getGuest();
            if (guest != null) {
                lobby.setLeader(guest);
                lobby.setGuest(null);
                lobby.getReadyStatus().remove(guest.getId());
                lobby.getReadyStatus().put(guest.getId(), true);
            } else {
                lobbies.remove(lobbyId);
                return null;
            }
        } else if (lobby.getGuest() != null && lobby.getGuest().getId().equals(user.getId())) {
            lobby.setGuest(null);
            lobby.getReadyStatus().remove(user.getId());
        }
        return lobby;
    }

    public Lobby setReady(String lobbyId, User user, boolean ready) {
        Lobby lobby = lobbies.get(lobbyId);
        if (lobby == null) return null;
        
        if (!lobby.getLeader().getId().equals(user.getId()) && 
            lobby.getGuest() != null && 
            lobby.getGuest().getId().equals(user.getId())) {
            lobby.getReadyStatus().put(user.getId(), ready);
        }
        return lobby;
    }

    public Lobby startGame(String lobbyId, User user) {
        Lobby lobby = lobbies.get(lobbyId);
        if (lobby == null) return null;
        
        if (lobby.getLeader().getId().equals(user.getId()) && lobby.areBothReady()) {
            GameSession gameSession = GameService.getInstance().createGame(lobby, lobby.getLeader(), lobby.getGuest());
            lobby.setGameId(gameSession.getId());
        }
        return lobby;
    }

    public void addSocket(String lobbyId, Socket socket) {
        Lobby lobby = lobbies.get(lobbyId);
        if (lobby != null) {
            lobby.getSockets().add(socket);
        }
    }

    public void removeSocket(String lobbyId, Socket socket) {
        Lobby lobby = lobbies.get(lobbyId);
        if (lobby != null) {
            lobby.getSockets().remove(socket);
        }
    }
}