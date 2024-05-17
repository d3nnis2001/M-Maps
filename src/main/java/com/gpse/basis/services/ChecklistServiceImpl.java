package com.gpse.basis.services;

import com.gpse.basis.domain.Checklist;
import com.gpse.basis.domain.ChecklistTemplate;
import com.gpse.basis.repositories.ChecklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;

@Service
public class ChecklistServiceImpl implements ChecklistService {
    private final ChecklistRepository checkRepo;
    @Autowired
    public ChecklistServiceImpl(ChecklistRepository checkRepo) {
        this.checkRepo = checkRepo;
    }
    public ArrayList<String> getAllNames() {
        ArrayList <String> arr = new ArrayList<>();
        Iterable it = checkRepo.findAll();
        Iterator<ChecklistTemplate> iterator = it.iterator();
        while (iterator.hasNext()) {
            ChecklistTemplate checkSolo = iterator.next();
            arr.add(checkSolo.getName());
        }
        return arr;
    }
    public Checklist loadChecklistByUsername(final String name) throws UsernameNotFoundException {
        return checkRepo.findById(name)
            .orElseThrow(() -> new UsernameNotFoundException("Checklist not found"));
    }
}

