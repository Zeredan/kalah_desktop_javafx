package com.last_battle.kalah.usecases.auth;

import com.last_battle.kalah.domain.settings.SettingsRepository;
import com.last_battle.kalah.usecases.model.UserCredentials;

public class SaveCredentialsUC {
    private final SettingsRepository settingsRepository;

    public SaveCredentialsUC(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }
    public void invoke(String userName, String password) {
        settingsRepository.setSavedUsername(userName);
        settingsRepository.setSavedPassword(password);
    }
}
