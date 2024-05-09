package com.gpse.basis.services;

import com.gpse.basis.domain.InspectionOrder;
import com.gpse.basis.repositories.InspectionOrderRepository;

import java.util.ArrayList;
import java.util.Iterator;

public class InspectionServicesImpl implements InspectionServices {

    private InspectionOrderRepository inspec;

    public InspectionServicesImpl(InspectionOrderRepository inspec) {
        this.inspec = inspec;
    }

    @Override
    public void createInspectionOrder(String streckenId, String userId, String start, String end, String startTime, String endTime,
                                      String fachabteilung, String messdaten, String status, String bemerkungen,
                                      boolean archiviert) {
        String defaultStatus = "unbearbeitet";
        InspectionOrder newInspec = new InspectionOrder(streckenId," ", start, end, startTime, endTime, fachabteilung,
            messdaten, defaultStatus, bemerkungen, false);
        inspec.save(newInspec);
    }

    @Override
    public void editInspectionOrder(String streckenId, String userId, String start, String end, String startTime, String endTime,
                                    String fachabteilung, String messdaten, String status, String bemerkungen,
                                    boolean archiviert) {


    }

    @Override
    public void acceptInspectionOrder(String streckenId, String userId, String status) {

    }

    @Override
    public ArrayList<InspectionOrder> getInspecData() {
        Iterable iterable = inspec.findAll();
        ArrayList<InspectionOrder> inspecArray = new ArrayList<>();
        Iterator<InspectionOrder> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            InspectionOrder inspec = iterator.next();
            inspecArray.add(inspec);
        }
        return inspecArray;
    }
}
