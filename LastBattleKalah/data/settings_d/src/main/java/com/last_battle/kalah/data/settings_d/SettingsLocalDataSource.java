package com.last_battle.kalah.data.settings_d;

import com.last_battle.kalah.core.ui.themes.GameTheme;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

public interface SettingsLocalDataSource {
    public StringProperty getSavedUsername();

    public StringProperty getSavedPassword();

    public BooleanProperty getDarkMode();

    public ObjectProperty<GameTheme> getGameTheme();

    public void setSavedUsername(String login);

    public void setSavedPassword(String password);

    public void setDarkMode(boolean darkMode);

    public void setGameTheme(GameTheme theme);
}
