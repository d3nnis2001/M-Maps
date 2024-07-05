package com.gpse.basis.services;

import com.gpse.basis.domain.Checklist;
import com.gpse.basis.domain.ReparaturChecklist;
import com.gpse.basis.repositories.ChecklistRepository;
import com.gpse.basis.repositories.RepChecklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

@Service
public class ChecklistServiceImpl implements ChecklistService {
    private ChecklistRepository checkRepo;
    private RepChecklistRepository repcheckRepo;
    @Autowired
    public ChecklistServiceImpl(ChecklistRepository checkRepo, RepChecklistRepository repcheckRepo) {
        this.checkRepo = checkRepo;
        this.repcheckRepo = repcheckRepo;
    }
    public ArrayList<String> getAllNames() {
        ArrayList<String> arr = new ArrayList<>();
        Iterable it = checkRepo.findAll();
        Iterator<Checklist> iterator = it.iterator();
        while (iterator.hasNext()) {
            Checklist checkSolo = iterator.next();
            arr.add(checkSolo.getName());
        }
        return arr;
    }
    public Checklist loadChecklistByName(final String name) throws UsernameNotFoundException {
        return checkRepo.findById(name)
            .orElseThrow(() -> new UsernameNotFoundException("Checklist not found"));
    }
    public ReparaturChecklist loadRepCheckById(final String id) {
        return repcheckRepo.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("Repair Checklist not found!"));
    }
    public Boolean changeRepChecklist(final String id, String[]arr) {
        ReparaturChecklist rep1 = repcheckRepo.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("Repair Checklist not found"));
        rep1.setCheckSel(new ArrayList<>(Arrays.asList(arr)));
        repcheckRepo.save(rep1);
        return true;
    }

    public ArrayList<String> getTickedwithId(String id) {
        ReparaturChecklist repCheck = loadRepCheckById(id);
        return repCheck.getCheckSel();
    }

    public Boolean setTerminatedDate(String id, LocalDate term) {
        try {
            ReparaturChecklist r1 = loadRepCheckById(id);
            r1.setTerminiert(term);
            repcheckRepo.save(r1);
            return true;
        } catch (Exception r) {
            return false;
        }
    }

    public LocalDate getTerminationDate(String id) {
        ReparaturChecklist reparaturChecklist = loadRepCheckById(id);
        LocalDate date = reparaturChecklist.getTerminiert();
        return date;
    }
}
