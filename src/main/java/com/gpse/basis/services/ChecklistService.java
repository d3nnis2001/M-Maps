package com.gpse.basis.services;

import com.gpse.basis.domain.Checklist;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

public interface ChecklistService {
    ArrayList<String> getAllNames();
    Checklist loadChecklistByUsername(final String name) throws UsernameNotFoundException;
}
