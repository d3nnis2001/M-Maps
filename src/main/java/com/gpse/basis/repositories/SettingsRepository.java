package com.gpse.basis.repositories;

import com.gpse.basis.domain.Settings;
import org.springframework.data.repository.CrudRepository;

/**
 * This is the Repository that stores the settings of the application in the database.
 */
public interface SettingsRepository extends CrudRepository<Settings, String> {
}
