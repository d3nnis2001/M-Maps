package com.gpse.basis.repositories;

import com.gpse.basis.domain.ChecklistTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistTemplateRepository extends CrudRepository<ChecklistTemplate, String> {
}
