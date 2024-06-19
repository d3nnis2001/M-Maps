package com.gpse.basis.web;

import com.gpse.basis.domain.Settings;
import com.gpse.basis.services.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {
    private final SettingsService settingsService;
    @Autowired
    SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }
    @GetMapping("/impressum")
    public String getImpressum() {
        Settings settings = settingsService.getImpressum();
        return settings.getImpressum();
    }
}
