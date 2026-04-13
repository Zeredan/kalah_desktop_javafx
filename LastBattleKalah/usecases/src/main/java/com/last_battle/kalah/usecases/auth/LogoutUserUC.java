package com.last_battle.kalah.usecases.auth;

import com.last_battle.kalah.domain.settings.SettingsRepository;

public class LogoutUserUC {
    private final SettingsRepository settingsRepository;
    public LogoutUserUC(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public void invoke() {
        settingsRepository.setSavedUsername("");
        settingsRepository.setSavedPassword("");
    }
}
