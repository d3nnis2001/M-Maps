package com.gpse.basis.services;

import com.gpse.basis.domain.Settings;
import com.gpse.basis.repositories.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SettingsServiceImpl implements SettingsService {
    private final SettingsRepository settingsRepository;
    @Autowired
    SettingsServiceImpl(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @Override
    public Optional<Settings> getImpressum(String id) {
        return settingsRepository.findById(id);
    }
}
