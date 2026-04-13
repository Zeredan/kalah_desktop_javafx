package com.last_battle.kalah.data_impl.settings_d_impl;

import com.last_battle.kalah.core.ui.themes.GameTheme;
import com.last_battle.kalah.data.settings_d.SettingsLocalDataSource;
import javafx.beans.property.*;

import java.util.prefs.Preferences;

public class SettingsLocalDataSourcePrefImpl implements SettingsLocalDataSource {

    private static final String PREF_SAVED_USERNAME = "saved_username";
    private static final String PREF_SAVED_PASSWORD = "saved_password";
    private static final String PREF_DARK_MODE = "dark_mode";
    private static final String PREF_GAME_THEME = "game_theme";

    private final Preferences preferences;


    private final StringProperty savedUsername = new SimpleStringProperty("");
    private final StringProperty savedPassword = new SimpleStringProperty("");
    private final BooleanProperty darkMode = new SimpleBooleanProperty(true);
    private final ObjectProperty<GameTheme> gameTheme = new SimpleObjectProperty<>(GameTheme.Sand);

    public SettingsLocalDataSourcePrefImpl(
            Preferences preferences
    ) {
        this.preferences = preferences;
        loadFromPreferences();
        setupListeners();
    }


    private void loadFromPreferences() {
        String username = preferences.get(PREF_SAVED_USERNAME, "");
        savedUsername.set(username);

        String password = preferences.get(PREF_SAVED_PASSWORD, "");
        savedPassword.set(password);

        boolean isDarkMode = preferences.getBoolean(PREF_DARK_MODE, true);
        darkMode.set(isDarkMode);

        String theme = preferences.get(PREF_GAME_THEME, GameTheme.Sand.name());
        gameTheme.set(GameTheme.valueOf(theme));
    }


    private void setupListeners() {
        savedUsername.addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                preferences.put(PREF_SAVED_USERNAME, newVal);
            } else {
                preferences.remove(PREF_SAVED_USERNAME);
            }
        });

        savedPassword.addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                preferences.put(PREF_SAVED_PASSWORD, newVal);
            } else {
                preferences.remove(PREF_SAVED_PASSWORD);
            }
        });

        darkMode.addListener((obs, oldVal, newVal) -> {
            preferences.putBoolean(PREF_DARK_MODE, newVal);
        });

        gameTheme.addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                preferences.put(PREF_GAME_THEME, newVal.name());
            } else {
                preferences.put(PREF_GAME_THEME, GameTheme.Sand.name());
            }
        });
    }

    @Override
    public StringProperty getSavedUsername() {
        return savedUsername;
    }

    @Override
    public StringProperty getSavedPassword() {
        return savedPassword;
    }

    @Override
    public BooleanProperty getDarkMode() {
        return darkMode;
    }

    @Override
    public ObjectProperty<GameTheme> getGameTheme() {
        return gameTheme;
    }

    @Override
    public void setSavedUsername(String username) {
        savedUsername.set(username != null ? username : "");
    }

    @Override
    public void setSavedPassword(String password) {
        savedPassword.set(password != null ? password : "");
    }

    @Override
    public void setDarkMode(boolean darkMode) {
        this.darkMode.set(darkMode);
    }

    @Override
    public void setGameTheme(GameTheme theme) {
        gameTheme.set(theme != null ? theme : GameTheme.Sand);
    }
}