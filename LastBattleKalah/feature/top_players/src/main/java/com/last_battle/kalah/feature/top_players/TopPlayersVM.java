package com.last_battle.kalah.feature.top_players;

import com.last_battle.kalah.core.domain.model.User;
import com.last_battle.kalah.core.ui.base.ViewModel;
import com.last_battle.kalah.usecases.top_users.GetTopUsersUC;
import javafx.application.Platform;
import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TopPlayersVM extends ViewModel {

    private final GetTopUsersUC getTopUsersUC;
    private final ObjectProperty<List<User>> topPlayers = new SimpleObjectProperty<>(null);

    public TopPlayersVM(GetTopUsersUC getTopUsersUC) {
        this.getTopUsersUC = getTopUsersUC;
    }

    public void loadTopPlayers() {
        this.topPlayers.set(null);
        CompletableFuture.runAsync(() -> {
            try {
                List<User> topPlayers = getTopUsersUC.invoke(15);
                Platform.runLater(() -> this.topPlayers.set(topPlayers));
            } catch (Exception e) {
                System.out.println("QWEQWEQWE" + e.getMessage());
            }
        });
    }

    public ObjectProperty<List<User>> topPlayersProperty() {
        return topPlayers;
    }
}