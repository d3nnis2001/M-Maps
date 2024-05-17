package com.gpse.basis.services;

import com.gpse.basis.domain.ChecklistTemplate;
import com.gpse.basis.repositories.ChecklistTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ChecklistTemplateServiceImpl implements ChecklistTemplateService {
    private final ChecklistTemplateRepository checklistTemplateRepository;
    public ChecklistTemplateServiceImpl(ChecklistTemplateRepository checklistTemplateRepository) {
        this.checklistTemplateRepository = checklistTemplateRepository;
    }
    @Override
    public List<String> getAllChecklistNames() {
        List<String> result = new LinkedList<>();
        Iterable<ChecklistTemplate> it = checklistTemplateRepository.findAll();
        for (ChecklistTemplate names : it) {
            result.add(names.getName());
        }
        System.out.println(result);
        return result;
    }
}
