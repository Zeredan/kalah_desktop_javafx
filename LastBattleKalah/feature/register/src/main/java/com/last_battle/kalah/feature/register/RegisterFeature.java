package com.last_battle.kalah.feature.register;

import com.last_battle.kalah.core.ui.MainMenuLeft;
import com.last_battle.kalah.core.ui.base.BaseFeature;
import com.last_battle.kalah.core.ui.base.NavigateMainMenuFunc;
import com.last_battle.kalah.feature.ProfileVM;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RegisterFeature implements BaseFeature {

    private final HBox root;
    private final ProfileVM profileVM;
    private final NavigateMainMenuFunc onNavigate;
    private final Runnable onBackToMain;

    public RegisterFeature(
            ProfileVM profileVM,
            NavigateMainMenuFunc onNavigate,
            Runnable onBackToMain
    ) {
        this.profileVM = profileVM;
        this.onNavigate = onNavigate;
        this.onBackToMain = onBackToMain;

        root = new HBox();

        MainMenuLeft mainMenuLeft = new MainMenuLeft(-1, onNavigate);
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

        Label titleLabel = new Label("Регистрация");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setPadding(new Insets(0, 0, 30, 0));

        // Используем BorderPane для прижатия кнопки к низу
        BorderPane borderPane = new BorderPane();
        VBox.setVgrow(borderPane, Priority.ALWAYS);

        // Верхняя часть с формой (скроллится)
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox formContainer = new VBox();
        formContainer.setSpacing(20);
        formContainer.setAlignment(Pos.TOP_CENTER);

        TextField emailField = createInputRow("E-mail", "Введите ваш email");
        TextField usernameField = createInputRow("Никнейм", "Введите никнейм");
        TextField passwordField = createInputRow("Пароль", "Введите пароль");

        Label errorLabel = new Label();
        errorLabel.setFont(Font.font("System", 14));
        errorLabel.setTextFill(Color.RED);
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(Double.MAX_VALUE);
        errorLabel.setAlignment(Pos.CENTER);
        errorLabel.setVisible(false);

        formContainer.getChildren().addAll(
                emailField, usernameField, passwordField, errorLabel
        );
        scrollPane.setContent(formContainer);

        // Нижняя часть с кнопкой
        VBox bottomBox = new VBox();
        bottomBox.setPadding(new Insets(20, 0, 0, 0));

        Button registerButton = createRegisterButton();
        registerButton.setOnAction(e -> {
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            profileVM.register(username, email, password);
        });

        bottomBox.getChildren().add(registerButton);
        VBox.setVgrow(registerButton, Priority.NEVER);

        borderPane.setCenter(scrollPane);
        borderPane.setBottom(bottomBox);

        content.getChildren().addAll(titleLabel, borderPane);

        // Подписка на ошибки
        profileVM.errorProperty().addListener((obs, oldErr, newErr) -> {
            if (newErr != null && !newErr.isEmpty()) {
                errorLabel.setText(newErr);
                errorLabel.setVisible(true);
            } else {
                errorLabel.setVisible(false);
            }
        });

        // Подписка на успешный вход (после регистрации)
        profileVM.registeredUserProperty().addListener((obs, oldUser, newUser) -> {
            if (newUser != null && oldUser == null) {
                onBackToMain.run();
            }
        });

        return content;
    }

    private TextField createInputRow(String labelText, String placeholder) {
        VBox row = new VBox();
        row.setSpacing(8);
        VBox.setVgrow(row, Priority.NEVER);

        Label label = new Label(labelText);
        label.setFont(Font.font("System", 24));
        label.setTextFill(Color.WHITE);

        TextField textField = new TextField();
        textField.setPromptText(placeholder);
        textField.setStyle(
                "-fx-background-color: #34438D;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #888888;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 12 15 12 15;"
        );
        textField.setFont(Font.font("System", 16));
        textField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(textField, Priority.ALWAYS);

        row.getChildren().addAll(label, textField);
        return textField;
    }

    private Button createRegisterButton() {
        Button button = new Button("Зарегистрироваться");
        button.setFont(Font.font("System", FontWeight.BOLD, 20));
        button.setTextFill(Color.WHITE);
        button.setStyle(
                "-fx-background-color: #2C4BE5;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(60);
        button.setPadding(new Insets(10, 20, 10, 20));

        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: #4A6BE5; -fx-background-radius: 10; -fx-cursor: hand;")
        );
        button.setOnMouseExited(e ->
                button.setStyle("-fx-background-color: #2C4BE5; -fx-background-radius: 10; -fx-cursor: hand;")
        );

        return button;
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}