package com.last_battle.kalah.feature.lobbies;

import com.last_battle.kalah.core.domain.model.Lobby;
import com.last_battle.kalah.core.ui.base.ViewModel;
import com.last_battle.kalah.feature.LobbyVM;
import com.last_battle.kalah.feature.ProfileVM;
import com.last_battle.kalah.usecases.lobbies.GetLobbiesUC;
import com.last_battle.kalah.usecases.lobbies.JoinLobbyUC;
import javafx.application.Platform;
import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LobbiesVM extends ViewModel {
    private final GetLobbiesUC getLobbiesUC;
    private final JoinLobbyUC joinLobbyUC;
    private final LobbyVM sharedLobbyVM;

    private final ObjectProperty<List<Lobby>> lobbies = new SimpleObjectProperty<>(new ArrayList<>());
    private final BooleanProperty isLoading = new SimpleBooleanProperty(false);
    private final StringProperty error = new SimpleStringProperty();
    private final ObjectProperty<Lobby> joinedLobby = new SimpleObjectProperty<>();

    public LobbiesVM(
            GetLobbiesUC getLobbiesUC,
            JoinLobbyUC joinLobbyUC,
            LobbyVM sharedLobbyVM
    ) {
        this.getLobbiesUC = getLobbiesUC;
        this.joinLobbyUC = joinLobbyUC;
        this.sharedLobbyVM = sharedLobbyVM;
    }

    public ObjectProperty<List<Lobby>> lobbiesProperty() { return lobbies; }
    public BooleanProperty isLoadingProperty() { return isLoading; }
    public StringProperty errorProperty() { return error; }
    public ObjectProperty<Lobby> joinedLobbyProperty() { return joinedLobby; }

    public void loadLobbies() {
        isLoading.set(true);
        error.set(null);

        CompletableFuture.runAsync(() -> {
            try {
                List<Lobby> result = getLobbiesUC.invoke();
                Platform.runLater(() -> {
                    lobbies.set(result);
                    isLoading.set(false);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    error.set(e.getMessage());
                    isLoading.set(false);
                });
            }
        });
    }

    public void joinLobby(Lobby lobby) {
        isLoading.set(true);
        error.set(null);

        CompletableFuture.runAsync(() -> {
            try {
                Lobby result = joinLobbyUC.invoke(
                        lobby.getId()
                );
                Platform.runLater(() -> {
                    joinedLobby.set(result);
                    sharedLobbyVM.trackLobby(
                            result.getId()
                    );
                    isLoading.set(false);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    error.set(e.getMessage());
                    isLoading.set(false);
                });
            }
        });
    }
}