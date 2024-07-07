package com.gpse.basis.repositories;

import com.gpse.basis.domain.GleisLageRange;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Gleis lage range repository.
 */
@Repository
public interface GleisLageRangeRepository extends CrudRepository<GleisLageRange, String> {
}
