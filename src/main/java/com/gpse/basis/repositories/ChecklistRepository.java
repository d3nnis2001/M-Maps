package com.gpse.basis.repositories;

import com.gpse.basis.domain.Checklist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * ChecklistRepository.
 */
@Repository
public interface ChecklistRepository extends CrudRepository<Checklist, String> {
}
