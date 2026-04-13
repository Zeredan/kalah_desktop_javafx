package com.last_battle.kalah.feature;

import com.last_battle.kalah.core.domain.model.User;
import com.last_battle.kalah.core.ui.base.ViewModel;
import com.last_battle.kalah.core.ui.themes.GameTheme;
import com.last_battle.kalah.usecases.auth.GetSavedCredentialsUC;
import com.last_battle.kalah.usecases.auth.LogInUserUC;
import com.last_battle.kalah.usecases.auth.LogoutUserUC;
import com.last_battle.kalah.usecases.auth.RegisterUserUC;
import com.last_battle.kalah.usecases.model.UserCredentials;
import com.last_battle.kalah.usecases.settings.GetDarkModeUC;
import com.last_battle.kalah.usecases.settings.GetGameThemeModeUC;
import com.last_battle.kalah.usecases.settings.SetDarkModeUC;
import com.last_battle.kalah.usecases.settings.SetGameThemeModeUC;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.concurrent.CompletableFuture;


public class SettingsVM extends ViewModel {
    private final GetDarkModeUC getDarkModeUC;
    private final SetDarkModeUC setDarkModeUC;
    private final GetGameThemeModeUC getGameThemeModeUC;
    private final SetGameThemeModeUC setGameThemeModeUC;
    private final ObjectProperty<User> registeredUser = new SimpleObjectProperty<>(null);

    public SettingsVM(
            GetDarkModeUC getDarkModeUC,
            SetDarkModeUC setDarkModeUC,
            GetGameThemeModeUC getGameThemeModeUC,
            SetGameThemeModeUC setGameThemeModeUC
    ) {
        this.getDarkModeUC = getDarkModeUC;
        this.setDarkModeUC = setDarkModeUC;
        this.getGameThemeModeUC = getGameThemeModeUC;
        this.setGameThemeModeUC = setGameThemeModeUC;
    }

    public BooleanProperty getDarkMode() {
        return getDarkModeUC.invoke();
    }

    public ObjectProperty<GameTheme> getGameTheme() {
        return  getGameThemeModeUC.invoke();
    }

    public void SetDarkMode(boolean darkMode) {
        setDarkModeUC.invoke(darkMode);
    }

    public void SetGameTheme(GameTheme gameTheme) {
        setGameThemeModeUC.invoke(gameTheme);
    }
}
