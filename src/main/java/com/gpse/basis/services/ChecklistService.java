package com.gpse.basis.services;

import com.gpse.basis.domain.Checklist;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;

public interface ChecklistService {
    ArrayList<String> getAllNames();
    Checklist loadChecklistByName(final String name) throws UsernameNotFoundException;
    Boolean changeRepChecklist(final String id, String[] ticked);
    ArrayList<String> getTickedwithId(String id);
    Boolean setTerminatedDate(String id, LocalDate term);

    LocalDate getTerminationDate(String id);
}
