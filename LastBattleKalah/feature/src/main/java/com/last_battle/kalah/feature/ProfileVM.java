package com.last_battle.kalah.feature;

import com.last_battle.kalah.core.domain.model.User;
import com.last_battle.kalah.core.ui.base.ViewModel;
import com.last_battle.kalah.usecases.auth.*;
import com.last_battle.kalah.usecases.model.UserCredentials;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.concurrent.CompletableFuture;

public class ProfileVM extends ViewModel {
    private final RegisterUserUC registerUserUC;
    private final LogInUserUC logInUserUC;
    private final GetSavedCredentialsUC getSavedCredentialsUC;
    private final LogoutUserUC logoutUserUC;
    private final SaveCredentialsUC saveCredentialsUC;
    private final ObjectProperty<User> registeredUser = new SimpleObjectProperty<>(null);
    private final StringProperty error = new SimpleStringProperty();

    public ProfileVM(
            RegisterUserUC registerUserUC,
            LogInUserUC logInUserUC,
            GetSavedCredentialsUC getSavedCredentialsUC,
            LogoutUserUC logoutUserUC,
            SaveCredentialsUC saveCredentialsUC
    ) {
        this.registerUserUC = registerUserUC;
        this.logInUserUC = logInUserUC;
        this.getSavedCredentialsUC = getSavedCredentialsUC;
        this.logoutUserUC = logoutUserUC;
        this.saveCredentialsUC = saveCredentialsUC;
        autoLogin();
    }

    public ObjectProperty<User> registeredUserProperty() {
        return registeredUser;
    }


    public StringProperty errorProperty() {
        return error;
    }

    public void autoLogin() {
        CompletableFuture.runAsync(() -> {
            try {
                UserCredentials uc = getSavedCredentialsUC.invoke();
                if (uc == null) return;
                if (uc.getUsername() == null || uc.getUsername().isEmpty()) return;
                if (uc.getPassword() == null || uc.getPassword().isEmpty()) return;
                logIn(uc.getUsername(), uc.getPassword());
            } catch (Exception e) {
                Platform.runLater(() -> error.set("Auto login failed: " + e.getMessage()));
            }
        });
    }

    public void register(String username, String email, String password) {
        error.set(null);
        CompletableFuture.runAsync(() -> {
            try {
                User result = registerUserUC.invoke(username, email, password);
                Platform.runLater(() -> {
                    if (result != null) {
                        registeredUser.set(result);
                        saveCredentialsUC.invoke(result.getUsername(), result.getPassword());
                        error.set(null);
                    } else {
                        error.set("Registration failed: user already exists or invalid data");
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> error.set("Registration error: " + e.getMessage()));
            }
        });
    }

    public void logIn(String username, String password) {
        error.set(null);
        CompletableFuture.runAsync(() -> {
            try {
                User result = logInUserUC.invoke(username, password);
                Platform.runLater(() -> {
                    if (result != null) {
                        registeredUser.set(result);
                        saveCredentialsUC.invoke(result.getUsername(), result.getPassword());
                        error.set(null);
                    } else {
                        error.set("Invalid username or password");
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> error.set("Login error: " + e.getMessage()));
            }
        });
    }

    public void logout() {
        CompletableFuture.runAsync(() -> {
            logoutUserUC.invoke();
            Platform.runLater(() -> registeredUser.set(null));
        });
    }
}