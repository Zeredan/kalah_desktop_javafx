package com.last_battle.kalah.feature.game_vs_player;

import com.last_battle.kalah.core.domain.model.GameInfo;
import com.last_battle.kalah.core.domain.model.GameState;
import com.last_battle.kalah.core.domain.model.User;
import com.last_battle.kalah.core.ui.MainMenuLeft;
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
import javafx.scene.image.Image;
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

    // UI элементы
    private HBox topHolesRow;      // Верхние лунки (видны сверху)
    private HBox bottomHolesRow;   // Нижние лунки (видны снизу)
    private Label leftKalahLabel;  // Левый калах
    private Label rightKalahLabel; // Правый калах
    private VBox leftKalahBox;     // Контейнер левого калаха
    private VBox rightKalahBox;    // Контейнер правого калаха
    private Label turnLabel;
    private Label opponentNameLabel;
    private Label playerNameLabel;
    private Label statusLabel;
    private Button exitGameButton;

    private List<Integer> reverseList(List<Integer> list) {
        List<Integer> reversed = new java.util.ArrayList<>(list);
        java.util.Collections.reverse(reversed);
        return reversed;
    }

    private void setKalahStyle(VBox kalahBox, boolean isPlayer) {
        if (isPlayer) {
            kalahBox.setStyle("-fx-background-color: #3779C4; -fx-background-radius: 15;");
        } else {
            kalahBox.setStyle("-fx-background-color: #7D2123; -fx-background-radius: 15;");
        }
    }

    private void updateKalahIcon(VBox kalahBox, int stones) {
        ImageView stoneImageView = (ImageView) kalahBox.getChildren().get(1);
        String stoneImageName;
        if (stones <= 1) {
            stoneImageName = "stones.png";
        } else if (stones == 2) {
            stoneImageName = "stones_2.png";
        } else if (stones == 3) {
            stoneImageName = "stones_3.png";
        } else if (stones == 4) {
            stoneImageName = "stones_4.png";
        } else if (stones == 5) {
            stoneImageName = "stones_5.png";
        } else {
            stoneImageName = "stones_many.png";
        }

        stoneImageView.setImage(Drawable.get(stoneImageName));
        stoneImageView.setFitHeight(30);
        stoneImageView.setFitWidth(30);
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

        if (gameVM.gameInfoProperty().get() != null) {
            updateUI(gameVM.gameInfoProperty().get());
        }
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

        exitGameButton = new Button("Выйти в меню");
        exitGameButton.setFont(Font.font("System", 14));
        exitGameButton.setTextFill(Color.WHITE);
        exitGameButton.setStyle(
                "-fx-background-color: #703030;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 10 20;"
        );
        exitGameButton.setVisible(false); // Скрыта до окончания игры
        exitGameButton.setOnAction(e -> {
            gameVM.leaveGame();
            onGameEnd.run();
        });

        content.getChildren().addAll(topBar, gameBoard, turnLabel, statusLabel, exitGameButton, bottomBar);
        return content;
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 20, 0));
        topBar.setSpacing(16);

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
            gameVM.leaveGame();
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

        // Верхние лунки
        VBox topSection = new VBox();
        topSection.setAlignment(Pos.CENTER);
        topSection.setSpacing(8);

        Label topHolesLabel = new Label("Верхние лунки");
        topHolesLabel.setFont(Font.font("System", 12));
        topHolesLabel.setTextFill(Color.web("#FF9999"));

        topHolesRow = new HBox();
        topHolesRow.setAlignment(Pos.CENTER);
        topHolesRow.setSpacing(10);

        topSection.getChildren().addAll(topHolesLabel, topHolesRow);

        // Средняя секция: левый калах + центральная часть + правый калах
        HBox middleSection = new HBox();
        middleSection.setAlignment(Pos.CENTER);
        middleSection.setSpacing(20);

        // Левый калах
        leftKalahBox = new VBox();
        leftKalahBox.setAlignment(Pos.CENTER);
        leftKalahBox.setSpacing(10);
        leftKalahBox.setPrefWidth(85);
        leftKalahBox.setPrefHeight(250);
        leftKalahBox.setStyle("-fx-background-color: #7D2123; -fx-background-radius: 15;");

        Label leftKalahTitle = new Label("Калах");
        leftKalahTitle.setFont(Font.font("System", 14));
        leftKalahTitle.setTextFill(Color.WHITE);

        leftKalahLabel = new Label("0");
        leftKalahLabel.setFont(Font.font("System", 28));
        leftKalahLabel.setTextFill(Color.WHITE);

        ImageView leftStoneIcon = new ImageView(Drawable.get("stones.png"));
        leftStoneIcon.setFitWidth(40);
        leftStoneIcon.setFitHeight(40);

        leftKalahBox.getChildren().addAll(leftKalahTitle, leftStoneIcon, leftKalahLabel);

        // Центральная часть
        VBox centerSection = new VBox();
        centerSection.setAlignment(Pos.CENTER);
        centerSection.setSpacing(20);

        // Нижние лунки
        VBox bottomSection = new VBox();
        bottomSection.setAlignment(Pos.CENTER);
        bottomSection.setSpacing(8);

        Label bottomHolesLabel = new Label("Нижние лунки");
        bottomHolesLabel.setFont(Font.font("System", 12));
        bottomHolesLabel.setTextFill(Color.web("#99FF99"));

        bottomHolesRow = new HBox();
        bottomHolesRow.setAlignment(Pos.CENTER);
        bottomHolesRow.setSpacing(10);

        bottomSection.getChildren().addAll(bottomHolesLabel, bottomHolesRow);

        centerSection.getChildren().addAll(topSection, bottomSection);

        // Правый калах
        rightKalahBox = new VBox();
        rightKalahBox.setAlignment(Pos.CENTER);
        rightKalahBox.setSpacing(10);
        rightKalahBox.setPrefWidth(85);
        rightKalahBox.setPrefHeight(250);
        rightKalahBox.setStyle("-fx-background-color: #3779C4; -fx-background-radius: 15;");

        Label rightKalahTitle = new Label("Калах");
        rightKalahTitle.setFont(Font.font("System", 14));
        rightKalahTitle.setTextFill(Color.WHITE);

        rightKalahLabel = new Label("0");
        rightKalahLabel.setFont(Font.font("System", 28));
        rightKalahLabel.setTextFill(Color.WHITE);

        ImageView rightStoneIcon = new ImageView(Drawable.get("stones.png"));
        rightStoneIcon.setFitWidth(40);
        rightStoneIcon.setFitHeight(40);

        rightKalahBox.getChildren().addAll(rightKalahTitle, rightStoneIcon, rightKalahLabel);

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

        if (isPlayer1) {
            leftKalahLabel.setText(String.valueOf(state.getPlayer2Kalah()));
            rightKalahLabel.setText(String.valueOf(state.getPlayer1Kalah()));

            setKalahStyle(leftKalahBox, false);
            setKalahStyle(rightKalahBox, true);

            updateKalahIcon(leftKalahBox, state.getPlayer2Kalah());
            updateKalahIcon(rightKalahBox, state.getPlayer1Kalah());

            updateHolesRow(topHolesRow, state.getPlayer2Holes(), false, false, state, gameInfo);
            updateHolesRow(bottomHolesRow, state.getPlayer1Holes(), true, false, state, gameInfo);
        } else {
            leftKalahLabel.setText(String.valueOf(state.getPlayer1Kalah()));
            rightKalahLabel.setText(String.valueOf(state.getPlayer2Kalah()));

            setKalahStyle(leftKalahBox, false);
            setKalahStyle(rightKalahBox, true);

            updateKalahIcon(leftKalahBox, state.getPlayer1Kalah());
            updateKalahIcon(rightKalahBox, state.getPlayer2Kalah());


            updateHolesRow(topHolesRow, reverseList(state.getPlayer1Holes()), false, true, state, gameInfo);
            updateHolesRow(bottomHolesRow, reverseList(state.getPlayer2Holes()), true, true, state, gameInfo);
        }

        // Обновляем статус игры
        if (state.isFinished()) {
            profileVM.autoLogin();
            showGameResult(state, gameInfo, currentUser);
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

    private void showGameResult(GameState state, GameInfo gameInfo, User currentUser) {
        boolean isDraw = "DRAW".equals(state.getStatus());
        boolean isPlayer1 = gameInfo.isPlayer1(currentUser.getId());

        String resultText;
        Color resultColor;

        if (isDraw) {
            resultText = "НИЧЬЯ!";
            resultColor = Color.web("#FFD700");
            statusLabel.setText("Игра закончилась вничью!");
        } else {
            boolean player1Win = "PLAYER1_WIN".equals(state.getStatus());
            boolean playerWin = (player1Win && isPlayer1) || (!player1Win && !isPlayer1);
            String winnerName = (player1Win ? gameInfo.getPlayer1() : gameInfo.getPlayer2()).getUsername();
            String loserName = (player1Win ? gameInfo.getPlayer2() : gameInfo.getPlayer1()).getUsername();

            if (playerWin) {
                resultText = "ПОБЕДА!";
                resultColor = Color.web("#66FF66");
                statusLabel.setText("Поздравляем! Вы победили игрока " + loserName + "!");
            } else {
                resultText = "ПОРАЖЕНИЕ!";
                resultColor = Color.web("#FF6666");
                statusLabel.setText("Вы проиграли игроку " + winnerName + "!");
            }
        }

        turnLabel.setText(resultText);
        turnLabel.setTextFill(resultColor);

        exitGameButton.setVisible(true);
    }

    private void updateHolesRow(HBox holesRow, List<Integer> holes, boolean isMyHoles, boolean isReversed,
                                GameState state, GameInfo gameInfo) {
        holesRow.getChildren().clear();

        User currentUser = profileVM.registeredUserProperty().get();
        boolean isMyTurn = state.isMyTurn(currentUser.getId(),
                gameInfo.getPlayer1().getId(), gameInfo.getPlayer2().getId());
        boolean canMove = isMyTurn && isMyHoles && !state.isFinished() && !state.isMakingMove();

        for (int i = 0; i < holes.size(); i++) {
            int stones = holes.get(i);
            final int moveIndex = (!isReversed) ? i : holes.size() - 1 - i;

            VBox holeBox = new VBox();
            holeBox.setAlignment(Pos.CENTER);
            holeBox.setSpacing(5);
            holeBox.setPrefWidth(60);
            holeBox.setPrefHeight(80);
            holeBox.setStyle("-fx-background-color: " + (isMyHoles ? "#3779C4" : "#7D2123") +
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
                holeBox.setOnMouseClicked(null);
            }

            String stoneImageName;
            if (stones <= 1) {
                stoneImageName = "stones.png";
            } else if (stones == 2) {
                stoneImageName = "stones_2.png";
            } else if (stones == 3) {
                stoneImageName = "stones_3.png";
            } else if (stones == 4) {
                stoneImageName = "stones_4.png";
            } else if (stones == 5) {
                stoneImageName = "stones_5.png";
            } else {
                stoneImageName = "stones_many.png";
            }

            ImageView stoneIcon = new ImageView(Drawable.get(stoneImageName));
            stoneIcon.setFitHeight(30);
            stoneIcon.setFitWidth(30);

            VBox iconContainer = new VBox(stoneIcon);
            iconContainer.setAlignment(Pos.CENTER);
            iconContainer.setPadding(new Insets(8, 0, 4, 0));

            Label stonesLabel = new Label(String.valueOf(stones));
            stonesLabel.setFont(Font.font("System", 16));
            stonesLabel.setTextFill(Color.WHITE);

            holeBox.getChildren().addAll(iconContainer, stonesLabel);
            holesRow.getChildren().add(holeBox);
        }
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}