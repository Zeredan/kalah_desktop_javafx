package com.last_battle.kalah.feature.lobby_creation;

import com.last_battle.kalah.core.domain.model.Lobby;
import com.last_battle.kalah.core.domain.model.User;
import com.last_battle.kalah.core.ui.base.ViewModel;
import com.last_battle.kalah.feature.LobbyVM;
import com.last_battle.kalah.feature.ProfileVM;
import com.last_battle.kalah.usecases.lobbies.CreateLobbyUC;
import javafx.application.Platform;
import javafx.beans.property.*;

import java.util.concurrent.CompletableFuture;

public class LobbyCreationVM extends ViewModel {
    private final CreateLobbyUC createLobbyUC;
    private final LobbyVM lobbyVM;

    private final StringProperty name = new SimpleStringProperty();
    private final IntegerProperty stones = new SimpleIntegerProperty(6);
    private final IntegerProperty holes = new SimpleIntegerProperty(6);

    private final ObjectProperty<Lobby> createdLobby = new SimpleObjectProperty<>();
    private final BooleanProperty isLoading = new SimpleBooleanProperty(false);
    private final StringProperty error = new SimpleStringProperty();

    public LobbyCreationVM(
            CreateLobbyUC createLobbyUC,
            LobbyVM lobbyVM
    ) {
        this.createLobbyUC = createLobbyUC;
        this.lobbyVM = lobbyVM;
    }

    public StringProperty nameProperty() { return name; }
    public IntegerProperty stonesProperty() { return stones; }
    public IntegerProperty holesProperty() { return holes; }
    public ObjectProperty<Lobby> createdLobbyProperty() { return createdLobby; }
    public BooleanProperty isLoadingProperty() { return isLoading; }
    public StringProperty errorProperty() { return error; }

    public void createLobby() {
        if (name.get() == null || name.get().trim().isEmpty()) {
            error.set("Название лобби не может быть пустым");
            return;
        }

        isLoading.set(true);
        error.set(null);

        CompletableFuture.runAsync(() -> {
            try {
                Lobby lobby = createLobbyUC.invoke(
                        name.get(),
                        stones.get(),
                        holes.get()
                );
                Platform.runLater(() -> {
                    createdLobby.set(lobby);
                    lobbyVM.trackLobby(lobby.getId());
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

    public void increaseStones() {
        if (stones.get() < 12) stones.set(stones.get() + 1);
    }

    public void decreaseStones() {
        if (stones.get() > 1) stones.set(stones.get() - 1);
    }

    public void increaseHoles() {
        if (holes.get() < 12) holes.set(holes.get() + 1);
    }

    public void decreaseHoles() {
        if (holes.get() > 3) holes.set(holes.get() - 1);
    }
}