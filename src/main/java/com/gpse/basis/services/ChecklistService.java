package com.gpse.basis.services;

import com.gpse.basis.domain.Checklist;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ChecklistService {
    List<String> getAllChecklistNames();
    List<Checklist> getALlTemplates();

    boolean addChecklist(String name, List<String> tasks, List<String> material);
    Optional<Checklist> getTemplate(String name);

    void deleteTemplate(String templateName);
    String duplicateTemplate(Checklist template);
    boolean editChecklist(Checklist template);
    ArrayList<String> getAllNames();
    Checklist loadChecklistByName(final String name) throws UsernameNotFoundException;
    Boolean changeRepChecklist(final String id, String[] ticked);
    ArrayList<String> getTickedwithId(String id);
    Boolean setTerminatedDate(String id, LocalDate term);

    LocalDate getTerminationDate(String id);
}
