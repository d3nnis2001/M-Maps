package com.gpse.basis.services;

import com.gpse.basis.domain.InspectionOrder;
import com.gpse.basis.repositories.InspectionOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
@Service
public class InspectionServicesImpl implements InspectionServices {

    private InspectionOrderRepository inspec;
    @Autowired
    public InspectionServicesImpl(InspectionOrderRepository inspec) {
        this.inspec = inspec;
    }

    @Override
    public void createInspectionOrder(InspectionOrder inspectionOrder) {
        String defaultStatus = "unbearbeitet";
        String defaultUserId = " ";
        boolean defaultArchiviert = false;
        inspectionOrder.setInspectionOrderId(generateId());
        inspec.save(inspectionOrder);
    }
    public String generateId() {
        long timestamp = System.currentTimeMillis();
        Random random = new Random();
        int randomValue = random.nextInt(1000);
        return String.valueOf(timestamp + randomValue);
    }

    @Override
    public void editInspectionOrder(InspectionOrder inspectionOrder) {


    }

    @Override
    public void acceptInspectionOrder(InspectionOrder inspectionOrder) {

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
        System.out.println("TEST-getInspecData");
        return inspecArray;
    }

    @Override
    public InspectionOrder loadInspecById(String streckenId) throws UsernameNotFoundException {
        return inspec.findById(streckenId).orElseThrow(()
            -> new UsernameNotFoundException("Inspection Id " + streckenId + " not found."));
    }
}
