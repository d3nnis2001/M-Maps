package com.gpse.basis.services;

import com.gpse.basis.domain.Checklist;
import com.gpse.basis.domain.ReparaturChecklist;
import com.gpse.basis.repositories.ChecklistRepository;
import com.gpse.basis.repositories.RepChecklistRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ChecklistServiceImpl implements ChecklistService {
    private final ChecklistRepository checkRepo;
    private final RepChecklistRepository repcheckRepo;
    public ChecklistServiceImpl(ChecklistRepository checkRepo, RepChecklistRepository repcheckRepo) {
        this.checkRepo = checkRepo;
        this.repcheckRepo = repcheckRepo;
    }
    @Override
    public List<String> getAllChecklistNames() {
        List<String> result = new LinkedList<>();
        checkRepo.findAll().forEach(item -> result.add(item.getName()));
        return result;
    }

    @Override
    public List<Checklist> getALlTemplates() {
        List<Checklist> result = new LinkedList<>();
        checkRepo.findAll().forEach(result::add);
        return result;
    }

    @Override
    public boolean addChecklist(String name, List<String> tasks, List<String> material) {
        if (checkRepo.existsById(name)) {
            return false;
        }
        Checklist newChecklist = new Checklist(name, tasks);
        newChecklist.setMaterial(material);
        checkRepo.save(newChecklist);
        return true;
    }

    @Override
    public Optional<Checklist> getTemplate(String name) {
        return checkRepo.findById(name);
    }

    @Override
    public void deleteTemplate(String templateName) {
        checkRepo.deleteById(templateName);
    }

    @Override
    public String duplicateTemplate(Checklist template) {
        final String kopie = "Kopie";
        final String whitespace = " ";
        String modifiedTemplateName = template.getName() + whitespace + kopie;
        while (!addChecklist(modifiedTemplateName, template.getTasks(), template.getMaterial())) {
            modifiedTemplateName += whitespace + kopie;
        }
        return modifiedTemplateName;
    }

    @Override
    public boolean editChecklist(Checklist template) {
        if (!checkRepo.existsById(template.getName())) {
            return false;
        }
        checkRepo.save(template);
        return true;
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

    /**
     * Load rep check by id reparatur checklist.
     *
     * @param id the id
     * @return the reparatur checklist
     */
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
