package com.last_battle.kalah_server;

import com.last_battle.kalah_server.controller.AuthController;
import com.last_battle.kalah_server.controller.LobbyController;
import com.last_battle.kalah_server.controller.UsersController;
import com.last_battle.kalah_server.socket.LobbySocketHandler;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class KalahServer {

    public static void main(String[] args) {
        new Thread(LobbySocketHandler::start).start();

        AuthController authController = new AuthController();
        UsersController usersController = new UsersController();
        LobbyController lobbyController = new LobbyController();

        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            config.router.apiBuilder(() -> {
                path("/api/auth", () -> {
                    post("/register", authController::register);
                    post("/login", authController::login);
                });
                path("/api/users", () -> {
                    get("/top", usersController::getTopUsers);
                });
                path("/api/lobbies", () -> {
                    get("/", lobbyController::getLobbies);
                    post("/", lobbyController::createLobby);
                    post("/{lobbyId}/join", lobbyController::joinLobby);
                });
            });
        }).start(getPort());

        System.out.println("✅ Server started on http://localhost:" + getPort());
    }

    private static int getPort() {
        String port = System.getenv("PORT");
        if (port != null) {
            return Integer.parseInt(port);
        }
        return 8080;
    }
}