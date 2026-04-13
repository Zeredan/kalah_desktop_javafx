package com.last_battle.kalah.usecases.settings;

import com.last_battle.kalah.domain.settings.SettingsRepository;
import javafx.beans.property.BooleanProperty;

public class SetDarkModeUC {
    private final SettingsRepository settingsRepository;

    public SetDarkModeUC(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }
    public void invoke(boolean darkMode) {
        settingsRepository.setDarkMode(darkMode);
    }
}
