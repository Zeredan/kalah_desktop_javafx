package com.last_battle.kalah.feature;

import com.last_battle.kalah.core.domain.model.GameInfo;
import com.last_battle.kalah.core.domain.model.GameState;
import com.last_battle.kalah.core.domain.model.User;
import com.last_battle.kalah.core.ui.base.ViewModel;
import com.last_battle.kalah.usecases.game.LeaveGameUC;
import com.last_battle.kalah.usecases.game.MakeMoveUC;
import com.last_battle.kalah.usecases.game.TrackGameUC;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class GameVM extends ViewModel {
    private final TrackGameUC trackGameUC;
    private final MakeMoveUC makeMoveUC;
    private final LeaveGameUC leaveGameUC;
    private String currentGameId;
    
    private final ObjectProperty<GameInfo> gameInfo = new SimpleObjectProperty<>();
    private final ObjectProperty<Boolean> isLoading = new SimpleObjectProperty<>(true);
    private final ObjectProperty<String> error = new SimpleObjectProperty<>();

    public GameVM(TrackGameUC trackGameUC, MakeMoveUC makeMoveUC, LeaveGameUC leaveGameUC) {
        this.trackGameUC = trackGameUC;
        this.makeMoveUC = makeMoveUC;
        this.leaveGameUC = leaveGameUC;
    }

    public void trackGame(String gameId) {
        if (gameId.equals(currentGameId)) return;

        currentGameId = gameId;
        isLoading.set(true);
        
        trackGameUC.invoke(gameId, gameInfo -> {
            Platform.runLater(() -> {
                this.gameInfo.set(gameInfo);
                isLoading.set(false);
            });
            Platform.runLater(() -> {
                // Подробный вывод в консоль
                System.out.println("========== GAME UPDATE RECEIVED ==========");
                System.out.println("GameInfo received: " + (gameInfo != null ? "NOT NULL" : "NULL"));

                if (gameInfo != null) {
                    GameState state = gameInfo.getGameState();
                    User player1 = gameInfo.getPlayer1();
                    User player2 = gameInfo.getPlayer2();

                    System.out.println("--- Players ---");
                    System.out.println("Player1: " + (player1 != null ? player1.getUsername() + " (id: " + player1.getId() + ")" : "NULL"));
                    System.out.println("Player2: " + (player2 != null ? player2.getUsername() + " (id: " + player2.getId() + ")" : "NULL"));

                    if (state != null) {
                        System.out.println("--- Game State ---");
                        System.out.println("holesCount: " + state.getHolesCount());
                        System.out.println("initialStonesCount: " + state.getInitialStonesCount());
                        System.out.println("currentPlayerInd: " + state.getCurrentPlayerInd());
                        System.out.println("status: " + state.getStatus());
                        System.out.println("isMakingMove: " + state.isMakingMove());
                        System.out.println("player1Kalah: " + state.getPlayer1Kalah());
                        System.out.println("player2Kalah: " + state.getPlayer2Kalah());

                        System.out.println("player1Holes: " + state.getPlayer1Holes());
                        System.out.println("player2Holes: " + state.getPlayer2Holes());

                        // Проверка размеров списков
                        if (state.getPlayer1Holes() != null) {
                            System.out.println("player1Holes size: " + state.getPlayer1Holes().size());
                        } else {
                            System.out.println("player1Holes is NULL!");
                        }

                        if (state.getPlayer2Holes() != null) {
                            System.out.println("player2Holes size: " + state.getPlayer2Holes().size());
                        } else {
                            System.out.println("player2Holes is NULL!");
                        }
                    } else {
                        System.out.println("GameState is NULL!");
                    }
                }
                System.out.println("==========================================");

                this.gameInfo.set(gameInfo);
                isLoading.set(false);
            });
        });
    }

    public void makeMove(int holeIndex) {
        makeMoveUC.invoke(holeIndex);
    }

    public void leaveGame() {
        leaveGameUC.invoke();
        gameInfo.set(null);
        currentGameId = null;
    }

    // Геттеры
    public ObjectProperty<GameInfo> gameInfoProperty() { return gameInfo; }
    public ObjectProperty<Boolean> isLoadingProperty() { return isLoading; }
    public ObjectProperty<String> errorProperty() { return error; }

    public String getCurrentGameId() {
        return currentGameId;
    }
}