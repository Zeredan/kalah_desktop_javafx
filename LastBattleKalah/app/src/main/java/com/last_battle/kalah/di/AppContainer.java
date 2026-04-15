package com.last_battle.kalah.di;

import com.last_battle.kalah.data.auth_d.AuthRemoteDataSource;
import com.last_battle.kalah.data.game_d.GameRemoteDataSource;
import com.last_battle.kalah.data.lobbies_d.LobbiesRemoteDataSource;
import com.last_battle.kalah.data.settings_d.SettingsLocalDataSource;
import com.last_battle.kalah.data.users_d.UsersRemoteDataSource;
import com.last_battle.kalah.data_impl.auth_d_impl.AuthRemoteDataSourceHttpImpl;
import com.last_battle.kalah.data_impl.game_d_impl.GameRemoteDataSourceSocketImpl;
import com.last_battle.kalah.data_impl.lobbies_d_impl.LobbiesRemoteDataSourceHttpImpl;
import com.last_battle.kalah.data_impl.settings_d_impl.SettingsLocalDataSourcePrefImpl;
import com.last_battle.kalah.data_impl.users_d_impl.UsersRemoteDataSourceHttpImpl;
import com.last_battle.kalah.domain.auth.AuthRepository;
import com.last_battle.kalah.domain.game.GameRepository;
import com.last_battle.kalah.domain.lobbies.LobbiesRepository;
import com.last_battle.kalah.domain.settings.SettingsRepository;
import com.last_battle.kalah.domain.users.UsersRepository;
import com.last_battle.kalah.domain_impl.auth_impl.AuthRepositoryRemoteImpl;
import com.last_battle.kalah.domain_impl.game_impl.GameRepositoryRemoteImpl;
import com.last_battle.kalah.domain_impl.lobbies_impl.LobbiesRepositoryImpl;
import com.last_battle.kalah.domain_impl.settings_impl.SettingsRepositoryLocalImpl;
import com.last_battle.kalah.domain_impl.users_impl.UsersRepositoryRemoteImpl;
import com.last_battle.kalah.feature.*;
import com.last_battle.kalah.feature.lobbies.LobbiesVM;
import com.last_battle.kalah.feature.lobby_creation.LobbyCreationVM;
import com.last_battle.kalah.feature.top_players.TopPlayersVM;
import com.last_battle.kalah.usecases.auth.*;
import com.last_battle.kalah.usecases.game.LeaveGameUC;
import com.last_battle.kalah.usecases.game.MakeMoveUC;
import com.last_battle.kalah.usecases.game.TrackGameUC;
import com.last_battle.kalah.usecases.lobbies.*;
import com.last_battle.kalah.usecases.settings.GetDarkModeUC;
import com.last_battle.kalah.usecases.settings.GetGameThemeModeUC;
import com.last_battle.kalah.usecases.settings.SetDarkModeUC;
import com.last_battle.kalah.usecases.settings.SetGameThemeModeUC;
import com.last_battle.kalah.usecases.top_users.GetTopUsersUC;

import java.net.http.HttpClient;
import java.util.prefs.Preferences;

public class AppContainer {
    // Use Cases
    private RegisterUserUC registerUserUC;
    private LogInUserUC logInUserUC;
    private LogoutUserUC logoutUserUC;
    private GetSavedCredentialsUC getSavedCredentialsUC;
    private SaveCredentialsUC saveCredentialsUC;
    private GetTopUsersUC getTopUsersUC;
    private GetDarkModeUC getDarkModeUC;
    private SetDarkModeUC setDarkModeUC;
    private GetGameThemeModeUC getGameThemeModeUC;
    private SetGameThemeModeUC setGameThemeModeUC;
    private GetLobbiesUC getLobbiesUC;
    private CreateLobbyUC createLobbyUC;
    private JoinLobbyUC joinLobbyUC;
    private LeaveLobbyUC leaveLobbyUC;
    private SetReadyUC setReadyUC;
    private StartGameUC startGameUC;
    private TrackLobbyUC trackLobbyUC;
    private TrackGameUC trackGameUC;
    private MakeMoveUC makeMoveUC;
    private LeaveGameUC leaveGameUC;

    // Repositories
    private AuthRepository authRepository;
    private SettingsRepository settingsRepository;
    private UsersRepository usersRepository;
    private LobbiesRepository lobbiesRepository;
    private GameRepository gameRepository;

    // Data Sources
    private AuthRemoteDataSource authRemoteDataSource;
    private SettingsLocalDataSource settingsLocalDataSource;
    private UsersRemoteDataSource usersRemoteDataSource;
    private LobbiesRemoteDataSource lobbiesRemoteDataSource;
    private GameRemoteDataSource gameRemoteDataSource;

