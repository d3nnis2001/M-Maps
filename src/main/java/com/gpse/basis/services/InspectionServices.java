package com.gpse.basis.services;

import com.gpse.basis.domain.InspectionOrder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

public interface InspectionServices {

    void createInspectionOrder(InspectionOrder inspectionOrder);

    void editInspectionOrder(InspectionOrder inspectionOrder);

    void acceptInspectionOrder(InspectionOrder inspectionOrder);

    ArrayList<InspectionOrder> getInspecData();
    InspectionOrder loadInspecById(String streckenId) throws UsernameNotFoundException;

}
