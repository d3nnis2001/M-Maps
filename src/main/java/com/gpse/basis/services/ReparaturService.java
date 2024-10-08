package com.gpse.basis.services;

import com.gpse.basis.domain.Checklist;
import com.gpse.basis.domain.GeoCords;
import com.gpse.basis.domain.Reparatur;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ReparaturService.
 */
public interface ReparaturService {
    ArrayList<Reparatur> getRepData();

    boolean addRepairOrder(int track, LocalDate date1, LocalDate date2, String authorized, Checklist checklist,
                           String remarks, GeoCords geo);

    Reparatur loadRepByName(final String repname) throws UsernameNotFoundException;

    Boolean changeStatus(String name, String newStatus);

    Boolean deleteOrder(String name);

    List<Reparatur> getArchivedRep();

    void unarchiveRep(String id);

    void deleteArchivedRep(String id);

    List<Reparatur> getReparaturForMap();

    List<Reparatur> getReparaturForPoint(Double latitude, Double longitude);
}
