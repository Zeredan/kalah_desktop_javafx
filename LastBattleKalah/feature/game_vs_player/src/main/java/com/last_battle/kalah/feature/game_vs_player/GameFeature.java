package com.last_battle.kalah.feature.game_vs_player;

import com.last_battle.kalah.core.domain.model.GameInfo;
import com.last_battle.kalah.core.domain.model.GameState;
import com.last_battle.kalah.core.domain.model.User;
import com.last_battle.kalah.core.ui.MainMenuLeft;
import com.last_battle.kalah.core.ui.ProfileOrAuthCard;
import com.last_battle.kalah.core.ui.base.BaseFeature;
import com.last_battle.kalah.core.ui.base.NavigateMainMenuFunc;
import com.last_battle.kalah.core.ui.managers.Drawable;
import com.last_battle.kalah.feature.GameVM;
import com.last_battle.kalah.feature.ProfileVM;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.List;


public class GameFeature implements BaseFeature {

    private final HBox root;
    private final GameVM gameVM;
    private final ProfileVM profileVM;
    private final NavigateMainMenuFunc onNavigate;
    private final Runnable onGameEnd;
    private final Runnable onLeaveGame;

    private HBox player1HolesRow;
    private HBox player2HolesRow;
    private Label player1KalahLabel;
    private Label player2KalahLabel;
    private Label turnLabel;
    private Label opponentNameLabel;
    private Label playerNameLabel;
    private Label statusLabel;
    private boolean gameEnded = false;

    private List<Integer> reverseList(List<Integer> list) {
        List<Integer> reversed = new java.util.ArrayList<>(list);
        java.util.Collections.reverse(reversed);
        return reversed;
    }

    public GameFeature(
            GameVM gameVM,
            ProfileVM profileVM,
            NavigateMainMenuFunc onNavigate,
            Runnable onGameEnd,
            Runnable onLeaveGame
    ) {
        this.gameVM = gameVM;
        this.profileVM = profileVM;
        this.onNavigate = onNavigate;
        this.onGameEnd = onGameEnd;
        this.onLeaveGame = onLeaveGame;

        root = new HBox();

        MainMenuLeft mainMenuLeft = new MainMenuLeft(-1, onNavigate);
        VBox contentArea = createContentArea();
        root.getChildren().addAll(mainMenuLeft.getRoot(), contentArea);

        bindViewModel();
    }

    private VBox createContentArea() {
        VBox content = new VBox();
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(17, 30, 30, 30));
        content.setSpacing(20);
        content.setStyle("-fx-background-color: #141B46;");
        HBox.setHgrow(content, Priority.ALWAYS);

        HBox topBar = createTopBar();
        VBox gameBoard = createGameBoard();
        HBox bottomBar = createBottomBar();

        turnLabel = new Label();
        turnLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        turnLabel.setTextFill(Color.WHITE);
        turnLabel.setAlignment(Pos.CENTER);
        turnLabel.setPadding(new Insets(20, 0, 0, 0));

        statusLabel = new Label();
        statusLabel.setFont(Font.font("System", 14));
        statusLabel.setTextFill(Color.web("#AAAAAA"));
        statusLabel.setAlignment(Pos.CENTER);

