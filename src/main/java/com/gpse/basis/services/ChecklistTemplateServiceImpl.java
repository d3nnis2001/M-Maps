package com.gpse.basis.services;

import com.gpse.basis.domain.ChecklistTemplate;
import com.gpse.basis.repositories.ChecklistTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    @Override
    public boolean addChecklist(String name) {
        //TODO: add tasks and material to method signature
        LinkedList<String> tasks = new LinkedList<>(Arrays.asList("Punkt 1", "Punkt 2", "Punkt 3"));
        LinkedList<String> material = new LinkedList<>(Arrays.asList("Material 1", "Material 2", "Material 3"));
        ChecklistTemplate newChecklist = new ChecklistTemplate(name, tasks, material);
        checklistTemplateRepository.save(newChecklist);
        return true;
    }
}
