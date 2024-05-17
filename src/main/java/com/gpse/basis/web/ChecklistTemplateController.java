package com.gpse.basis.web;

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
    @GetMapping("/all")
    public List<String> getAllChecklistNames() {
        return checklistTemplateService.getAllChecklistNames();
    }

    @PostMapping("/create/{name}")
    public boolean addChecklist(@PathVariable("name") final String name) {
        return checklistTemplateService.addChecklist(name);
    }
}
