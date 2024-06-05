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
    public void createInspectionOrder(ArrayList<String> inspecArray) {
        String defaultStatus = "beauftragt";
        String defaultUserId = " ";
        String inspectionOrderId = generateId();
        boolean defaultArchived = false;

        InspectionOrder inspectionOrder = new InspectionOrder(inspectionOrderId, inspecArray.get(0), defaultUserId,
            inspecArray.get(1), inspecArray.get(2), inspecArray.get(3), inspecArray.get(4),
            inspecArray.get(5), inspecArray.get(6), defaultStatus, inspecArray.get(7), defaultArchived);
        inspec.save(inspectionOrder);
    }

    public String generateId() {
        long timestamp = System.currentTimeMillis();
        Random random = new Random();
        int randomValue = random.nextInt(1000);
        return String.valueOf(timestamp + randomValue);
    }

    @Override
    public void editInspectionOrder(InspectionOrder inspecNew) {
        InspectionOrder inspecOld = loadInspecById(inspecNew.getInspectionOrderId());
        inspecOld.setCourseId(inspecNew.getCourseId());
        inspecOld.setStartLocation(inspecNew.getStartLocation());
        inspecOld.setEndLocation(inspecNew.getEndLocation());
        inspecOld.setStartTime(inspecNew.getStartTime());
        inspecOld.setEndTime(inspecNew.getEndTime());
        inspecOld.setDepartment(inspecNew.getDepartment());
        inspecOld.setInspectionData(inspecNew.getInspectionData());
        inspecOld.setRemarks(inspecNew.getRemarks());
        inspec.save(inspecOld);
    }

    @Override
    public void acceptInspectionOrder(InspectionOrder inspectionOrder) {

    }

    @Override
    public Boolean deleteInspectionOrder(String inspectionOrderId) {
        try {
            System.out.println("TEST: Impl Datei");
            InspectionOrder inspectionOrder = loadInspecById(inspectionOrderId);
            System.out.println("TEST: " + inspectionOrder.getInspectionOrderId());
            inspec.delete(inspectionOrder);
            return true;
        } catch (Exception e) {
            return false;
        }

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

    @Override
    public InspectionOrder loadInspecById(String inspectionOrderId) throws UsernameNotFoundException {
        return inspec.findById(inspectionOrderId).orElseThrow(()
            -> new UsernameNotFoundException("Inspection Order Id " + inspectionOrderId + " not found."));
    }
}
