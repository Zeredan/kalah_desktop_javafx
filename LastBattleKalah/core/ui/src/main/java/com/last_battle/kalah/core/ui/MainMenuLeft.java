package com.last_battle.kalah.core.ui;

import com.last_battle.kalah.core.ui.base.BaseFeature;
import com.last_battle.kalah.core.ui.base.NavigateMainMenuFunc;
import com.last_battle.kalah.core.ui.managers.Drawable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.function.Consumer;

public class MainMenuLeft implements BaseFeature {

    private final VBox root;
    private final MenuButton[] menuButtons;

    private NavigateMainMenuFunc onNavigate;

    private static final String ACTIVE_COLOR = "#7188FF";
    private static final String INACTIVE_COLOR = "#FFFFFF";
    private static final String BACKGROUND_COLOR = "#0E1332";
    private static final int BUTTON_SIZE = 68;
    private static final int SPACING_BETWEEN = 30;
    private static final int TOP_BOTTOM_PADDING = 20;

    public MainMenuLeft(
            Integer selectedIndex,
            NavigateMainMenuFunc onNavigate
    ) {
        this.onNavigate = onNavigate;
        root = new VBox();
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(0, 20, 0, 20));

        menuButtons = new MenuButton[3];

        menuButtons[0] = new MenuButton(
                "play.png",
                "Играть",
                selectedIndex == 0,
                () -> onMenuItemClick(0)
        );

        menuButtons[1] = new MenuButton(
                "goblet.png",
                "Рейтинг",
                selectedIndex == 1,
                () -> onMenuItemClick(1)
        );

        menuButtons[2] = new MenuButton(
                "settings.png",
                "Настройки",
                selectedIndex == 2,
                () -> onMenuItemClick(2)
        );

        // Добавляем спейсеры между кнопками
        Region topSpacer = new Region();
        Region spacer1 = new Region();
        Region spacer2 = new Region();
        Region bottomSpacer = new Region();

        root.getChildren().addAll(
                topSpacer,
                menuButtons[0],
                spacer1,
                menuButtons[1],
                spacer2,
                menuButtons[2],
                bottomSpacer
        );

        // Все спейсеры растягиваются одинаково
        VBox.setVgrow(topSpacer, Priority.ALWAYS);
        VBox.setVgrow(spacer1, Priority.ALWAYS);
        VBox.setVgrow(spacer2, Priority.ALWAYS);
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

    }

    private void onMenuItemClick(int index) {
        onNavigate.navigate(index);
    }


    @Override
    public Parent getRoot() {
        return root;
    }

    private static class MenuButton extends VBox {
        private final ImageView icon;
        private final Label textLabel;

        public MenuButton(String iconName, String text, boolean isActive, Runnable onClick) {
            setAlignment(Pos.TOP_CENTER);
            setSpacing(5);

            icon = new ImageView(Drawable.get(iconName));
            icon.setFitWidth(BUTTON_SIZE);
            icon.setFitHeight(BUTTON_SIZE);
            icon.setPreserveRatio(true);

            textLabel = new Label(text);
            textLabel.setFont(Font.font("System", 16));
            if (isActive) {
                textLabel.setStyle("-fx-text-fill: " + ACTIVE_COLOR + ";");
            } else {
                textLabel.setStyle("-fx-text-fill: " + INACTIVE_COLOR + ";");
            }

            textLabel.setAlignment(Pos.CENTER);

            getChildren().addAll(icon, textLabel);


            if (!isActive) {
                setOnMouseClicked(e -> {
                    if (onClick != null) {
                        onClick.run();
                    }
                });

                setOnMouseEntered(e -> {
                    setStyle("-fx-cursor: hand; -fx-opacity: 0.8;");
                });

                setOnMouseExited(e -> {
                    setStyle("-fx-cursor: default; -fx-opacity: 1;");
                });
                setStyle("-fx-cursor: hand;");
            }
        }
    }
}