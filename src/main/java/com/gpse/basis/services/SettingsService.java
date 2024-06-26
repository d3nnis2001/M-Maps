package com.gpse.basis.services;

import com.gpse.basis.domain.Settings;

public interface SettingsService {
    Settings getImpressum();
    Settings editImpressum(String content);
}
