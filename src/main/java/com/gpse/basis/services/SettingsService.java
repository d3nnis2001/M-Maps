package com.gpse.basis.services;

import com.gpse.basis.domain.Settings;

import java.util.Optional;

public interface SettingsService {
    Optional<Settings> getImpressum(String id);
}
