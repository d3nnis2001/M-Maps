package com.gpse.basis.repositories;

import com.gpse.basis.domain.Settings;
import org.springframework.data.repository.CrudRepository;

public interface SettingsRepository extends CrudRepository<Settings, String> {
}
