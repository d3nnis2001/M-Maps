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
            return settingsRepository.save(new Settings("", new Colors("#AFFE11", "#AFFE22"), new byte[0]));
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
            return settingsRepository.save(new Settings("", new Colors("#AFFE11", "#AFFE22"), new byte[0])).getColors();
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
            return settingsRepository.save(new Settings("", new Colors("#AFFE11", "#AFFE22"), new byte[0])).getLogo();
        }
        Settings settings = items.getFirst();
        return settings.getLogo();
    }

}
