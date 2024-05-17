package com.gpse.basis.web;

import com.gpse.basis.services.ChecklistTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ChecklistTemplateController {
    ChecklistTemplateService checklistTemplateService;
    @Autowired
    public ChecklistTemplateController(ChecklistTemplateService checklistTemplateService) {
        this.checklistTemplateService = checklistTemplateService;
    }
    @GetMapping("/templates")
    public List<String> getAllChecklistNames() {
        return checklistTemplateService.getAllChecklistNames();
    }
}