    // Core utils
    private HttpClient httpClient;
    private Preferences preferences;

    private String baseURL = "http://localhost:8080/api";
    private String socketHost = "localhost";
    56 24

    private int socketLobbiesPort = 8081;
    private int socketGamePort = 8082;

    public AppContainer() {
        this.httpClient = HttpClient.newHttpClient();
        this.preferences = Preferences.userRoot().node("kalah_player2");

        // Data Sources
        this.authRemoteDataSource = new AuthRemoteDataSourceHttpImpl(httpClient, baseURL);
        this.settingsLocalDataSource = new SettingsLocalDataSourcePrefImpl(preferences);
        this.usersRemoteDataSource = new UsersRemoteDataSourceHttpImpl(httpClient, baseURL);
        this.lobbiesRemoteDataSource = new LobbiesRemoteDataSourceHttpImpl(httpClient, baseURL, socketHost, socketLobbiesPort);
        this.gameRemoteDataSource = new GameRemoteDataSourceSocketImpl(socketHost, socketGamePort);

        // Repositories
        this.authRepository = new AuthRepositoryRemoteImpl(authRemoteDataSource);
        this.settingsRepository = new SettingsRepositoryLocalImpl(settingsLocalDataSource);
        this.usersRepository = new UsersRepositoryRemoteImpl(usersRemoteDataSource);
        /*ouc*/this.getSavedCredentialsUC = new GetSavedCredentialsUC(settingsRepository);
        this.lobbiesRepository = new LobbiesRepositoryImpl(lobbiesRemoteDataSource, getSavedCredentialsUC);
        this.gameRepository = new GameRepositoryRemoteImpl(gameRemoteDataSource, getSavedCredentialsUC);

        // Auth Use Cases
        this.registerUserUC = new RegisterUserUC(authRepository);
        this.logInUserUC = new LogInUserUC(authRepository);
        this.logoutUserUC = new LogoutUserUC(settingsRepository);
        this.saveCredentialsUC = new SaveCredentialsUC(settingsRepository);

        // Settings Use Cases
        this.getDarkModeUC = new GetDarkModeUC(settingsRepository);
        this.setDarkModeUC = new SetDarkModeUC(settingsRepository);
        this.getGameThemeModeUC = new GetGameThemeModeUC(settingsRepository);
        this.setGameThemeModeUC = new SetGameThemeModeUC(settingsRepository);

        // Users Use Cases
        this.getTopUsersUC = new GetTopUsersUC(usersRepository);

        // Lobbies Use Cases
        this.getLobbiesUC = new GetLobbiesUC(lobbiesRepository);
        this.createLobbyUC = new CreateLobbyUC(lobbiesRepository);
        this.joinLobbyUC = new JoinLobbyUC(lobbiesRepository);
        this.leaveLobbyUC = new LeaveLobbyUC(lobbiesRepository);
        this.setReadyUC = new SetReadyUC(lobbiesRepository);
        this.startGameUC = new StartGameUC(lobbiesRepository);
        this.trackLobbyUC = new TrackLobbyUC(lobbiesRepository);

        // Game Use Cases
        this.trackGameUC = new TrackGameUC(gameRepository);
        this.makeMoveUC = new MakeMoveUC(gameRepository);
        this.leaveGameUC = new LeaveGameUC(gameRepository);
    }

    // ==================== ViewModels ====================

    public ProfileVM provideProfileVM() {
        return new ProfileVM(
                registerUserUC,
                logInUserUC,
                getSavedCredentialsUC,
                logoutUserUC,
                saveCredentialsUC
        );
    }

    public GameVsBotVM provideGameVsBotVM() {
        return new GameVsBotVM();
    }

    public TopPlayersVM provideTopPlayersVM() {
        return new TopPlayersVM(getTopUsersUC);
    }

    public SettingsVM provideSettingsVM() {
        return new SettingsVM(
                getDarkModeUC,
                setDarkModeUC,
                getGameThemeModeUC,
                setGameThemeModeUC
        );
    }

    public LobbyVM provideLobbyVM() {
        return new LobbyVM(
                trackLobbyUC,
                setReadyUC,
                startGameUC,
                leaveLobbyUC
        );
    }

    public LobbiesVM provideLobbiesVM(LobbyVM lobbyVM) {
        return new LobbiesVM(
                getLobbiesUC,
                joinLobbyUC,
                lobbyVM
        );
    }

    public LobbyCreationVM provideLobbyCreationVM(LobbyVM lobbyVM) {
        return new LobbyCreationVM(
                createLobbyUC,
                lobbyVM
        );
    }
    public GameVM provideGameVM() {
        return new GameVM(trackGameUC, makeMoveUC, leaveGameUC);
    }
}