package com.gpse.basis.services;

import java.util.List;

public interface ChecklistTemplateService {
    List<String> getAllChecklistNames();

    boolean addChecklist(String name);
}
