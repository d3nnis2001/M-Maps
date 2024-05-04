package com.gpse.basis.services;

import com.gpse.basis.domain.Reparatur;
import com.gpse.basis.repositories.ReperaturRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

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
}
