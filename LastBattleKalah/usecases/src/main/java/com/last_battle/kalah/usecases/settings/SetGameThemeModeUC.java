package com.last_battle.kalah.usecases.settings;

import com.last_battle.kalah.core.ui.themes.GameTheme;
import com.last_battle.kalah.domain.settings.SettingsRepository;
import javafx.beans.property.ObjectProperty;

public class SetGameThemeModeUC {
    private final SettingsRepository settingsRepository;

    public SetGameThemeModeUC(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }
    public void invoke(GameTheme gameTheme) {
        settingsRepository.setGameTheme(gameTheme);
    }
}
