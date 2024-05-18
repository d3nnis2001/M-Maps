package com.gpse.basis.web;

import com.gpse.basis.domain.ChecklistTemplate;
import com.gpse.basis.services.ChecklistTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
