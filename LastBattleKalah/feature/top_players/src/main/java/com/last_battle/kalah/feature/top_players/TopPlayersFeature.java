package com.last_battle.kalah.feature.top_players;

import com.last_battle.kalah.core.domain.model.User;
import com.last_battle.kalah.core.ui.MainMenuLeft;
import com.last_battle.kalah.core.ui.ProfileOrAuthCard;
import com.last_battle.kalah.core.ui.base.BaseFeature;
import com.last_battle.kalah.core.ui.base.NavigateMainMenuFunc;
import com.last_battle.kalah.core.ui.managers.Drawable;
import com.last_battle.kalah.feature.ProfileVM;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TopPlayersFeature implements BaseFeature {

    private final HBox root;
    private final ProfileVM profileVM;
    private final TopPlayersVM topPlayersVM;
    private VBox playersContainer;
    private final Runnable onProfile;
    private final Runnable onRegister;
    private final Runnable onLogin;
    private final NavigateMainMenuFunc onNavigate;

    public TopPlayersFeature(
            ProfileVM profileVM,
            TopPlayersVM topPlayersVM,
            NavigateMainMenuFunc onNavigate,
            Runnable onProfile,
            Runnable onRegister,
            Runnable onLogin
    ) {
        this.profileVM = profileVM;
        this.topPlayersVM = topPlayersVM;
        this.onNavigate = onNavigate;
        this.onProfile = onProfile;
        this.onRegister = onRegister;
        this.onLogin = onLogin;

        root = new HBox();

        MainMenuLeft mainMenuLeft = new MainMenuLeft(1, onNavigate);
        VBox contentArea = createContentArea();
        root.getChildren().addAll(mainMenuLeft.getRoot(), contentArea);

        bindPlayers();
    }

    private VBox createContentArea() {
        VBox content = new VBox();
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(17, 30, 30, 30));
        content.setSpacing(17);
        content.setStyle("-fx-background-color: #141B46;");
        HBox.setHgrow(content, Priority.ALWAYS);

        HBox topBar = createTopBar();

        VBox playersListContainer = new VBox();
        playersListContainer.setStyle("-fx-background-color: #24306B; -fx-background-radius: 10;");
        playersListContainer.setPadding(new Insets(20));
        playersListContainer.setSpacing(10);
        VBox.setVgrow(playersListContainer, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        this.playersContainer = new VBox();
        this.playersContainer.setSpacing(10);
        scrollPane.setContent(this.playersContainer);

        playersListContainer.getChildren().add(scrollPane);
        content.getChildren().addAll(topBar, playersListContainer);
        return content;
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 20, 0));
        topBar.setSpacing(16);

        Label titleLabel = new Label("Лучшие игроки");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);

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
        refreshButton.setOnAction(e -> topPlayersVM.loadTopPlayers());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Node profileCard = new ProfileOrAuthCard(
                profileVM.registeredUserProperty(),
                onProfile,
                onLogin,
                onRegister
        ).getRoot();

        topBar.getChildren().addAll(titleLabel, refreshButton, spacer, profileCard);
        return topBar;
    }

    private void bindPlayers() {
        topPlayersVM.topPlayersProperty().addListener((obs, oldPlayers, newPlayers) -> {
            playersContainer.getChildren().clear();
            if (newPlayers == null || newPlayers.isEmpty()) {
                Label emptyLabel = new Label("Нет данных об игроках");
                emptyLabel.setFont(Font.font("System", 16));
                emptyLabel.setTextFill(Color.web("#AAAAAA"));
                emptyLabel.setAlignment(Pos.CENTER);
                playersContainer.getChildren().add(emptyLabel);
                return;
            }

            for (int i = 0; i < newPlayers.size(); i++) {
                User player = newPlayers.get(i);
                playersContainer.getChildren().add(createPlayerRow(i + 1, player));
            }
        });
        topPlayersVM.loadTopPlayers();
    }

    private HBox createPlayerRow(int index, User player) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: #34438D; -fx-background-radius: 8;");
        row.setPadding(new Insets(15, 20, 15, 20));
        row.setSpacing(20);

        Label indexLabel = new Label(String.valueOf(index));
        indexLabel.setFont(Font.font("System", FontWeight.BOLD, 36));
        indexLabel.setTextFill(Color.WHITE);
        indexLabel.setMinWidth(50);
        indexLabel.setAlignment(Pos.CENTER);

        VBox playerCard = createPlayerCard(player);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox statsBox = createStatsBox(player);

        row.getChildren().addAll(indexLabel, playerCard, spacer, statsBox);
        return row;
    }

    private VBox createPlayerCard(User player) {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setSpacing(5);
        card.setMinWidth(100);

        ImageView avatarIcon = new ImageView(Drawable.get("profile.png"));
        avatarIcon.setFitWidth(48);
        avatarIcon.setFitHeight(48);
        avatarIcon.setPreserveRatio(true);

        Label usernameLabel = new Label(player.getUsername());
        usernameLabel.setFont(Font.font("System", 14));
        usernameLabel.setTextFill(Color.WHITE);

        card.getChildren().addAll(avatarIcon, usernameLabel);
        return card;
    }

    private VBox createStatsBox(User player) {
        VBox statsBox = new VBox();
        statsBox.setAlignment(Pos.CENTER_RIGHT);
        statsBox.setSpacing(5);

        Label humanWinsLabel = new Label("Побед в играх с человеком: " + player.getWinsVsPlayer());
        humanWinsLabel.setFont(Font.font("System", 12));
        humanWinsLabel.setTextFill(Color.web("#F0F0F0"));

        Label botWinsLabel = new Label("Побед в играх с компьютером: " + player.getWinsVsBot());
        botWinsLabel.setFont(Font.font("System", 12));
        botWinsLabel.setTextFill(Color.web("#F0F0F0"));

        statsBox.getChildren().addAll(humanWinsLabel, botWinsLabel);
        return statsBox;
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}