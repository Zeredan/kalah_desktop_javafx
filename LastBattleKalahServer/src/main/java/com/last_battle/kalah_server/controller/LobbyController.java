package com.last_battle.kalah_server.controller;

import com.last_battle.kalah_server.model.Lobby;
import com.last_battle.kalah_server.model.User;
import com.last_battle.kalah_server.service.LobbyService;
import com.last_battle.kalah_server.service.UsersService;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;

public class LobbyController {
    private final UsersService userService = new UsersService();

    public void getLobbies(Context ctx) {
        List<Lobby> lobbies = LobbyService.getInstance().getAllLobbies();
        for (Lobby l : lobbies) {
            System.out.println(l.getId());
        }
        ctx.json(lobbies);
    }

    public void createLobby(Context ctx) {
        CreateLobbyRequest req = ctx.bodyAsClass(CreateLobbyRequest.class);
        
        User user = userService.authenticate(req.username, req.password);
        if (user == null) {
            ctx.status(401).json(Map.of("error", "Invalid credentials"));
            return;
        }
        
        Lobby lobby = LobbyService.getInstance().createLobby(req.name, req.stones, req.holes, user);
        ctx.status(201).json(lobby);
    }

    public void joinLobby(Context ctx) {
        String lobbyId = ctx.pathParam("lobbyId");
        JoinLobbyRequest req = ctx.bodyAsClass(JoinLobbyRequest.class);
        
        User user = userService.authenticate(req.username, req.password);
        if (user == null) {
            ctx.status(401).json(Map.of("error", "Invalid credentials"));
            return;
        }
        
        Lobby lobby = LobbyService.getInstance().joinLobby(lobbyId, user);
        if (lobby == null) {
            ctx.status(400).json(Map.of("error", "Lobby is full or not found"));
            return;
        }
        
        ctx.json(lobby);
    }

    public static class CreateLobbyRequest {
        public String name;
        public int stones;
        public int holes;
        public String username;
        public String password;
    }

    public static class JoinLobbyRequest {
        public String username;
        public String password;
    }
}