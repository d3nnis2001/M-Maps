package com.gpse.basis.repositories;

import com.gpse.basis.domain.Reparatur;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * ReparaturRepository.
 */
@Repository
public interface ReparaturRepository extends CrudRepository<Reparatur, String> {
}
