package com.gpse.basis.services;

import com.gpse.basis.domain.Checklist;
import com.gpse.basis.domain.Reparatur;

import java.util.ArrayList;
import java.util.Date;

public interface ReparaturService {
    ArrayList<Reparatur> getRepData();
    boolean addUser(int track, Date date1, Date date2, String authorized, Checklist checklist, String remarks);
}
