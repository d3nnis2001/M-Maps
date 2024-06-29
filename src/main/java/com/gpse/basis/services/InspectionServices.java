package com.gpse.basis.services;

import com.gpse.basis.domain.InspectionOrder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

public interface InspectionServices {

    void createInspectionOrder(ArrayList<String> inspecArray);

    void editInspectionOrder(InspectionOrder inspectionOrder);

    void acceptInspectionOrder(InspectionOrder inspectionOrder);

    Boolean deleteInspectionOrder( String id);
    void editStatus(String inspectionOrderId, String status);

    void editReview(String inspectionOrderId, String review);
    ArrayList<InspectionOrder> getInspecData();
    InspectionOrder loadInspecById(String inspectionOrderId) throws UsernameNotFoundException;

}
