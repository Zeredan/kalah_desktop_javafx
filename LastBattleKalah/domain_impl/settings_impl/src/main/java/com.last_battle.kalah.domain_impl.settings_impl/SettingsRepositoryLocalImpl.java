package com.last_battle.kalah.domain_impl.settings_impl;

import com.last_battle.kalah.core.ui.themes.GameTheme;
import com.last_battle.kalah.data.settings_d.SettingsLocalDataSource;
import com.last_battle.kalah.domain.settings.SettingsRepository;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

public class SettingsRepositoryLocalImpl implements SettingsRepository {
    private SettingsLocalDataSource settingsLocalDataSource;

    public SettingsRepositoryLocalImpl(
            SettingsLocalDataSource settingsLocalDataSource
    ) {
        this.settingsLocalDataSource = settingsLocalDataSource;
    }

    @Override
    public StringProperty getSavedUsername() {
        return settingsLocalDataSource.getSavedUsername();
    }

    @Override
    public StringProperty getSavedPassword() {
        return settingsLocalDataSource.getSavedPassword();
    }

    @Override
    public BooleanProperty getDarkMode() {
        return settingsLocalDataSource.getDarkMode();
    }

    @Override
    public ObjectProperty<GameTheme> getGameTheme() {
        return settingsLocalDataSource.getGameTheme();
    }

    @Override
    public void setSavedUsername(String login) {
        settingsLocalDataSource.setSavedUsername(login);
    }

    @Override
    public void setSavedPassword(String password) {
        settingsLocalDataSource.setSavedPassword(password);
    }

    @Override
    public void setDarkMode(boolean darkMode) {
        settingsLocalDataSource.setDarkMode(darkMode);
    }

    @Override
    public void setGameTheme(GameTheme theme) {
        settingsLocalDataSource.setGameTheme(theme);
    }
}
