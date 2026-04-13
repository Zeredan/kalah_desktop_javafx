package com.last_battle.kalah.feature.main;

import com.last_battle.kalah.core.ui.MainMenuLeft;
import com.last_battle.kalah.core.ui.ProfileOrAuthCard;
import com.last_battle.kalah.core.ui.base.BaseFeature;
import com.last_battle.kalah.core.ui.base.NavigateMainMenuFunc;
import com.last_battle.kalah.core.ui.managers.Drawable;
import com.last_battle.kalah.feature.GameVsBotVM;
import com.last_battle.kalah.feature.ProfileVM;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainFeature implements BaseFeature {

    private final HBox root;
    private final ProfileVM profileVM;
    private final GameVsBotVM gameVsBotVM;

    private Label difficultyLabel;
    private Label pitsLabel;
    private Label stonesLabel;

    private final Runnable onPlayWithHuman;
    private final Runnable onPlayWithBot;
    private final Runnable onProfile;
    private final Runnable onRegister;
    private final Runnable onLogin;
    private final NavigateMainMenuFunc onNavigate;

    public MainFeature(
            ProfileVM profileVM,
            GameVsBotVM gameVsBotVM,
            NavigateMainMenuFunc onNavigate,
            Runnable onPlayWithHuman,
            Runnable onPlayWithBot,
            Runnable onProfile,
            Runnable onRegister,
            Runnable onLogin
    ) {
        this.profileVM = profileVM;
        this.gameVsBotVM = gameVsBotVM;
        this.onPlayWithHuman = onPlayWithHuman;
        this.onPlayWithBot = onPlayWithBot;
        this.onProfile = onProfile;
        this.onRegister = onRegister;
        this.onLogin = onLogin;
        this.onNavigate = onNavigate;

        root = new HBox();

        MainMenuLeft mainMenuLeft = new MainMenuLeft(0, onNavigate);
        VBox contentArea = createContentArea();
        root.getChildren().addAll(mainMenuLeft.getRoot(), contentArea);

        bindGameSettings();
    }

    private VBox createContentArea() {
        VBox content = new VBox();
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(17, 30, 30, 30));
        content.setSpacing(17);
        content.setStyle("-fx-background-color: #141B46;");
        HBox.setHgrow(content, Priority.ALWAYS);

        HBox topBar = createTopBar();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        VBox scrollContent = new VBox();
        scrollContent.setSpacing(17);
        scrollContent.setAlignment(Pos.TOP_CENTER);

        Button playWithHumanBtn = createGameButton("Играть с человеком", "#2C4BE5");
        playWithHumanBtn.setOnAction((e) -> {
            if (profileVM.registeredUserProperty().get() != null) {
                onPlayWithHuman.run();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Требуется авторизация");
                alert.setHeaderText(null);
                alert.setContentText("Для игры с человеком необходимо войти в аккаунт или зарегистрироваться");
                alert.showAndWait();
            }
        });
        VBox computerGameSettings = createComputerGameSettings();

        scrollContent.getChildren().addAll(playWithHumanBtn, computerGameSettings);
        scrollPane.setContent(scrollContent);

        content.getChildren().addAll(topBar, scrollPane);
        return content;
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 20, 0));
        topBar.setSpacing(20);

        Label titleLabel = new Label("Игра Калах");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Node profileCard = new ProfileOrAuthCard(
                profileVM.registeredUserProperty(),
                onProfile,
                onLogin,
                onRegister
        ).getRoot();

        topBar.getChildren().addAll(titleLabel, spacer, profileCard);
        return topBar;
    }

    private Button createGameButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(Font.font("System", FontWeight.BOLD, 20));
        button.setTextFill(Color.WHITE);
        button.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(60);

        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: " + color + ";" +
                        "-fx-background-radius: 10;" +
                        "-fx-opacity: 0.9;" +
                        "-fx-cursor: hand;")
        );
        button.setOnMouseExited(e ->
                button.setStyle("-fx-background-color: " + color + ";" +
                        "-fx-background-radius: 10;" +
                        "-fx-opacity: 1.0;" +
                        "-fx-cursor: hand;")
        );

        return button;
    }

    private VBox createComputerGameSettings() {
        VBox settingsBox = new VBox();
        settingsBox.setStyle(
                "-fx-background-color: #24306B;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;"
        );
        settingsBox.setPadding(new Insets(20));
        settingsBox.setSpacing(34);

        Button playWithComputerBtn = createGameButton("Играть с компьютером", "#2C4BE5");
        playWithComputerBtn.setOnAction(e -> onPlayWithBot.run());

        HBox difficultySettings = createDifficultySettings();
        HBox pitsSettings = createPitsSettings();
        HBox stonesSettings = createStonesSettings();

        settingsBox.getChildren().addAll(playWithComputerBtn, difficultySettings, pitsSettings, stonesSettings);
        return settingsBox;
    }

    private HBox createDifficultySettings() {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER_LEFT);
        container.setStyle("-fx-background-color: #4E399A; -fx-background-radius: 10;");
        container.setPadding(new Insets(15));
        container.setSpacing(10);

        Label titleLabel = new Label("Уровень сложности:");
        titleLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        titleLabel.setTextFill(Color.WHITE);

        HBox controls = new HBox();
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setSpacing(10);

        Button minusBtn = createControlButton("-");
        minusBtn.setOnAction(e -> gameVsBotVM.decreaseDifficulty());

        VBox difficultyBox = createDifficultyBox();
        difficultyLabel = (Label) difficultyBox.getChildren().get(1);
        difficultyLabel.textProperty().bind(gameVsBotVM.difficultyNameProperty());

        Button plusBtn = createControlButton("+");
        plusBtn.setOnAction(e -> gameVsBotVM.increaseDifficulty());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        controls.getChildren().addAll(minusBtn, difficultyBox, plusBtn, spacer);
        container.getChildren().addAll(titleLabel, controls);
        return container;
    }

    private VBox createDifficultyBox() {
        VBox box = new VBox();
        box.setPrefWidth(50);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(5);

        ImageView icon = new ImageView(Drawable.get("easy.png"));
        icon.setFitWidth(25);
        icon.setFitHeight(25);
        icon.setPreserveRatio(true);
        icon.imageProperty().bind(Bindings.createObjectBinding(() -> {
            int level = gameVsBotVM.getDifficultyLevel();
            String iconName = switch (level) {
                case 0 -> "easy.png";
                case 1 -> "medium.png";
                default -> "hard.png";
            };
            return Drawable.get(iconName);
        }, gameVsBotVM.difficultyLevelProperty()));

        Label label = new Label();
        label.setFont(Font.font("System", 12));
        label.setTextFill(Color.WHITE);

        box.getChildren().addAll(icon, label);
        return box;
    }

    private HBox createPitsSettings() {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER_LEFT);
        container.setStyle("-fx-background-color: #4E399A; -fx-background-radius: 10;");
        container.setPadding(new Insets(15));
        container.setSpacing(10);

        Label titleLabel = new Label("Лунки:");
        titleLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        titleLabel.setTextFill(Color.WHITE);

        HBox controls = new HBox();
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setSpacing(10);

        Button minusBtn = createControlButton("-");
        minusBtn.setOnAction(e -> gameVsBotVM.decreasePits());

        VBox valueBox = createValueBox("hole.png", pitsLabel = new Label());
        pitsLabel.textProperty().bind(gameVsBotVM.pitsCountProperty().asString());

        Button plusBtn = createControlButton("+");
        plusBtn.setOnAction(e -> gameVsBotVM.increasePits());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        controls.getChildren().addAll(minusBtn, valueBox, plusBtn, spacer);
        container.getChildren().addAll(titleLabel, controls);
        return container;
    }

    private HBox createStonesSettings() {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER_LEFT);
        container.setStyle("-fx-background-color: #4E399A; -fx-background-radius: 10;");
        container.setPadding(new Insets(15));
        container.setSpacing(10);

        Label titleLabel = new Label("Камни:");
        titleLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        titleLabel.setTextFill(Color.WHITE);

        HBox controls = new HBox();
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setSpacing(10);

        Button minusBtn = createControlButton("-");
        minusBtn.setOnAction(e -> gameVsBotVM.decreaseStones());

        VBox valueBox = createValueBox("stones.png", stonesLabel = new Label());
        stonesLabel.textProperty().bind(gameVsBotVM.stonesCountProperty().asString());

        Button plusBtn = createControlButton("+");
        plusBtn.setOnAction(e -> gameVsBotVM.increaseStones());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        controls.getChildren().addAll(minusBtn, valueBox, plusBtn, spacer);
        container.getChildren().addAll(titleLabel, controls);
        return container;
    }

    private VBox createValueBox(String iconName, Label valueLabel) {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(5);

        ImageView icon = new ImageView(Drawable.get(iconName));
        icon.setFitWidth(25);
        icon.setFitHeight(25);
        icon.setPreserveRatio(true);

        valueLabel.setFont(Font.font("System", 12));
        valueLabel.setTextFill(Color.WHITE);

        box.getChildren().addAll(icon, valueLabel);
        return box;
    }

    private Button createControlButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(25, 25);
        button.setFont(Font.font("System", FontWeight.BOLD, 16));
        button.setTextFill(Color.WHITE);
        button.setStyle(
                "-fx-background-color: #7360C4;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: #8A77E0; -fx-background-radius: 5; -fx-cursor: hand;")
        );
        button.setOnMouseExited(e ->
                button.setStyle("-fx-background-color: #7360C4; -fx-background-radius: 5; -fx-cursor: hand;")
        );

        return button;
    }

    private void bindGameSettings() {
        gameVsBotVM.difficultyLevelProperty().addListener((obs, old, newVal) -> {});
        gameVsBotVM.pitsCountProperty().addListener((obs, old, newVal) -> {});
        gameVsBotVM.stonesCountProperty().addListener((obs, old, newVal) -> {});
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}