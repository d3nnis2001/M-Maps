package com.gpse.basis.repositories;

import com.gpse.basis.domain.GleisLageDatenpunkt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Gleis lage daten repository.
 */
@Repository
public interface GleisLageDatenRepository extends CrudRepository<GleisLageDatenpunkt, String> {
}
