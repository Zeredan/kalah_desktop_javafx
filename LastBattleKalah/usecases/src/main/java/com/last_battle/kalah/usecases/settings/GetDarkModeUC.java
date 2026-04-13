package com.last_battle.kalah.usecases.settings;

import com.last_battle.kalah.domain.settings.SettingsRepository;
import javafx.beans.property.BooleanProperty;

public class GetDarkModeUC {
    private final SettingsRepository settingsRepository;

    public GetDarkModeUC(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }
    public BooleanProperty invoke() {
        return settingsRepository.getDarkMode();
    }
}
