package com.gpse.basis.services;

import com.gpse.basis.domain.InspectionOrder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface für den InspectionService.
 */
public interface InspectionServices {

    void createInspectionOrder(ArrayList<String> inspecArray);
    void editInspectionOrder(InspectionOrder inspectionOrder);
    Boolean deleteInspectionOrder(String id);
    void editStatus(String inspectionOrderId, String status);
    void editReview(String inspectionOrderId, String review, String date);
    ArrayList<InspectionOrder> getInspecData();
    InspectionOrder loadInspecById(String inspectionOrderId) throws UsernameNotFoundException;
    List<InspectionOrder> getArchivedInspectionOrders();
    void unarchiveInspectionOrder(String id);
    void deleteArchivedInspectionOrder(String id);
    void editUsername(String inspectionOrderId, String username);
}
