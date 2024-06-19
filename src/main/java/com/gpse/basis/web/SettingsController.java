package com.gpse.basis.web;

import com.gpse.basis.domain.Settings;
import com.gpse.basis.services.SettingsService;
import com.gpse.basis.web.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {
    private final SettingsService settingsService;
    @Autowired
    SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }
    @GetMapping("/impressum")
    public String getImpressum(String id) {
        Optional<Settings> settings = settingsService.getImpressum(id);
        if (settings.isEmpty()) {
            throw new NotFoundException();
        }
        return settings.get().getImpressum();
    }
}
