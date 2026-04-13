package com.last_battle.kalah.feature.lobby_creation;

import com.last_battle.kalah.core.ui.MainMenuLeft;
import com.last_battle.kalah.core.ui.ProfileOrAuthCard;
import com.last_battle.kalah.core.ui.base.BaseFeature;
import com.last_battle.kalah.core.ui.base.NavigateMainMenuFunc;
import com.last_battle.kalah.core.ui.managers.Drawable;
import com.last_battle.kalah.feature.ProfileVM;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LobbyCreationFeature implements BaseFeature {

    private final HBox root;
    private final LobbyCreationVM viewModel;
    private final ProfileVM profileVM;
    private final NavigateMainMenuFunc onNavigate;
    private final Runnable onLobbyCreated;
    private final Runnable onProfile;

    private Label errorLabel;

    public LobbyCreationFeature(
            LobbyCreationVM viewModel,
            ProfileVM profileVM,
            NavigateMainMenuFunc onNavigate,
            Runnable onProfile,
            Runnable onLobbyCreated
    ) {
        this.viewModel = viewModel;
        this.profileVM = profileVM;
        this.onNavigate = onNavigate;
        this.onProfile = onProfile;
        this.onLobbyCreated = onLobbyCreated;

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

        VBox formContainer = new VBox();
        formContainer.setSpacing(25);
        formContainer.setPadding(new Insets(20, 0, 20, 0));

        // Поле название
        VBox nameRow = createInputRow("Название лобби", "Введите название");
        TextField nameField = (TextField) nameRow.getChildren().get(1);
        nameField.textProperty().bindBidirectional(viewModel.nameProperty());

        // Настройка лунок
        VBox holesRow = createHolesSettings();
        // Настройка камней
        VBox stonesRow = createStonesSettings();

        // Ошибка
        errorLabel = new Label();
        errorLabel.setFont(Font.font("System", 14));
        errorLabel.setTextFill(Color.RED);
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(Double.MAX_VALUE);
        errorLabel.setAlignment(Pos.CENTER);
        errorLabel.setVisible(false);

        // Кнопка создания
        Button createButton = createCreateButton();

        formContainer.getChildren().addAll(nameRow, holesRow, stonesRow, errorLabel, createButton);
        scrollPane.setContent(formContainer);

        content.getChildren().addAll(topBar, scrollPane);
        return content;
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 20, 0));
        topBar.setSpacing(16);

        Label titleLabel = new Label("Создание лобби");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Node profileCard = new ProfileOrAuthCard(
                profileVM.registeredUserProperty(),
                onProfile, () -> {}, () -> {}
        ).getRoot();

        topBar.getChildren().addAll(titleLabel, spacer, profileCard);
        return topBar;
    }

    private VBox createInputRow(String labelText, String placeholder) {
        VBox row = new VBox();
        row.setSpacing(8);

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

        row.getChildren().addAll(label, textField);
        return row;
    }

    private VBox createHolesSettings() {
        VBox container = new VBox();
        container.setSpacing(10);

        Label titleLabel = new Label("Количество лунок");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.WHITE);

        HBox controls = new HBox();
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setSpacing(15);

        Button minusBtn = createControlButton("-");
        minusBtn.setOnAction(e -> viewModel.decreaseHoles());

        VBox valueBox = createValueBox("hole.png", viewModel.holesProperty());

        Button plusBtn = createControlButton("+");
        plusBtn.setOnAction(e -> viewModel.increaseHoles());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        controls.getChildren().addAll(minusBtn, valueBox, plusBtn, spacer);
        container.getChildren().addAll(titleLabel, controls);
        return container;
    }

    private VBox createStonesSettings() {
        VBox container = new VBox();
        container.setSpacing(10);

        Label titleLabel = new Label("Количество камней в лунке");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.WHITE);

        HBox controls = new HBox();
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setSpacing(15);

        Button minusBtn = createControlButton("-");
        minusBtn.setOnAction(e -> viewModel.decreaseStones());

        VBox valueBox = createValueBox("stones.png", viewModel.stonesProperty());

        Button plusBtn = createControlButton("+");
        plusBtn.setOnAction(e -> viewModel.increaseStones());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        controls.getChildren().addAll(minusBtn, valueBox, plusBtn, spacer);
        container.getChildren().addAll(titleLabel, controls);
        return container;
    }

    private VBox createValueBox(String iconName, IntegerProperty valueProperty) {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(5);

        ImageView icon = new ImageView(Drawable.get(iconName));
        icon.setFitWidth(32);
        icon.setFitHeight(32);
        icon.setPreserveRatio(true);

        Label valueLabel = new Label();
        valueLabel.textProperty().bind(valueProperty.asString());
        valueLabel.setFont(Font.font("System", 18));
        valueLabel.setTextFill(Color.WHITE);

        box.getChildren().addAll(icon, valueLabel);
        return box;
    }

    private Button createControlButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(40, 40);
        button.setFont(Font.font("System", FontWeight.BOLD, 20));
        button.setTextFill(Color.WHITE);
        button.setStyle(
                "-fx-background-color: #7360C4;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
        );
        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: #8A77E0; -fx-background-radius: 8; -fx-cursor: hand;")
        );
        button.setOnMouseExited(e ->
                button.setStyle("-fx-background-color: #7360C4; -fx-background-radius: 8; -fx-cursor: hand;")
        );
        return button;
    }

    private Button createCreateButton() {
        Button button = new Button("Создать лобби");
        button.setFont(Font.font("System", FontWeight.BOLD, 20));
        button.setTextFill(Color.WHITE);
        button.setStyle(
                "-fx-background-color: #2C4BE5;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(60);
        button.setOnAction(e -> viewModel.createLobby());
        return button;
    }

    private void bindViewModel() {
        viewModel.isLoadingProperty().addListener((obs, old, isLoading) -> {
            // можно показать индикатор загрузки, но для простоты пропустим
        });

        viewModel.errorProperty().addListener((obs, old, err) -> {
            if (err != null && !err.isEmpty()) {
                errorLabel.setText(err);
                errorLabel.setVisible(true);
            } else {
                errorLabel.setVisible(false);
            }
        });

        viewModel.createdLobbyProperty().addListener((obs, old, lobby) -> {
            if (lobby != null) {
                onLobbyCreated.run();
            }
        });
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}