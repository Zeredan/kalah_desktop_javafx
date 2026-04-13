package com.last_battle.kalah.usecases.auth;

import com.last_battle.kalah.domain.settings.SettingsRepository;
import com.last_battle.kalah.usecases.model.UserCredentials;

public class GetSavedCredentialsUC {
    private final SettingsRepository settingsRepository;

    public GetSavedCredentialsUC(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }
    public UserCredentials invoke() {
        return new UserCredentials(
                settingsRepository.getSavedUsername().get(),
                settingsRepository.getSavedPassword().get()
        );
    }
}
