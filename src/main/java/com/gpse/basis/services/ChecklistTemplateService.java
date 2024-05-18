package com.gpse.basis.services;

import com.gpse.basis.domain.ChecklistTemplate;

import java.util.List;

public interface ChecklistTemplateService {
    List<String> getAllChecklistNames();
    List<ChecklistTemplate> getALlTemplates();

    boolean addChecklist(String name);
}
