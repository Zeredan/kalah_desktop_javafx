package com.last_battle.kalah.feature.lobbies;

import com.last_battle.kalah.core.domain.model.Lobby;
import com.last_battle.kalah.core.ui.MainMenuLeft;
import com.last_battle.kalah.core.ui.ProfileOrAuthCard;
import com.last_battle.kalah.core.ui.base.BaseFeature;
import com.last_battle.kalah.core.ui.base.NavigateMainMenuFunc;
import com.last_battle.kalah.core.ui.managers.Drawable;
import com.last_battle.kalah.feature.ProfileVM;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.List;

public class LobbiesFeature implements BaseFeature {

    private final HBox root;
    private final LobbiesVM viewModel;
    private final ProfileVM profileVM;
    private final NavigateMainMenuFunc onNavigate;
    private final Runnable onCreateLobby;
    private final Runnable onJoinLobby;
    private final Runnable onProfile;
    private VBox lobbiesContainer;

    public LobbiesFeature(
            LobbiesVM viewModel,
            ProfileVM profileVM,
            NavigateMainMenuFunc onNavigate,
            Runnable onProfile,
            Runnable onCreateLobby,
            Runnable onJoinLobby
    ) {
        this.viewModel = viewModel;
        this.profileVM = profileVM;
        this.onNavigate = onNavigate;
        this.onProfile = onProfile;
        this.onCreateLobby = onCreateLobby;
        this.onJoinLobby = onJoinLobby;

        root = new HBox();

        MainMenuLeft mainMenuLeft = new MainMenuLeft(-1, onNavigate);
        VBox contentArea = createContentArea();
        root.getChildren().addAll(mainMenuLeft.getRoot(), contentArea);

        bindViewModel();
        viewModel.loadLobbies();
    }

    private VBox createContentArea() {
        VBox content = new VBox();
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(17, 30, 30, 30));
        content.setSpacing(20);
        content.setStyle("-fx-background-color: #141B46;");
        HBox.setHgrow(content, Priority.ALWAYS);

        // Верхняя панель
        HBox topBar = createTopBar();

        // Контейнер для списка лобби
        VBox lobbiesListContainer = new VBox();
        lobbiesListContainer.setStyle("-fx-background-color: #24306B; -fx-background-radius: 10;");
        lobbiesListContainer.setPadding(new Insets(20));
        lobbiesListContainer.setSpacing(10);
        VBox.setVgrow(lobbiesListContainer, Priority.ALWAYS);

        // ScrollPane для прокрутки
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        lobbiesContainer = new VBox();
        lobbiesContainer.setSpacing(10);
        scrollPane.setContent(lobbiesContainer);
        lobbiesListContainer.getChildren().add(scrollPane);

        // Кнопка создания лобби
        Button createLobbyButton = createCreateLobbyButton();

        // Индикатор загрузки
        Label loadingLabel = new Label("Загрузка...");
        loadingLabel.setFont(Font.font("System", 16));
        loadingLabel.setTextFill(Color.web("#AAAAAA"));
        loadingLabel.setAlignment(Pos.CENTER);

        // Биндинг состояния загрузки
        viewModel.isLoadingProperty().addListener((obs, old, isLoading) -> {
            if (isLoading) {
                lobbiesContainer.setVisible(false);
                loadingLabel.setVisible(true);
            } else {
                lobbiesContainer.setVisible(true);
                loadingLabel.setVisible(false);
            }
        });

        viewModel.joinedLobbyProperty().addListener((obs, old, lobby) -> {
            if (lobby != null) {
                onJoinLobby.run();
            }
        });

