package com.last_battle.kalah.feature.lobby;

import com.last_battle.kalah.core.domain.model.Lobby;
import com.last_battle.kalah.core.domain.model.User;
import com.last_battle.kalah.core.ui.MainMenuLeft;
import com.last_battle.kalah.core.ui.base.BaseFeature;
import com.last_battle.kalah.core.ui.base.NavigateMainMenuFunc;
import com.last_battle.kalah.core.ui.managers.Drawable;
import com.last_battle.kalah.feature.LobbyVM;
import com.last_battle.kalah.feature.ProfileVM;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LobbyFeature implements BaseFeature {

    private final HBox root;
    private final LobbyVM lobbyVM;
    private final ProfileVM profileVM;
    private final NavigateMainMenuFunc onNavigate;
    private final Runnable onLeaveLobby;
    private final Runnable onGameStart;

    private VBox playersContainer;
    private Label lobbyNameLabel;
    private Button startGameButton;
    private Button readyButton;
    private Label statusLabel;

    public LobbyFeature(
            LobbyVM lobbyVM,
            ProfileVM profileVM,
            NavigateMainMenuFunc onNavigate,
            Runnable onLeaveLobby,
            Runnable onGameStart
    ) {
        this.lobbyVM = lobbyVM;
        this.profileVM = profileVM;
        this.onNavigate = onNavigate;
        this.onLeaveLobby = onLeaveLobby;
        this.onGameStart = onGameStart;

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

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        VBox contentContainer = new VBox();
        contentContainer.setSpacing(15);
        contentContainer.setPadding(new Insets(20));
        contentContainer.setStyle("-fx-background-color: #24306B; -fx-background-radius: 15;");

        lobbyNameLabel = new Label();
        lobbyNameLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        lobbyNameLabel.setTextFill(Color.WHITE);
        lobbyNameLabel.setPadding(new Insets(0, 0, 10, 0));

        statusLabel = new Label();
        statusLabel.setFont(Font.font("System", 14));
        statusLabel.setTextFill(Color.web("#AAAAAA"));

        Label playersTitle = new Label("Игроки:");
        playersTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        playersTitle.setTextFill(Color.WHITE);
        playersTitle.setPadding(new Insets(10, 0, 5, 0));

        playersContainer = new VBox();
        playersContainer.setSpacing(10);

        startGameButton = createStartGameButton();
        readyButton = createReadyButton();

        VBox buttonsBox = new VBox();
        buttonsBox.setSpacing(10);
        buttonsBox.setPadding(new Insets(20, 0, 10, 0));
        buttonsBox.getChildren().addAll(startGameButton, readyButton);

        contentContainer.getChildren().addAll(
                lobbyNameLabel, statusLabel, playersTitle, playersContainer, buttonsBox
        );
        scrollPane.setContent(contentContainer);

        content.getChildren().addAll(topBar, scrollPane);
        return content;
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 20, 0));
        topBar.setSpacing(16);

        Label titleLabel = new Label("Лобби");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button leaveButton = new Button("Выйти из лобби");
        leaveButton.setFont(Font.font("System", 14));
        leaveButton.setTextFill(Color.WHITE);
        leaveButton.setStyle(
                "-fx-background-color: #703030;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 16;"
        );
        leaveButton.setOnAction(e -> {
            lobbyVM.leaveLobby();
            onLeaveLobby.run();
        });

        topBar.getChildren().addAll(titleLabel, spacer, leaveButton);
        return topBar;
    }

    private Button createStartGameButton() {
        Button button = new Button("Начать игру");
        button.setFont(Font.font("System", FontWeight.BOLD, 20));
        button.setTextFill(Color.WHITE);
        button.setStyle(
                "-fx-background-color: #2C4BE5;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(60);
        button.setVisible(false);
        button.setOnAction(e -> {
            lobbyVM.startGame();
        });
        return button;
    }

    private Button createReadyButton() {
        Button button = new Button("Готов");
        button.setFont(Font.font("System", FontWeight.BOLD, 20));
        button.setTextFill(Color.WHITE);
        button.setStyle(
                "-fx-background-color: #4E399A;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(60);
        button.setVisible(false);
        button.setOnAction(e -> {
            Lobby lobby = lobbyVM.currentLobbyProperty().get();
            User currentUser = profileVM.registeredUserProperty().get();
            if (lobby != null && currentUser != null) {
                boolean isReady = lobby.getReadyStatus() != null &&
                        lobby.getReadyStatus().getOrDefault(currentUser.getId(), false);
                lobbyVM.setReady(!isReady);
            }
        });
        return button;
    }

    private void bindViewModel() {
        lobbyVM.currentLobbyProperty().addListener((obs, old, lobby) -> {
            if (lobby != null) {
                updateLobbyUI(lobby);
                if (lobby.getGameId() != null) {
                    onGameStart.run();
                }
            }
        });

        lobbyVM.isConnectedProperty().addListener((obs, old, connected) -> {
            if (!connected) {
                statusLabel.setText("Соединение с лобби потеряно...");
            }
        });
    }

    private void updateLobbyUI(Lobby lobby) {
        lobbyNameLabel.setText(lobby.getName());

        User currentUser = profileVM.registeredUserProperty().get();
        boolean isLeader = currentUser != null && lobby.getLeader() != null &&
                currentUser.getId().equals(lobby.getLeader().getId());

        if (isLeader) {
            statusLabel.setText("Вы создатель лобби. Нажмите 'Начать игру', когда оба игрока готовы");
            statusLabel.setTextFill(Color.web("#A68DFF"));
        } else {
            statusLabel.setText("Ожидание начала игры...");
            statusLabel.setTextFill(Color.web("#AAAAAA"));
        }

        updatePlayersList(lobby, isLeader, currentUser);

        if (isLeader) {
            startGameButton.setVisible(true);
            readyButton.setVisible(false);
            boolean bothReady = lobby.areBothReady();
            startGameButton.setDisable(!bothReady);
            startGameButton.setTextFill(bothReady ? Color.WHITE : Color.web("#AAAAAA"));
        } else {
            startGameButton.setVisible(false);
            readyButton.setVisible(true);
            boolean isReady = lobby.getReadyStatus() != null &&
                    lobby.getReadyStatus().getOrDefault(currentUser.getId(), false);
            readyButton.setText(isReady ? "Не готов" : "Готов");
            readyButton.setStyle(isReady ?
                    "-fx-background-color: #703030; -fx-background-radius: 10; -fx-cursor: hand;" :
                    "-fx-background-color: #4E399A; -fx-background-radius: 10; -fx-cursor: hand;"
            );
        }
    }

    private void updatePlayersList(Lobby lobby, boolean isLeader, User currentUser) {
        playersContainer.getChildren().clear();

        // Лидер
        playersContainer.getChildren().add(createPlayerRow(
                lobby.getLeader(),
                true,
                currentUser != null && currentUser.getId().equals(lobby.getLeader().getId()),
                lobby
        ));

        // Гость (если есть)
        if (lobby.getGuest() != null) {
            playersContainer.getChildren().add(createPlayerRow(
                    lobby.getGuest(),
                    false,
                    currentUser != null && currentUser.getId().equals(lobby.getGuest().getId()),
                    lobby
            ));
        } else {
            // Пустой слот
            HBox emptySlot = new HBox();
            emptySlot.setAlignment(Pos.CENTER_LEFT);
            emptySlot.setStyle("-fx-background-color: #34438D; -fx-background-radius: 12; -fx-opacity: 0.5;");
            emptySlot.setPadding(new Insets(15, 20, 15, 20));
            emptySlot.setSpacing(20);

            ImageView avatarIcon = new ImageView(Drawable.get("profile.png"));
            avatarIcon.setFitWidth(48);
            avatarIcon.setFitHeight(48);
            avatarIcon.setPreserveRatio(true);
            avatarIcon.setOpacity(0.5);

            Label waitingLabel = new Label("Ожидание игрока...");
            waitingLabel.setFont(Font.font("System", 16));
            waitingLabel.setTextFill(Color.web("#888888"));

            emptySlot.getChildren().addAll(avatarIcon, waitingLabel);
            playersContainer.getChildren().add(emptySlot);
        }
    }

    private HBox createPlayerRow(User player, boolean isLeader, boolean isCurrentUser, Lobby lobby) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: #34438D; -fx-background-radius: 12;");
        row.setPadding(new Insets(15, 20, 15, 20));
        row.setSpacing(20);

        if (isCurrentUser) {
            row.setStyle("-fx-background-color: #4A3D7A; -fx-background-radius: 12;");
        }

        // Звездочка для лидера
        if (isLeader) {
            ImageView starIcon = new ImageView(Drawable.get("star.png"));
            starIcon.setFitWidth(32);
            starIcon.setFitHeight(32);
            starIcon.setPreserveRatio(true);
            row.getChildren().add(starIcon);
        } else {
            Region spacer = new Region();
            spacer.setPrefWidth(32);
            row.getChildren().add(spacer);
        }

        // Карточка игрока
        VBox playerCard = new VBox();
        playerCard.setAlignment(Pos.CENTER);
        playerCard.setSpacing(5);
        playerCard.setMinWidth(100);

        ImageView avatarIcon = new ImageView(Drawable.get("profile.png"));
        avatarIcon.setFitWidth(48);
        avatarIcon.setFitHeight(48);
        avatarIcon.setPreserveRatio(true);

        Label usernameLabel = new Label(player.getUsername());
        usernameLabel.setFont(Font.font("System", 14));
        usernameLabel.setTextFill(Color.WHITE);

        playerCard.getChildren().addAll(avatarIcon, usernameLabel);

        // Статистика
        VBox statsBox = new VBox();
        statsBox.setAlignment(Pos.CENTER_LEFT);
        statsBox.setSpacing(5);

        Label humanWinsLabel = new Label("Побед с человеком: " + player.getWinsVsPlayer());
        humanWinsLabel.setFont(Font.font("System", 12));
        humanWinsLabel.setTextFill(Color.web("#F0F0F0"));

        Label botWinsLabel = new Label("Побед с компьютером: " + player.getWinsVsBot());
        botWinsLabel.setFont(Font.font("System", 12));
        botWinsLabel.setTextFill(Color.web("#F0F0F0"));

        statsBox.getChildren().addAll(humanWinsLabel, botWinsLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Статус готовности
        Label readyLabel = new Label();
        readyLabel.setFont(Font.font("System", 14));

        if (!isLeader) {
            boolean isReady = lobby.getReadyStatus() != null &&
                    lobby.getReadyStatus().getOrDefault(player.getId(), false);
            readyLabel.setText(isReady ? "✅ Готов" : "⏳ Не готов");
            readyLabel.setTextFill(isReady ? Color.web("#66FF66") : Color.web("#FF6666"));
        } else {
            readyLabel.setText("👑 Создатель");
            readyLabel.setTextFill(Color.web("#FFD700"));
        }

        // Метка (Вы)
        Label youLabel = new Label();
        if (isCurrentUser) {
            youLabel.setText("(Вы)");
            youLabel.setFont(Font.font("System", 12));
            youLabel.setTextFill(Color.web("#A68DFF"));
        }

        HBox rightBox = new HBox();
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.setSpacing(15);
        rightBox.getChildren().addAll(readyLabel, youLabel);

        row.getChildren().addAll(playerCard, statsBox, spacer, rightBox);
        return row;
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}