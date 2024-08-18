package com.gpse.basis.repositories;

import com.gpse.basis.domain.Vorlage;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository für die Vorlagen.
 */
public interface VorlagenRepository extends CrudRepository<Vorlage, String> {
}
