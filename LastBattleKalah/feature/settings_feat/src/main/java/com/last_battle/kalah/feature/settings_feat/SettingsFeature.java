package com.last_battle.kalah.feature.settings_feat;

import com.last_battle.kalah.core.ui.MainMenuLeft;
import com.last_battle.kalah.core.ui.ProfileOrAuthCard;
import com.last_battle.kalah.core.ui.base.BaseFeature;
import com.last_battle.kalah.core.ui.base.NavigateMainMenuFunc;
import com.last_battle.kalah.core.ui.managers.Drawable;
import com.last_battle.kalah.core.ui.themes.GameTheme;
import com.last_battle.kalah.feature.ProfileVM;
import com.last_battle.kalah.feature.SettingsVM;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SettingsFeature implements BaseFeature {

    private final HBox root;
    private final ProfileVM profileVM;
    private final SettingsVM settingsVM;
    private final Runnable onProfile;
    private final Runnable onRegister;
    private final Runnable onLogin;
    private final Runnable onAboutSystem;
    private final Runnable onAboutDeveloper;
    private final NavigateMainMenuFunc onNavigate;

    public SettingsFeature(
            ProfileVM profileVM,
            SettingsVM settingsVM,
            NavigateMainMenuFunc onNavigate,
            Runnable onProfile,
            Runnable onRegister,
            Runnable onLogin,
            Runnable onAboutSystem,
            Runnable onAboutDeveloper
    ) {
        this.profileVM = profileVM;
        this.settingsVM = settingsVM;
        this.onNavigate = onNavigate;
        this.onProfile = onProfile;
        this.onRegister = onRegister;
        this.onLogin = onLogin;
        this.onAboutSystem = onAboutSystem;
        this.onAboutDeveloper = onAboutDeveloper;

        root = new HBox();

        MainMenuLeft mainMenuLeft = new MainMenuLeft(2, onNavigate);
        VBox contentArea = createContentArea();
        root.getChildren().addAll(mainMenuLeft.getRoot(), contentArea);
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

        VBox scrollContent = new VBox();
        scrollContent.setSpacing(15);

        HBox darkModeRow = createDarkModeRow();
        HBox gameThemeRow = createGameThemeRow();
        HBox aboutSystemRow = createAboutRow("О системе", onAboutSystem);
        HBox aboutDevRow = createAboutRow("О разработчике", onAboutDeveloper);

        scrollContent.getChildren().addAll(darkModeRow, gameThemeRow, aboutSystemRow, aboutDevRow);
        scrollPane.setContent(scrollContent);

        content.getChildren().addAll(topBar, scrollPane);
        return content;
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 20, 0));
        topBar.setSpacing(20);

        Label titleLabel = new Label("Настройки");
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

    private HBox createDarkModeRow() {
        HBox row = createBaseSettingRow();
        Label titleLabel = new Label("Темная тема");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.WHITE);

        StackPane switchControl = createCustomSwitch();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        row.getChildren().addAll(titleLabel, spacer, switchControl);
        return row;
    }

    private StackPane createCustomSwitch() {
        StackPane switchPane = new StackPane();
        switchPane.setPrefSize(80, 40);
        switchPane.setMaxSize(80, 40);
        switchPane.setStyle("-fx-background-color: #212267; -fx-background-radius: 20; -fx-cursor: hand;");

        Circle thumb = new Circle(16);
        thumb.setFill(Color.web("#6A6CE5"));
        thumb.setTranslateX(-20);

        switchPane.getChildren().add(thumb);

        BooleanProperty darkMode = settingsVM.getDarkMode();
        darkMode.addListener((obs, oldVal, newVal) -> {
            double targetX = newVal ? 20 : -20;
            thumb.setTranslateX(targetX);
        });
        thumb.setTranslateX(darkMode.get() ? 20 : -20);

        switchPane.setOnMouseClicked(e -> {
            boolean newValue = !darkMode.get();
            settingsVM.SetDarkMode(newValue);
        });

        return switchPane;
    }

    private HBox createGameThemeRow() {
        HBox row = createBaseSettingRow();
        Label titleLabel = new Label("Тема игры");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label themeLabel = new Label();
        themeLabel.setFont(Font.font("System", 20));
        themeLabel.setTextFill(Color.web("#A68DFF"));
        themeLabel.setCursor(Cursor.HAND);

        themeLabel.textProperty().bind(Bindings.createStringBinding(
                () -> settingsVM.getGameTheme().get().name(),
                settingsVM.getGameTheme()
        ));

        themeLabel.setOnMouseClicked(e -> {
            GameTheme current = settingsVM.getGameTheme().get();
            GameTheme next = switch (current) {
                case Sand -> GameTheme.River;
                case River -> GameTheme.Ship;
                default -> GameTheme.Sand;
            };
            settingsVM.SetGameTheme(next);
        });

        row.getChildren().addAll(titleLabel, spacer, themeLabel);
        return row;
    }

    private HBox createAboutRow(String text, Runnable onClick) {
        HBox row = createBaseSettingRow();
        row.setCursor(Cursor.HAND);
        row.setOnMouseClicked(e -> onClick.run());

        Label titleLabel = new Label(text);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.WHITE);

        Label arrowLabel = new Label("→");
        arrowLabel.setFont(Font.font("System", 24));
        arrowLabel.setTextFill(Color.web("#A68DFF"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        row.getChildren().addAll(titleLabel, spacer, arrowLabel);
        return row;
    }

    private HBox createBaseSettingRow() {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: #34438D; -fx-background-radius: 12;");
        row.setPadding(new Insets(20, 25, 20, 25));
        row.setSpacing(20);
        row.setPrefHeight(90);
        HBox.setHgrow(row, Priority.ALWAYS);
        return row;
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}