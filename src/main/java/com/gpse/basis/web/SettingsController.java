package com.gpse.basis.web;

import com.gpse.basis.domain.Colors;
import com.gpse.basis.domain.Settings;
import com.gpse.basis.services.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @GetMapping("/colors")
    public Colors getColors() {
        return settingsService.getColors();
    }

    /**
     * This Method changes the Logo to the newly uploaded file.
     * @param newLogo file of the uploaded picture
     * @return the byte array which represents the uploaded logo
     */
    @PostMapping("/edit/logo")
    public byte[] editLogo(@RequestPart(value = "file") MultipartFile newLogo) {
        try {
            return settingsService.editLogo(newLogo);
        } catch (IOException e) {
            return new byte[0];
        }
    }
    @GetMapping("/logo")
    public byte[] getLogo() {
        return settingsService.getLogo();
    }
}
