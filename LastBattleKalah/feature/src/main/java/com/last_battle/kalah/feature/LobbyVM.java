package com.last_battle.kalah.feature;

import com.last_battle.kalah.core.domain.model.Lobby;
import com.last_battle.kalah.core.ui.base.ViewModel;
import com.last_battle.kalah.usecases.lobbies.LeaveLobbyUC;
import com.last_battle.kalah.usecases.lobbies.SetReadyUC;
import com.last_battle.kalah.usecases.lobbies.StartGameUC;
import com.last_battle.kalah.usecases.lobbies.TrackLobbyUC;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class LobbyVM extends ViewModel {
    private final TrackLobbyUC trackLobbyUC;
    private final SetReadyUC setReadyUC;
    private final StartGameUC startGameUC;
    private final LeaveLobbyUC leaveLobbyUC;

    private final ObjectProperty<Lobby> currentLobby = new SimpleObjectProperty<>();
    private final ObjectProperty<Boolean> isConnected = new SimpleObjectProperty<>(false);
    private final ObjectProperty<String> error = new SimpleObjectProperty<>();

    public LobbyVM(
            TrackLobbyUC trackLobbyUC,
            SetReadyUC setReadyUC,
            StartGameUC startGameUC,
            LeaveLobbyUC leaveLobbyUC
    ) {
        this.trackLobbyUC = trackLobbyUC;
        this.setReadyUC = setReadyUC;
        this.startGameUC = startGameUC;
        this.leaveLobbyUC = leaveLobbyUC;
    }

    public ObjectProperty<Lobby> currentLobbyProperty() {
        return currentLobby;
    }

    public ObjectProperty<Boolean> isConnectedProperty() {
        return isConnected;
    }

    public ObjectProperty<String> errorProperty() {
        return error;
    }

    public void trackLobby(String lobbyId) {
        isConnected.set(true);

        trackLobbyUC.invoke(lobbyId, lobby -> {
            Platform.runLater(() -> {
                if (lobby == null) {
                    currentLobby.set(null);
                    isConnected.set(false);
                } else {
                    currentLobby.set(lobby);
                }
            });
        });
    }

    public void setReady(boolean ready) {
        setReadyUC.invoke(ready);
    }

    public void startGame() {
        startGameUC.invoke();
    }

    public void leaveLobby() {
        leaveLobbyUC.invoke();
        currentLobby.set(null);
        isConnected.set(false);
    }
}