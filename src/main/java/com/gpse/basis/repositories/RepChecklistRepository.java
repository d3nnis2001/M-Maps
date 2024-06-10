package com.gpse.basis.repositories;

import com.gpse.basis.domain.ReparaturChecklist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepChecklistRepository  extends CrudRepository<ReparaturChecklist, String> {
}
