package com.gpse.basis.services;

import com.gpse.basis.domain.*;
import com.gpse.basis.repositories.RepChecklistRepository;
import com.gpse.basis.repositories.ReperaturRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service

public class ReparaturServiceImpl implements ReparaturService {
    private final ReperaturRepository rep;
    private final RepChecklistRepository checkRepo;
    @Autowired
    public ReparaturServiceImpl(ReperaturRepository rep, RepChecklistRepository checkRepo) {
        this.rep = rep;
        this.checkRepo = checkRepo;
    }

    public ArrayList<Reparatur> getRepData() {
        Iterable it = rep.findAll();
        ArrayList<Reparatur> repArr = new ArrayList<>();
        Iterator<Reparatur> iterator = it.iterator();
        while (iterator.hasNext()) {
            Reparatur repSolo = iterator.next();
            repArr.add(repSolo);
        }
        return repArr;
    }
    public boolean addRepairOrder(int track, LocalDate date1,
                           LocalDate date2, String authorized, Checklist checklist, String remarks, GeoCords geo) {
        Utils util = new Utils();
        String uniqueID = util.generateID();
        ArrayList<String> selected = new ArrayList<>();
        ReparaturChecklist check = new ReparaturChecklist(uniqueID, checklist, selected);
        Reparatur newRep = new Reparatur(uniqueID, track, date1, date2, check, remarks, "beauftragt", authorized, geo);
        rep.save(newRep);
        checkRepo.save(check);
        return true;
    }

    @Override
    public Reparatur loadRepByName(final String repname) throws UsernameNotFoundException {
        return rep.findById(repname)
            .orElseThrow(() -> new UsernameNotFoundException("Reparatur name " + repname + " not found."));
    }

    public Boolean changeStatus(String name, String newStatus) {
        Reparatur repa = loadRepByName(name);
        repa.setStatus(newStatus);
        rep.save(repa);
        return true;
    }

    public Boolean deleteOrder(String name) {
        try {
            Reparatur repa = loadRepByName(name);
            rep.delete(repa);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Reparatur> getReparaturForMap() {
        Iterable it = rep.findAll();
        ArrayList<Reparatur> repArr = new ArrayList<>();
        Iterator<Reparatur> iterator = it.iterator();
        while (iterator.hasNext()) {
            Reparatur repSolo = iterator.next();
            if(Objects.equals(repSolo.getStatus(), "beauftragt")){
                repArr.add(repSolo);
            }
        }
        return repArr;
    }
}
