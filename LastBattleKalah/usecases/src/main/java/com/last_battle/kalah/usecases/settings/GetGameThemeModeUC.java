package com.last_battle.kalah.usecases.settings;

import com.last_battle.kalah.core.ui.themes.GameTheme;
import com.last_battle.kalah.domain.settings.SettingsRepository;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;

public class GetGameThemeModeUC {
    private final SettingsRepository settingsRepository;

    public GetGameThemeModeUC(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }
    public ObjectProperty<GameTheme> invoke() {
        return settingsRepository.getGameTheme();
    }
}
