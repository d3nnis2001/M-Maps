package com.gpse.basis.services;

import com.gpse.basis.domain.Settings;
import com.gpse.basis.repositories.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            return settingsRepository.save(new Settings(""));
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
}
