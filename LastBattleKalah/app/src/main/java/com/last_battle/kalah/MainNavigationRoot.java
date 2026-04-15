package com.last_battle.kalah;

import com.last_battle.kalah.core.ui.base.BaseFeature;
import com.last_battle.kalah.di.AppContainer;
import com.last_battle.kalah.feature.GameVM;
import com.last_battle.kalah.feature.GameVsBotVM;
import com.last_battle.kalah.feature.LobbyVM;
import com.last_battle.kalah.feature.ProfileVM;
import com.last_battle.kalah.feature.game_vs_player.GameFeature;
import com.last_battle.kalah.feature.lobbies.LobbiesFeature;
import com.last_battle.kalah.feature.lobby.LobbyFeature;
import com.last_battle.kalah.feature.lobby_creation.LobbyCreationFeature;
import com.last_battle.kalah.feature.lobby_creation.LobbyCreationVM;
import com.last_battle.kalah.feature.login.LoginFeature;
import com.last_battle.kalah.feature.main.MainFeature;
import com.last_battle.kalah.feature.profile.ProfileFeature;
import com.last_battle.kalah.feature.register.RegisterFeature;
import com.last_battle.kalah.feature.settings_feat.SettingsFeature;
import com.last_battle.kalah.feature.top_players.TopPlayersFeature;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainNavigationRoot {
    private Stage stage;
    private BaseFeature currentFeature;
    private AppContainer appContainer;

    private ProfileVM profileVM;
    private GameVsBotVM gameVsBotVM;
    private LobbyVM lobbyVM;
    private GameVM gameVM;

    public MainNavigationRoot(Stage stage) {
        this.stage = stage;
        appContainer = new AppContainer();

        profileVM = appContainer.provideProfileVM();
        gameVsBotVM = appContainer.provideGameVsBotVM();
        lobbyVM = appContainer.provideLobbyVM();
        gameVM = appContainer.provideGameVM();

        activateMainFeature();
    }

    public void activateMainFeature() {
        MainFeature feature = new MainFeature(
                profileVM,
                gameVsBotVM,
                (v) -> {
                    switch (v) {
                        case 1:
                            activateTopPlayersFeature();
                            break;
                        case 2:
                            activateSettingsFeature();
                            break;
                    }
                },
                () -> {
                    if (gameVM.getCurrentGameId() != null) {
                        activateGameVsPlayerFeature();
                    } else {
                        activateLobbiesFeature();
                    }
                },
                () -> {
                    System.out.println("PWB");
                },
                () -> {
                    activateProfileFeature();
                },
                () -> {
                    activateRegisterFeature();
                },
                () -> {
                    activateLoginFeature();
                }
        );
        currentFeature = feature;

        Scene scene = new Scene(feature.getRoot(), 800, 600);
        stage.setScene(scene);
        stage.setTitle("Kalah Game (Main)");
        stage.show();
    }

    public void activateTopPlayersFeature() {
        TopPlayersFeature feature = new TopPlayersFeature(
                profileVM,
                appContainer.provideTopPlayersVM(),
                (v) -> {
                    switch (v) {
                        case 0:
                            activateMainFeature();
                            break;
                        case 2:
                            activateSettingsFeature();
                            break;
                    }
                },
                () -> {
                    activateProfileFeature();
                },
                () -> {
                    activateRegisterFeature();
                },
                () -> {
                    activateLoginFeature();
                }
        );
        currentFeature = feature;

        Scene scene = new Scene(feature.getRoot(), 800, 600);
        stage.setScene(scene);
        stage.setTitle("Kalah Game (Top Players)");
        stage.show();
    }

    public void activateSettingsFeature() {
        SettingsFeature feature = new SettingsFeature(
                profileVM,
                appContainer.provideSettingsVM(),
                (v) -> {
                    switch (v) {
                        case 0:
                            activateMainFeature();
                            break;
                        case 1:
                            activateTopPlayersFeature();
                            break;
                    }
                },
                () -> {
                    activateProfileFeature();
                },
                () -> {
                    activateRegisterFeature();
                },
                () -> {
                    activateLoginFeature();
                },
                () -> {
                    System.out.println("A_DEV");
                },
                () -> {
                    System.out.println("A_SYS");
                }
        );
        currentFeature = feature;

        Scene scene = new Scene(feature.getRoot(), 800, 600);
        stage.setScene(scene);
        stage.setTitle("Kalah Game (Settings)");
        stage.show();
    }


    public void activateProfileFeature() {
        ProfileFeature feature = new ProfileFeature(
                profileVM,
                (v) -> {
                    switch (v) {
                        case 0:
                            activateMainFeature();
                            break;
                        case 1:
                            activateTopPlayersFeature();
                            break;
                        case 2:
                            activateSettingsFeature();
                    }
                },
                () -> {
                    activateMainFeature();
                }
        );
        currentFeature = feature;

        Scene scene = new Scene(feature.getRoot(), 800, 600);
        stage.setScene(scene);
        stage.setTitle("Kalah Game (Profile)");
        stage.show();
    }

    public void activateRegisterFeature() {
        RegisterFeature feature = new RegisterFeature(
                profileVM,
                (v) -> {
                    switch (v) {
                        case 0:
                            activateMainFeature();
                            break;
                        case 1:
                            activateTopPlayersFeature();
                            break;
                        case 2:
                            activateSettingsFeature();
                    }
                },
                () -> {
                    activateMainFeature();
                }
        );
        currentFeature = feature;

        Scene scene = new Scene(feature.getRoot(), 800, 600);
        stage.setScene(scene);
        stage.setTitle("Kalah Game (Register)");
        stage.show();
    }

    public void activateLoginFeature() {
        LoginFeature feature = new LoginFeature(
                profileVM,
                (v) -> {
                    switch (v) {
                        case 0:
                            activateMainFeature();
                            break;
                        case 1:
                            activateTopPlayersFeature();
                            break;
                        case 2:
                            activateSettingsFeature();
                    }
                },
                () -> {
                    activateMainFeature();
                }
        );
        currentFeature = feature;

        Scene scene = new Scene(feature.getRoot(), 800, 600);
        stage.setScene(scene);
        stage.setTitle("Kalah Game (Login)");
        stage.show();
    }

    public void activateLobbiesFeature() {
        LobbiesFeature feature = new LobbiesFeature(
                appContainer.provideLobbiesVM(lobbyVM),
                profileVM,
                (v) -> {
                    switch (v) {
                        case 0:
                            activateMainFeature();
                            break;
                        case 1:
                            activateTopPlayersFeature();
                            break;
                        case 2:
                            activateSettingsFeature();
                    }
                },
                () -> {
                    activateProfileFeature();
                },
                () -> {
                    activateLobbyCreationFeature();
                },
                () -> {
                    activateLobbyFeature();
                }
        );
        currentFeature = feature;

        Scene scene = new Scene(feature.getRoot(), 800, 600);
        stage.setScene(scene);
        stage.setTitle("Kalah Game (Lobbies)");
        stage.show();
    }

    public void activateLobbyCreationFeature() {
        LobbyCreationFeature feature = new LobbyCreationFeature(
                appContainer.provideLobbyCreationVM(lobbyVM),
                profileVM,
                (v) -> {
                    switch (v) {
                        case 0:
                            activateMainFeature();
                            break;
                        case 1:
                            activateTopPlayersFeature();
                            break;
                        case 2:
                            activateSettingsFeature();
                    }
                },
                () -> {
                    activateProfileFeature();
                },
                () -> {
                    activateLobbyFeature();
                }
        );
        currentFeature = feature;

        Scene scene = new Scene(feature.getRoot(), 800, 600);
        stage.setScene(scene);
        stage.setTitle("Kalah Game (lobby creation)");
        stage.show();
    }

    public void activateLobbyFeature() {
        LobbyFeature feature = new LobbyFeature(
                lobbyVM,
                profileVM,
                gameVM,
                (v) -> {
                    switch (v) {
                        case 0:
                            activateMainFeature();
                            break;
                        case 1:
                            activateTopPlayersFeature();
                            break;
                        case 2:
                            activateSettingsFeature();
                    }
                },
                () -> {
                    activateMainFeature();
                },
                () -> {
                    activateGameVsPlayerFeature();
                }
        );
        currentFeature = feature;

        Scene scene = new Scene(feature.getRoot(), 800, 600);
        stage.setOnCloseRequest((e) -> {lobbyVM.leaveLobby();});
        stage.setScene(scene);
        stage.setTitle("Kalah Game (lobby)");
        stage.show();
    }

    public void activateGameVsPlayerFeature() {
        GameFeature feature = new GameFeature(
                gameVM,
                profileVM,
                (v) -> {
                    switch (v) {
                        case 0:
                            activateMainFeature();
                            break;
                        case 1:
                            activateTopPlayersFeature();
                            break;
                        case 2:
                            activateSettingsFeature();
                    }
                },
                () -> {
                    activateMainFeature();
                },
                () -> {
                    activateMainFeature();
                }
        );
        currentFeature = feature;

        Scene scene = new Scene(feature.getRoot(), 800, 600);
        stage.setOnCloseRequest((e) -> {gameVM.leaveGame();});
        stage.setScene(scene);
        stage.setTitle("Kalah Game (game vs player)");
        stage.show();
    }
}
