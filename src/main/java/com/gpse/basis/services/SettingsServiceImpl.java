package com.gpse.basis.services;

import com.gpse.basis.domain.Colors;
import com.gpse.basis.domain.Settings;
import com.gpse.basis.repositories.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * This is the implementation of the Service that manages all the logic concerning the settings of the application.
 */
@Service
public class SettingsServiceImpl implements SettingsService {
    private static final String PRIMARY_AND_DARK_COLOR  = "#282D37";
    private final SettingsRepository settingsRepository;
    @Autowired
    SettingsServiceImpl(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @Override
    public Settings getImpressum() {
        List<Settings> items = new LinkedList<>();
        settingsRepository.findAll().forEach(items::add);
        if (items.size() == 0) {
            return settingsRepository.save(initializeSettings());
        }
        return items.getFirst();
    }

    @Override
    public Settings editImpressum(String content) {
        List<Settings> items = new LinkedList<>();
        settingsRepository.findAll().forEach(items::add);
        Settings settings = items.getFirst();
        settings.setImpressum(content);
        return settingsRepository.save(settings);
    }

    @Override
    public Colors editColors(Colors colors) {
        List<Settings> items = new LinkedList<>();
        settingsRepository.findAll().forEach(items::add);
        Settings settings = items.getFirst();
        settings.setColors(colors);
        return settingsRepository.save(settings).getColors();
    }
    @Override
    public Colors getColors() {
        List<Settings> items = new LinkedList<>();
        settingsRepository.findAll().forEach(items::add);
        if (items.size() == 0) {
            return settingsRepository.save(initializeSettings()).getColors();
        }
        Settings settings = items.getFirst();
        return settings.getColors();
    }

    @Override
    public byte[] editLogo(MultipartFile newLogo) throws IOException {
        List<Settings> items = new LinkedList<>();
        settingsRepository.findAll().forEach(items::add);
        Settings settings = items.getFirst();
        settings.setLogo(newLogo.getBytes());
        return settingsRepository.save(settings).getLogo();
    }

    @Override
    public byte[] getLogo() {
        List<Settings> items = new LinkedList<>();
        settingsRepository.findAll().forEach(items::add);
        if (items.size() == 0) {
            return settingsRepository.save(initializeSettings()).getLogo();
        }
        Settings settings = items.getFirst();
        return settings.getLogo();
    }

    @Override
    public Settings initializeSettings() {
        return new Settings("", new Colors(PRIMARY_AND_DARK_COLOR, "#ec0016", "#1e7f5e",
            "#e21437", "#fec705", PRIMARY_AND_DARK_COLOR, "#31CCEC"), new byte[0]);
    }

}
