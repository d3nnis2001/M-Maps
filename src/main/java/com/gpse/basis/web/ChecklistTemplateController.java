package com.gpse.basis.web;

import com.gpse.basis.domain.Checklist;
import com.gpse.basis.services.ChecklistService;
import com.gpse.basis.web.errors.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Lädt Template Namen",
        description = "Funktion, um die Namen der erstellten Templates zu laden.")
    @GetMapping("/all/names")
    public List<String> getAllChecklistNames() {
        return checklistTemplateService.getAllChecklistNames();
    }

    @Operation(summary = "Lädt alle Templates", description = "Funktion, um alle erstellten Templates zu laden.")
    @GetMapping("/all")
    public List<Checklist> getAllTemplates() {
        return checklistTemplateService.getALlTemplates();
    }

    @Operation(summary = "Erstellt Template", description = "Funktion, um eine neue Template zu erstellen.")
    @PostMapping("/create")
    public boolean addChecklist(@RequestBody Checklist template) {
        return checklistTemplateService.addChecklist(template.getName(), template.getTasks(), template.getMaterial());
    }
    /**
     * This is the method to get a checklist template by a certain name.
     * @param name name of the template
     * @return Checklist template
     */
    @Operation(summary = "Lädt Template", description = "Funktion, um eine bestimmte Template per Namen zu laden.")
    @GetMapping("/{name}")
    public Checklist getTemplate(@PathVariable("name") final String name) {
        Optional<Checklist> template = checklistTemplateService.getTemplate(name);
        if (template.isEmpty()) {
            throw new NotFoundException();
        }
        return template.get();
    }

    @Operation(summary = "Löscht Template", description = "Funktion, um eine bestimmte Template per Namen zu löschen.")
    @DeleteMapping("/delete/{name}")
    public String deleteTemplate(@PathVariable("name") String templateName) {
        checklistTemplateService.deleteTemplate(templateName);
        return templateName;
    }
    @Operation(summary = "Dupliziert Template",
        description = "Funktion, um eine bestimmte Template per Namen zu duplizieren.")
    @PostMapping("/duplicate")
    public String addDuplicate(@RequestBody Checklist template) {
        return checklistTemplateService.duplicateTemplate(template);
    }
    @Operation(summary = "Bearbeitet Template",
        description = "Funktion, um den Inhalt einer Template per Namen zu ändern.")
    @PostMapping("/edit")
    public boolean editChecklist(@RequestBody Checklist templateEdit) {
        return checklistTemplateService.editChecklist(templateEdit);
    }
}
