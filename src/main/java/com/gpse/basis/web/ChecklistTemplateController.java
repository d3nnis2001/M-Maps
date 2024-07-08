package com.gpse.basis.web;

import com.gpse.basis.domain.Checklist;
import com.gpse.basis.services.ChecklistService;
import com.gpse.basis.web.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/templates")
public class ChecklistTemplateController {
    ChecklistService checklistTemplateService;
    @Autowired
    public ChecklistTemplateController(ChecklistService checklistTemplateService) {
        this.checklistTemplateService = checklistTemplateService;
    }
    @GetMapping("/all/names")
    public List<String> getAllChecklistNames() {
        return checklistTemplateService.getAllChecklistNames();
    }

    @GetMapping("/all")
    public List<Checklist> getAllTemplates() {
        return checklistTemplateService.getALlTemplates();
    }

    @PostMapping("/create")
    public boolean addChecklist(@RequestBody Checklist template) {
        return checklistTemplateService.addChecklist(template.getName(), template.getTasks(), template.getMaterial());
    }

    @GetMapping("/{name}")
    public Checklist getTemplate(@PathVariable("name") final String name) {
        Optional<Checklist> template = checklistTemplateService.getTemplate(name);
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

    @PostMapping("/duplicate")
    public String addDuplicate(@RequestBody Checklist template) {
        return checklistTemplateService.duplicateTemplate(template);
    }

    @PostMapping("/edit")
    public boolean editChecklist(@RequestBody Checklist templateEdit) {
        return checklistTemplateService.editChecklist(templateEdit);
    }
}
