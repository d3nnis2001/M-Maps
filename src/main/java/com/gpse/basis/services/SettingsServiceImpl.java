package com.gpse.basis.services;

import com.gpse.basis.domain.Settings;
import com.gpse.basis.repositories.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

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
            return new Settings("");
        }
        return items.getFirst();
    }
}
