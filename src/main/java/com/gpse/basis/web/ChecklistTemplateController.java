package com.gpse.basis.web;

import com.gpse.basis.domain.ChecklistTemplate;
import com.gpse.basis.services.ChecklistTemplateService;
import com.gpse.basis.web.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/templates")
public class ChecklistTemplateController {
    ChecklistTemplateService checklistTemplateService;
    @Autowired
    public ChecklistTemplateController(ChecklistTemplateService checklistTemplateService) {
        this.checklistTemplateService = checklistTemplateService;
    }
    @GetMapping("/all/names")
    public List<String> getAllChecklistNames() {
        return checklistTemplateService.getAllChecklistNames();
    }

    @GetMapping("/all")
    public List<ChecklistTemplate> getAllTemplates() {
        return checklistTemplateService.getALlTemplates();
    }

    @PostMapping("/create")
    public boolean addChecklist(@RequestBody ChecklistTemplate template) {
        return checklistTemplateService.addChecklist(template.getName(), template.getTasks(), template.getMaterial());
    }

    @GetMapping("/{name}")
    public ChecklistTemplate getTemplate(@PathVariable("name") final String name) {
        Optional<ChecklistTemplate> template = checklistTemplateService.getTemplate(name);
        if (template.isEmpty()) {
            throw new NotFoundException();
        }
        return template.get();
    }

    @DeleteMapping("/delete/{name}")
    public String deleteTemplate(@PathVariable("name") String templateName) {
        checklistTemplateService.deleteTemplate(templateName);
        return templateName;
    }
}
