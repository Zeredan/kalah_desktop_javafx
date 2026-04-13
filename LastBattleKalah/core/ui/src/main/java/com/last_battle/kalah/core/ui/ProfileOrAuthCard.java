package com.last_battle.kalah.core.ui;

import com.last_battle.kalah.core.domain.model.User;
import com.last_battle.kalah.core.ui.base.BaseFeature;
import com.last_battle.kalah.core.ui.managers.Drawable;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ProfileOrAuthCard implements BaseFeature {
    private final Parent parentNode;

    public ProfileOrAuthCard(
            ObjectProperty<User> registeredUserProperty,
            Runnable onProfile,
            Runnable onLogin,
            Runnable onRegister
    ) {
        VBox profileCard = new VBox();
        profileCard.setAlignment(Pos.CENTER);
        profileCard.setPadding(new Insets(8, 16, 8, 16));

        VBox profileUserCard = new VBox();
        profileUserCard.setAlignment(Pos.CENTER);
        profileUserCard.setCursor(Cursor.HAND);
        profileUserCard.setStyle("-fx-background-color: transparent;");
        profileUserCard.setOnMouseClicked(e -> onProfile.run());

        ImageView avatarIcon = new ImageView(Drawable.get("profile.png"));
        avatarIcon.setFitWidth(48);
        avatarIcon.setFitHeight(48);
        avatarIcon.setPreserveRatio(true);

        Label userNameLabel = new Label();
        userNameLabel.setFont(Font.font("System", 14));
        userNameLabel.setTextFill(Color.web("#AAAAAA"));

        profileUserCard.getChildren().addAll(avatarIcon, userNameLabel);

        HBox guestRow = new HBox();
        guestRow.setAlignment(Pos.CENTER);
        guestRow.setSpacing(5);

        Label loginLabel = new Label("Войти");
        loginLabel.setFont(Font.font("System", 12));
        loginLabel.setTextFill(Color.web("#AAAAAA"));
        loginLabel.setOnMouseClicked(e -> onLogin.run());
        loginLabel.setCursor(Cursor.HAND);
        loginLabel.setUnderline(true);

        Label registerLabel = new Label("Регистрация");
        registerLabel.setFont(Font.font("System", 12));
        registerLabel.setTextFill(Color.web("#AAAAAA"));
        registerLabel.setOnMouseClicked(e -> onRegister.run());
        registerLabel.setCursor(Cursor.HAND);
        registerLabel.setUnderline(true);

        Label slashLabel = new Label("/");
        slashLabel.setFont(Font.font("System", 12));
        slashLabel.setTextFill(Color.web("#AAAAAA"));

        guestRow.getChildren().addAll(loginLabel, slashLabel, registerLabel);

        registeredUserProperty.addListener((obs, oldUser, newUser) -> {
            profileCard.getChildren().clear();
            if (newUser != null) {
                userNameLabel.setText(newUser.getUsername());
                profileCard.getChildren().add(profileUserCard);
            } else {
                profileCard.getChildren().add(guestRow);
            }
        });

        if (registeredUserProperty.get() != null) {
            userNameLabel.setText(registeredUserProperty.get().getUsername());
            profileCard.getChildren().add(profileUserCard);
        } else {
            profileCard.getChildren().add(guestRow);
        }
        parentNode = profileCard;
    }

    @Override
    public Parent getRoot() {
        return parentNode;
    }
}
