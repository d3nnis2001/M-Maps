package com.gpse.basis.web;

import com.gpse.basis.domain.Colors;
import com.gpse.basis.domain.Settings;
import com.gpse.basis.services.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * This is the Controller that manages all methods associated with the settings,
 * i.e. the impressum, the logo and the colors of the application.
 */
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
    @PostMapping("/edit/impressum")
    public String editImpressum(@RequestBody Map<String, String> body) {
        String content = body.get("content");
        return settingsService.editImpressum(content).getImpressum();
    }
    @PostMapping("/edit/colors")
    public Colors editColors(@RequestBody Colors colors) {
        return settingsService.editColors(colors);
    }
}