        content.getChildren().addAll(topBar, lobbiesListContainer, createLobbyButton, loadingLabel);
        return content;
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 20, 0));
        topBar.setSpacing(16);

        Label titleLabel = new Label("Доступные лобби");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);

        // Кнопка обновления
        Button refreshButton = new Button();
        ImageView refreshIcon = new ImageView(Drawable.get("refresh.png"));
        refreshIcon.setFitWidth(28);
        refreshIcon.setFitHeight(28);
        refreshIcon.setPreserveRatio(true);
        refreshButton.setGraphic(refreshIcon);
        refreshButton.setStyle(
                "-fx-background-color: #4E399A;" +
                        "-fx-background-radius: 24;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 10;"
        );
        refreshButton.setOnAction(e -> viewModel.loadLobbies());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Node profileCard = new ProfileOrAuthCard(
                profileVM.registeredUserProperty(),
                onProfile, () -> {}, () -> {}
        ).getRoot();

        topBar.getChildren().addAll(titleLabel, refreshButton, spacer, profileCard);
        return topBar;
    }

    private Button createCreateLobbyButton() {
        Button button = new Button("+ Создать лобби");
        button.setFont(Font.font("System", FontWeight.BOLD, 20));
        button.setTextFill(Color.WHITE);
        button.setStyle(
                "-fx-background-color: #2C4BE5;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(55);
        button.setPadding(new Insets(10, 20, 10, 20));

        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: #4A6BE5; -fx-background-radius: 10; -fx-cursor: hand;")
        );
        button.setOnMouseExited(e ->
                button.setStyle("-fx-background-color: #2C4BE5; -fx-background-radius: 10; -fx-cursor: hand;")
        );

        button.setOnAction(e -> onCreateLobby.run());
        return button;
    }

    private void bindViewModel() {
        viewModel.lobbiesProperty().addListener((obs, old, lobbies) -> {
            updateLobbiesList(lobbies);
        });

        viewModel.errorProperty().addListener((obs, old, error) -> {
            if (error != null && !error.isEmpty()) {
                System.err.println("Error loading lobbies: " + error);
            }
        });
    }

    private void updateLobbiesList(List<Lobby> lobbies) {
        lobbiesContainer.getChildren().clear();

        if (lobbies == null || lobbies.isEmpty()) {
            Label emptyLabel = new Label("Нет доступных лобби\nСоздайте новое!");
            emptyLabel.setFont(Font.font("System", 16));
            emptyLabel.setTextFill(Color.web("#AAAAAA"));
            emptyLabel.setAlignment(Pos.CENTER);
            lobbiesContainer.getChildren().add(emptyLabel);
            return;
        }

        for (Lobby lobby : lobbies) {
            lobbiesContainer.getChildren().add(createLobbyCard(lobby));
        }
    }

    private HBox createLobbyCard(Lobby lobby) {
        HBox card = new HBox();
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: #34438D; -fx-background-radius: 12;");
        card.setPadding(new Insets(15, 20, 15, 20));
        card.setSpacing(20);
        card.setCursor(Cursor.HAND);
        card.setOnMouseClicked(e -> viewModel.joinLobby(lobby));

        // Название лобби с фиксированной шириной
        String lobbyName = lobby.getName();
        Label nameLabel = new Label(lobbyName);
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setPrefWidth(220);
        nameLabel.setMinWidth(220);
        nameLabel.setMaxWidth(220);

        // Обрезаем длинное название
        if (lobbyName.length() > 18) {
            String shortName = lobbyName.substring(0, 15) + "...";
            nameLabel.setText(shortName);
            Tooltip tooltip = new Tooltip(lobbyName);
            Tooltip.install(nameLabel, tooltip);
        }

        // Информация о создателе и игроках
        VBox infoBox = new VBox();
        infoBox.setSpacing(5);
        infoBox.setPrefWidth(120);

        Label creatorLabel = new Label("Создатель: " + lobby.getLeader().getUsername());
        creatorLabel.setFont(Font.font("System", 14));
        creatorLabel.setTextFill(Color.web("#CCCCCC"));

        String playersText = "Игроки: " + lobby.getCurrentPlayers() + "/2";
        Label playersLabel = new Label(playersText);
        playersLabel.setFont(Font.font("System", 14));
        playersLabel.setTextFill(Color.web("#CCCCCC"));

        infoBox.getChildren().addAll(creatorLabel, playersLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Статус (полное/неполное)
        Label statusLabel = new Label(lobby.isFull() ? "🔴 Занято" : "🟢 Свободно");
        statusLabel.setFont(Font.font("System", 14));
        statusLabel.setPrefWidth(80);
        statusLabel.setTextFill(lobby.isFull() ? Color.web("#FF6666") : Color.web("#66FF66"));

        // Стрелка вперёд
        Label arrowLabel = new Label("→");
        arrowLabel.setFont(Font.font("System", 20));
        arrowLabel.setPrefWidth(30);
        arrowLabel.setTextFill(Color.web("#A68DFF"));

        card.getChildren().addAll(nameLabel, infoBox, spacer, statusLabel, arrowLabel);
        return card;
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}