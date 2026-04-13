package com.last_battle.kalah.feature.profile;

import com.last_battle.kalah.core.domain.model.User;
import com.last_battle.kalah.core.ui.MainMenuLeft;
import com.last_battle.kalah.core.ui.base.BaseFeature;
import com.last_battle.kalah.core.ui.base.NavigateMainMenuFunc;
import com.last_battle.kalah.core.ui.managers.Drawable;
import com.last_battle.kalah.feature.ProfileVM;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ProfileFeature implements BaseFeature {

    private final HBox root;
    private final ProfileVM profileVM;
    private final NavigateMainMenuFunc onNavigate;
    private final Runnable onBackToMain;

    public ProfileFeature(
            ProfileVM profileVM,
            NavigateMainMenuFunc onNavigate,
            Runnable onBackToMain
    ) {
        this.profileVM = profileVM;
        this.onNavigate = onNavigate;
        this.onBackToMain = onBackToMain;

        root = new HBox();

        MainMenuLeft mainMenuLeft = new MainMenuLeft(-1, onNavigate); // null - ни одна кнопка не активна
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

        // Верхняя панель (только заголовок, без карточки профиля)
        HBox topBar = createTopBar();

        // Контент с прокруткой
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        VBox profileContent = createProfileContent();
        scrollPane.setContent(profileContent);

        content.getChildren().addAll(topBar, scrollPane);
        return content;
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 20, 0));
        topBar.setSpacing(20);

        Label titleLabel = new Label("Профиль");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);

        // Нет карточки профиля справа

        topBar.getChildren().add(titleLabel);
        return topBar;
    }

    private VBox createProfileContent() {
        VBox content = new VBox();
        content.setAlignment(Pos.TOP_CENTER);
        content.setSpacing(15);
        content.setPadding(new Insets(20, 0, 20, 0));

        // Аватар
        ImageView avatarIcon = new ImageView(Drawable.get("profile_big.png"));
        avatarIcon.setFitWidth(135);
        avatarIcon.setFitHeight(135);
        avatarIcon.setPreserveRatio(true);

        // Username
        Label usernameLabel = new Label();
        usernameLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        usernameLabel.setTextFill(Color.WHITE);

        // Статистика (заголовок)
        Label statsTitleLabel = new Label("Статистика:");
        statsTitleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        statsTitleLabel.setTextFill(Color.WHITE);
        statsTitleLabel.setPadding(new Insets(20, 0, 5, 0));

        // Статистика - строки с отступом
        VBox statsBox = createStatsBox();

        // Личные данные (заголовок)
        Label personalTitleLabel = new Label("Личные данные:");
        personalTitleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        personalTitleLabel.setTextFill(Color.WHITE);
        personalTitleLabel.setPadding(new Insets(20, 0, 5, 0));

        // Личные данные - строки с отступом
        VBox personalBox = createPersonalBox();

        // Кнопка выхода
        Button logoutButton = createLogoutButton();

        content.getChildren().addAll(
                avatarIcon,
                usernameLabel,
                statsTitleLabel,
                statsBox,
                personalTitleLabel,
                personalBox,
                logoutButton
        );

        // Обновляем данные при изменении пользователя
        profileVM.registeredUserProperty().addListener((obs, oldUser, newUser) -> {
            if (newUser == null) {
                onBackToMain.run();
            } else {
                updateUserData(newUser, usernameLabel, statsBox, personalBox);
            }
        });

        // Устанавливаем начальные данные
        updateUserData(profileVM.registeredUserProperty().get(), usernameLabel, statsBox, personalBox);

        return content;
    }

    private VBox createStatsBox() {
        VBox statsBox = new VBox();
        statsBox.setSpacing(8);
        statsBox.setPadding(new Insets(0, 0, 0, 30)); // отступ слева как табуляция

        Label humanWinsLabel = new Label();
        humanWinsLabel.setFont(Font.font("System", 16));
        humanWinsLabel.setTextFill(Color.web("#CCCCCC"));

        Label botWinsLabel = new Label();
        botWinsLabel.setFont(Font.font("System", 16));
        botWinsLabel.setTextFill(Color.web("#CCCCCC"));

        statsBox.getChildren().addAll(humanWinsLabel, botWinsLabel);
        return statsBox;
    }

    private VBox createPersonalBox() {
        VBox personalBox = new VBox();
        personalBox.setSpacing(8);
        personalBox.setPadding(new Insets(0, 0, 0, 30)); // отступ слева как табуляция

        Label emailLabel = new Label();
        emailLabel.setFont(Font.font("System", 16));
        emailLabel.setTextFill(Color.web("#FFFFFF"));

        Label phoneLabel = new Label();
        phoneLabel.setFont(Font.font("System", 16));
        phoneLabel.setTextFill(Color.web("#FFFFFF"));

        personalBox.getChildren().addAll(emailLabel, phoneLabel);
        return personalBox;
    }

    private Button createLogoutButton() {
        Button logoutButton = new Button("Выйти из профиля");
        logoutButton.setFont(Font.font("System", FontWeight.BOLD, 20));
        logoutButton.setTextFill(Color.WHITE);
        logoutButton.setStyle(
                "-fx-background-color: #703030;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );
        logoutButton.setMaxWidth(400);
        logoutButton.setPrefHeight(60);
        logoutButton.setPadding(new Insets(10, 30, 10, 30));

        logoutButton.setOnMouseEntered(e ->
                logoutButton.setStyle("-fx-background-color: #904040; -fx-background-radius: 10; -fx-cursor: hand;")
        );
        logoutButton.setOnMouseExited(e ->
                logoutButton.setStyle("-fx-background-color: #703030; -fx-background-radius: 10; -fx-cursor: hand;")
        );

        logoutButton.setOnAction(e -> profileVM.logout());

        // Центрируем кнопку
        VBox.setMargin(logoutButton, new Insets(30, 0, 20, 0));
        logoutButton.setAlignment(Pos.CENTER);

        return logoutButton;
    }

    private void updateUserData(User user, Label usernameLabel, VBox statsBox, VBox personalBox) {
        if (user != null) {
            usernameLabel.setText(user.getUsername());

            // Обновляем статистику
            if (statsBox.getChildren().size() >= 2) {
                Label humanWinsLabel = (Label) statsBox.getChildren().get(0);
                Label botWinsLabel = (Label) statsBox.getChildren().get(1);
                humanWinsLabel.setText("Побед в играх с человеком: " + user.getWinsVsPlayer());
                botWinsLabel.setText("Побед в играх с компьютером: " + user.getWinsVsBot());
            }

            // Обновляем личные данные
            if (personalBox.getChildren().size() >= 2) {
                Label emailLabel = (Label) personalBox.getChildren().get(0);
                Label phoneLabel = (Label) personalBox.getChildren().get(1);
                emailLabel.setText("E-mail: " + (user.getEmail() != null ? user.getEmail() : "???"));
                phoneLabel.setText("Пароль: " + (user.getPassword() != null ? user.getPassword() : "???"));
            }
        } else {
            usernameLabel.setText("Гость");
            if (statsBox.getChildren().size() >= 2) {
                Label humanWinsLabel = (Label) statsBox.getChildren().get(0);
                Label botWinsLabel = (Label) statsBox.getChildren().get(1);
                humanWinsLabel.setText("Побед в играх с человеком: 0");
                botWinsLabel.setText("Побед в играх с компьютером: 0");
            }
            if (personalBox.getChildren().size() >= 2) {
                Label emailLabel = (Label) personalBox.getChildren().get(0);
                Label phoneLabel = (Label) personalBox.getChildren().get(1);
                emailLabel.setText("E-mail: не указан");
                phoneLabel.setText("Телефон: не указан");
            }
        }
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}