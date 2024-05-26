package com.gpse.basis.repositories;

import com.gpse.basis.domain.GleisLageRange;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GleisLageRangeRepository extends CrudRepository<GleisLageRange, String> {
}
