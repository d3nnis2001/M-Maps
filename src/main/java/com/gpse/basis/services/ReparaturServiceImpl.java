package com.gpse.basis.services;

import com.gpse.basis.domain.Checklist;
import com.gpse.basis.domain.Reparatur;
import com.gpse.basis.domain.UserModel;
import com.gpse.basis.repositories.ReperaturRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

@Service

public class ReparaturServiceImpl implements ReparaturService {
    private ReperaturRepository rep;
    @Autowired
    public ReparaturServiceImpl(ReperaturRepository rep) {
        this.rep = rep;
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
    public boolean addUser(int track, Date date1,
                           Date date2, String authorized, Checklist checklist, String remarks) {
        String uniqueID = generateID();
        if (rep.findById(uniqueID) == null) {
            uniqueID = generateID();
        }
        System.out.println(uniqueID);
        Reparatur newRep = new Reparatur(uniqueID, track, date1, date2, checklist, remarks, "beauftragt", authorized);
        System.out.println(newRep);
        rep.save(newRep);
        return true;
    }

    public String generateID() {
        long timestamp = System.currentTimeMillis();
        Random random = new Random();
        int randomValue = random.nextInt(1000);
        return String.valueOf(timestamp) + String.valueOf(randomValue);
    }
    @Override
    public Reparatur loadRepByName(final String repname) throws UsernameNotFoundException {
        return rep.findById(repname)
            .orElseThrow(() -> new UsernameNotFoundException("Reparatur name " + repname + " not found."));
    }
}
