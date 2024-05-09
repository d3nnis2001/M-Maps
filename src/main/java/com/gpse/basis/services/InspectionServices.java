package com.gpse.basis.services;

import com.gpse.basis.domain.InspectionOrder;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;

import java.util.ArrayList;

public interface InspectionServices {

    void createInspectionOrder(String streckenId, String userId, String start, String end, String startTime, String endTime, String fachabteilung, String messdaten, String status, String bemerkungen, boolean archiviert);

    void editInspectionOrder(String streckenId, String userId, String start, String end, String startTime, String endTime, String fachabteilung, String messdaten, String status, String bemerkungen, boolean archiviert);

    void acceptInspectionOrder(String streckenId, String userId, String status);

    ArrayList<InspectionOrder> getInspecData();

}
