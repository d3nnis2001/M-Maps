package com.gpse.basis.services;

import com.gpse.basis.domain.ChecklistTemplate;

import java.util.List;
import java.util.Optional;

public interface ChecklistTemplateService {
    List<String> getAllChecklistNames();
    List<ChecklistTemplate> getALlTemplates();

    boolean addChecklist(String name, List<String> tasks, List<String> material);
    Optional<ChecklistTemplate> getTemplate(String name);

    void deleteTemplate(ChecklistTemplate template);
}
