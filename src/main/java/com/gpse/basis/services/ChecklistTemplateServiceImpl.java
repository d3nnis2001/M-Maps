package com.gpse.basis.services;

import com.gpse.basis.domain.ChecklistTemplate;
import com.gpse.basis.repositories.ChecklistTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ChecklistTemplateServiceImpl implements ChecklistTemplateService {
    private final ChecklistTemplateRepository checklistTemplateRepository;
    public ChecklistTemplateServiceImpl(ChecklistTemplateRepository checklistTemplateRepository) {
        this.checklistTemplateRepository = checklistTemplateRepository;
    }
    @Override
    public List<String> getAllChecklistNames() {
        List<String> result = new LinkedList<>();
        checklistTemplateRepository.findAll().forEach(item -> result.add(item.getName()));
        return result;
    }

    @Override
    public List<ChecklistTemplate> getALlTemplates() {
        List<ChecklistTemplate> result = new LinkedList<>();
        checklistTemplateRepository.findAll().forEach(result::add);
        return result;
    }

    @Override
    public boolean addChecklist(String name, List<String> tasks, List<String> material) {
        if (checklistTemplateRepository.existsById(name)) {
            return false;
        }
        ChecklistTemplate newChecklist = new ChecklistTemplate(name, tasks, material);
        checklistTemplateRepository.save(newChecklist);
        return true;
    }

    @Override
    public Optional<ChecklistTemplate> getTemplate(String name) {
        return checklistTemplateRepository.findById(name);
    }

    @Override
    public void deleteTemplate(String templateName) {
        checklistTemplateRepository.deleteById(templateName);
    }

    @Override
    public String duplicateTemplate(ChecklistTemplate template) {
        String modifiedTemplateName = template.getName() + " Kopie";
        while (!addChecklist(modifiedTemplateName, template.getTasks(), template.getMaterial())) {
            modifiedTemplateName += " Kopie";
        }
        return modifiedTemplateName;
    }

    @Override
    public boolean editChecklist(ChecklistTemplate oldTemplate, ChecklistTemplate newTemplate) {
        boolean success = addChecklist(newTemplate.getName(), newTemplate.getTasks(), newTemplate.getMaterial());
        if (success) {
            checklistTemplateRepository.deleteById(oldTemplate.getName());
        }
        return success;
    }
}
