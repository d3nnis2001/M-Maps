package com.gpse.basis.repositories;

import com.gpse.basis.domain.GleisVData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * GleisVDataRepository.
 */
@Repository
public interface GleisVDataRepository extends CrudRepository<GleisVData, String> {
}
