package com.gpse.basis.services;

import com.gpse.basis.domain.InspectionOrder;
import com.gpse.basis.repositories.InspectionOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Implementation des InspectionServices.
 */
@Service
public class InspectionServicesImpl implements InspectionServices {

    private InspectionOrderRepository inspec;

    private MongoTemplate template;
    private String inspectionOrderId = "inspectionOrderId";
    @Autowired
    public InspectionServicesImpl(InspectionOrderRepository inspec, MongoTemplate template) {
        this.inspec = inspec;
        this.template = template;
    }

    @Override
    public void createInspectionOrder(ArrayList<String> inspecArray) {
        String defaultStatus = "beauftragt";
        String defaultUserId = " ";
        String inspectionOrderId = generateId();
        boolean defaultArchived = false;

        InspectionOrder inspectionOrder = new InspectionOrder(inspectionOrderId, inspecArray.get(0), defaultUserId,
            inspecArray.get(1), inspecArray.get(2), inspecArray.get(3), inspecArray.get(4), inspecArray.get(5),
            inspecArray.get(6), defaultStatus, inspecArray.get(7), defaultArchived, inspecArray.get(8));
        inspec.save(inspectionOrder);
    }

    /**
     * ID generieren.
     * @return - generierte ID
     */
    public String generateId() {
        long timestamp = System.currentTimeMillis();
        Random random = new Random();
        int randomValue = random.nextInt(1000);
        return String.valueOf("p-" + timestamp + randomValue);
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
        inspecOld.setPriority(inspecNew.getPriority());
        inspec.save(inspecOld);
    }

    @Override
    public void acceptInspectionOrder(InspectionOrder inspectionOrder) {

    }

    @Override
    public Boolean deleteInspectionOrder(String inspectionOrderId) {
        try {
            InspectionOrder inspectionOrder = loadInspecById(inspectionOrderId);
            inspec.delete(inspectionOrder);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void editStatus(String inspectionOrderId, String status) {
        InspectionOrder inspectionOrder = loadInspecById(inspectionOrderId);
        if (status.equals("archiviert")) {
            inspectionOrder.setArchived(true);
        }
        inspectionOrder.setStatus(status);
        inspec.save(inspectionOrder);
    }

    @Override
    public void editReview(String inspectionOrderId, String review, String date) {
        InspectionOrder inspectionOrder = loadInspecById(inspectionOrderId);
        inspectionOrder.setReview(review);
        inspectionOrder.setFinishedDate(date);
        inspec.save(inspectionOrder);
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

    @Override
    public List<InspectionOrder> getArchivedInspectionOrders() {
        Query query = new Query();
        query.addCriteria(Criteria.where("archived").is(true));
        return template.find(query, InspectionOrder.class);
    }

    @Override
    public void unarchiveInspectionOrder(String id) {
        var insp = template.findById(id, InspectionOrder.class);
        if (insp != null) {
            insp.setArchived(false);
            Query qu = new Query();
            qu.addCriteria(Criteria.where(inspectionOrderId).is(id));
            template.replace(qu, insp);
        }
    }

    @Override
    public void deleteArchivedInspectionOrder(String id) {
        Query qu = new Query();
        qu.addCriteria(Criteria.where(inspectionOrderId).is(id));
        template.remove(qu, InspectionOrder.class);
    }


}
