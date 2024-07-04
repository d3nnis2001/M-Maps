package com.gpse.basis.services;

import com.gpse.basis.domain.Colors;
import com.gpse.basis.domain.Settings;

/**
 * This Service manages all the logic concerning the settings of the application.
 */
public interface SettingsService {
    Settings getImpressum();
    Settings editImpressum(String content);
    Colors editColors(Colors colors);
}