        content.getChildren().addAll(topBar, gameBoard, turnLabel, statusLabel, bottomBar);
        return content;
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 20, 0));
        topBar.setSpacing(16);

        // Профиль противника (слева)
        HBox opponentBox = new HBox();
        opponentBox.setAlignment(Pos.CENTER_LEFT);
        opponentBox.setSpacing(8);

        ImageView opponentAvatar = new ImageView(Drawable.get("profile.png"));
        opponentAvatar.setFitWidth(40);
        opponentAvatar.setFitHeight(40);

        opponentNameLabel = new Label();
        opponentNameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        opponentNameLabel.setTextFill(Color.WHITE);

        opponentBox.getChildren().addAll(opponentAvatar, opponentNameLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button leaveButton = new Button("Выйти из игры");
        leaveButton.setFont(Font.font("System", 14));
        leaveButton.setTextFill(Color.WHITE);
        leaveButton.setStyle(
                "-fx-background-color: #703030;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 16;"
        );
        leaveButton.setOnAction(e -> {
            if (!gameEnded) {
                gameVM.leaveGame();
            }
            onLeaveGame.run();
        });

        topBar.getChildren().addAll(opponentBox, spacer, leaveButton);
        return topBar;
    }

    private HBox createBottomBar() {
        HBox bottomBar = new HBox();
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        bottomBar.setPadding(new Insets(20, 0, 0, 0));
        bottomBar.setSpacing(8);

        playerNameLabel = new Label();
        playerNameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        playerNameLabel.setTextFill(Color.WHITE);

        ImageView playerAvatar = new ImageView(Drawable.get("profile.png"));
        playerAvatar.setFitWidth(40);
        playerAvatar.setFitHeight(40);

        bottomBar.getChildren().addAll(playerNameLabel, playerAvatar);
        return bottomBar;
    }

    private VBox createGameBoard() {
        VBox board = new VBox();
        board.setAlignment(Pos.CENTER);
        board.setSpacing(15);
        board.setPadding(new Insets(20));
        board.setStyle("-fx-background-color: #1E2A5A; -fx-background-radius: 20;");

        // Верхние лунки (соперник)
        VBox opponentSection = new VBox();
        opponentSection.setAlignment(Pos.CENTER);
        opponentSection.setSpacing(8);

        Label opponentHolesLabel = new Label("Лунки противника");
        opponentHolesLabel.setFont(Font.font("System", 12));
        opponentHolesLabel.setTextFill(Color.web("#FF9999"));

        player2HolesRow = new HBox();
        player2HolesRow.setAlignment(Pos.CENTER);
        player2HolesRow.setSpacing(10);

        opponentSection.getChildren().addAll(opponentHolesLabel, player2HolesRow);

        // Средняя секция: левый калах + центральная часть + правый калах
        HBox middleSection = new HBox();
        middleSection.setAlignment(Pos.CENTER);
        middleSection.setSpacing(20);

        // Левый калах (соперника)
        VBox leftKalahBox = new VBox();
        leftKalahBox.setAlignment(Pos.CENTER);
        leftKalahBox.setSpacing(10);
        leftKalahBox.setPrefWidth(85);
        leftKalahBox.setPrefHeight(250);
        leftKalahBox.setStyle("-fx-background-color: #7D2123; -fx-background-radius: 15;");

        Label leftKalahLabel = new Label("Калах");
        leftKalahLabel.setFont(Font.font("System", 14));
        leftKalahLabel.setTextFill(Color.WHITE);

        player2KalahLabel = new Label("0");
        player2KalahLabel.setFont(Font.font("System", 28));
        player2KalahLabel.setTextFill(Color.WHITE);

        ImageView leftStoneIcon = new ImageView(Drawable.get("stones.png"));
        leftStoneIcon.setFitWidth(40);
        leftStoneIcon.setFitHeight(40);

        leftKalahBox.getChildren().addAll(leftKalahLabel, leftStoneIcon, player2KalahLabel);

        // Центральная часть
        VBox centerSection = new VBox();
        centerSection.setAlignment(Pos.CENTER);
        centerSection.setSpacing(20);

        // Лунки игрока (нижние)
        VBox playerSection = new VBox();
        playerSection.setAlignment(Pos.CENTER);
        playerSection.setSpacing(8);

        Label playerHolesLabel = new Label("Ваши лунки");
        playerHolesLabel.setFont(Font.font("System", 12));
        playerHolesLabel.setTextFill(Color.web("#99FF99"));

        player1HolesRow = new HBox();
        player1HolesRow.setAlignment(Pos.CENTER);
        player1HolesRow.setSpacing(10);

        playerSection.getChildren().addAll(playerHolesLabel, player1HolesRow);

        centerSection.getChildren().addAll(opponentSection, playerSection);

        // Правый калах (игрока)
        VBox rightKalahBox = new VBox();
        rightKalahBox.setAlignment(Pos.CENTER);
        rightKalahBox.setSpacing(10);
        rightKalahBox.setPrefWidth(85);
        rightKalahBox.setPrefHeight(250);
        rightKalahBox.setStyle("-fx-background-color: #3779C4; -fx-background-radius: 15;");

        Label rightKalahLabel = new Label("Калах");
        rightKalahLabel.setFont(Font.font("System", 14));
        rightKalahLabel.setTextFill(Color.WHITE);

        player1KalahLabel = new Label("0");
        player1KalahLabel.setFont(Font.font("System", 28));
        player1KalahLabel.setTextFill(Color.WHITE);

        ImageView rightStoneIcon = new ImageView(Drawable.get("stone.png"));
        rightStoneIcon.setFitWidth(40);
        rightStoneIcon.setFitHeight(40);

        rightKalahBox.getChildren().addAll(rightKalahLabel, rightStoneIcon, player1KalahLabel);

        middleSection.getChildren().addAll(leftKalahBox, centerSection, rightKalahBox);
        board.getChildren().addAll(middleSection);

        return board;
    }

    private void bindViewModel() {
        gameVM.gameInfoProperty().addListener((obs, old, gameInfo) -> {
            if (gameInfo != null) {
                updateUI(gameInfo);
            }
        });
    }

    private void updateUI(GameInfo gameInfo) {
        GameState state = gameInfo.getGameState();
        User currentUser = profileVM.registeredUserProperty().get();
        User opponent = gameInfo.getOpponent(currentUser.getId());
        boolean isPlayer1 = gameInfo.isPlayer1(currentUser.getId());

        opponentNameLabel.setText(opponent.getUsername());
        playerNameLabel.setText(currentUser.getUsername());

        // Обновляем калах
        player1KalahLabel.setText(String.valueOf(state.getPlayer1Kalah()));
        player2KalahLabel.setText(String.valueOf(state.getPlayer2Kalah()));

        // Обновляем лунки с учетом перспективы
        if (isPlayer1) {
            // Игрок 1 видит свои лунки снизу, чужие сверху
            updateHolesRow(player1HolesRow, state.getPlayer1Holes(), true, state, gameInfo);
            updateHolesRow(player2HolesRow, reverseList(state.getPlayer2Holes()), false, state, gameInfo);
        } else {
            // Игрок 2 видит свои лунки снизу (но они отображаются в обратном порядке)
            updateHolesRow(player1HolesRow, reverseList(state.getPlayer2Holes()), true, state, gameInfo);
            updateHolesRow(player2HolesRow, reverseList(state.getPlayer1Holes()), false, state, gameInfo);
        }

        // Проверка на досрочное окончание игры
        int holesCount = state.getHolesCount();
        int initialStones = state.getInitialStonesCount();
        int totalStones = holesCount * initialStones * 2;
        int halfStones = totalStones / 2;

        if ((state.getPlayer1Kalah() > halfStones || state.getPlayer2Kalah() > halfStones) && !gameEnded) {
            gameEnded = true;
            String winnerName;
            if (state.getPlayer1Kalah() > halfStones) {
                state.setStatus("PLAYER1_WIN");
                winnerName = gameInfo.getPlayer1().getUsername();
            } else {
                state.setStatus("PLAYER2_WIN");
                winnerName = gameInfo.getPlayer2().getUsername();
            }
            turnLabel.setText("Игра окончена! Победитель: " + winnerName);
            turnLabel.setTextFill(Color.web("#FFD700"));

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
                if (!gameEnded) return;
                gameVM.leaveGame();
                onGameEnd.run();
            }));
            timeline.setCycleCount(1);
            timeline.play();
            return;
        }

        // Обновляем статус игры (обычная проверка)
        if (state.isFinished() && !gameEnded) {
            gameEnded = true;
            String winnerName;
            if ("DRAW".equals(state.getStatus())) {
                turnLabel.setText("Игра окончена! Ничья!");
            } else {
                boolean player1Win = "PLAYER1_WIN".equals(state.getStatus());
                winnerName = (player1Win ? gameInfo.getPlayer1() : gameInfo.getPlayer2()).getUsername();
                turnLabel.setText("Игра окончена! Победитель: " + winnerName);
            }
            turnLabel.setTextFill(Color.web("#FFD700"));

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
                if (!gameEnded) return;
                gameVM.leaveGame();
                onGameEnd.run();
            }));
            timeline.setCycleCount(1);
            timeline.play();
        } else if (state.isMyTurn(currentUser.getId(), gameInfo.getPlayer1().getId(), gameInfo.getPlayer2().getId())) {
            turnLabel.setText("Ваш ход!");
            turnLabel.setTextFill(Color.web("#66FF66"));
            statusLabel.setText("Нажмите на лунку, чтобы сделать ход");
        } else {
            turnLabel.setText("Ход противника...");
            turnLabel.setTextFill(Color.web("#FF6666"));
            statusLabel.setText("Ожидайте хода соперника");
        }

        if (state.isMakingMove()) {
            statusLabel.setText("Ход выполняется...");
        }
    }

    private void updateHolesRow(HBox holesRow, List<Integer> holes, boolean isCurrentPlayer,
                                GameState state, GameInfo gameInfo) {
        holesRow.getChildren().clear();

        User currentUser = profileVM.registeredUserProperty().get();
        boolean isMyTurn = state.isMyTurn(currentUser.getId(),
                gameInfo.getPlayer1().getId(), gameInfo.getPlayer2().getId());
        boolean canMove = isMyTurn && isCurrentPlayer && !state.isFinished() && !state.isMakingMove();

        for (int i = 0; i < holes.size(); i++) {
            int index = i;
            int stones = holes.get(i);

            // Для игрока 2, который видит перевернутую доску, нужно передавать правильный индекс хода
            boolean isPlayer1 = gameInfo.isPlayer1(currentUser.getId());
            int actualIndex;
            if (isPlayer1 && isCurrentPlayer) {
                actualIndex = i;
            } else if (!isPlayer1 && isCurrentPlayer) {
                actualIndex = holes.size() - 1 - i;
            } else if (isPlayer1 && !isCurrentPlayer) {
                actualIndex = holes.size() - 1 - i;
            } else {
                actualIndex = i;
            }

            final int moveIndex = actualIndex;

            VBox holeBox = new VBox();
            holeBox.setAlignment(Pos.CENTER);
            holeBox.setSpacing(5);
            holeBox.setPrefWidth(60);
            holeBox.setPrefHeight(80);
            holeBox.setStyle("-fx-background-color: " + (isCurrentPlayer ? "#3779C4" : "#7D2123") +
                    "; -fx-background-radius: 10;");

            if (canMove && stones > 0) {
                holeBox.setStyle(holeBox.getStyle() + " -fx-border-color: #00FF00; -fx-border-width: 3; -fx-border-radius: 10;");
                holeBox.setCursor(javafx.scene.Cursor.HAND);
                holeBox.setOnMouseClicked(e -> {
                    if (!state.isMakingMove()) {
                        gameVM.makeMove(moveIndex);
                    }
                });
            } else {
                holeBox.setCursor(javafx.scene.Cursor.DEFAULT);
            }

            ImageView stoneIcon = new ImageView(Drawable.get("stone.png"));
            stoneIcon.setFitWidth(30);
            stoneIcon.setFitHeight(30);
            stoneIcon.setOpacity(Math.min(1.0, stones / 10.0));

            Label stonesLabel = new Label(String.valueOf(stones));
            stonesLabel.setFont(Font.font("System", 16));
            stonesLabel.setTextFill(Color.WHITE);

            holeBox.getChildren().addAll(stoneIcon, stonesLabel);
            holesRow.getChildren().add(holeBox);
        }
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}