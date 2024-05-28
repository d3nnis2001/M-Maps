package com.gpse.basis.services;

import com.gpse.basis.domain.Checklist;
import com.gpse.basis.domain.GeoCords;
import com.gpse.basis.domain.Reparatur;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Date;

public interface ReparaturService {
    ArrayList<Reparatur> getRepData();
    boolean addRepairOrder(int track, Date date1, Date date2, String authorized, Checklist checklist, String remarks,  GeoCords geo);
    Reparatur loadRepByName(final String repname) throws UsernameNotFoundException;
    Boolean changeStatus(String name, String newStatus);

    Boolean deleteOrder(String name);
}
