package com.gpse.basis.web;

import com.gpse.basis.domain.InspectionOrder;
import com.gpse.basis.services.InspectionServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;

public class InspectionController {

    private final InspectionServices inspectionServices;

    public InspectionController(InspectionServices inspec) {
        this.inspectionServices = inspec;
    }
    @GetMapping("/getdata")
    public ArrayList<InspectionOrder> getData() {
        return inspectionServices.getInspecData();
    }
    @PostMapping("/senddata")
    public void sendData(final WebRequest request) {

        String streckenId = request.getParameter("streckenId");
        String userId = request.getParameter("userId");
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String fachabteilung = request.getParameter("fachabteilung");
        String messdaten = request.getParameter("messdaten");
        String bemerkungen = request.getParameter("bemerkungen");
        String status = request.getParameter("status");
        boolean archiviert = Boolean.valueOf(request.getParameter("archiviert"));
        InspectionOrder inspecOrder = new InspectionOrder(streckenId, userId, start, end, startTime, endTime,
            fachabteilung, messdaten, status, bemerkungen, archiviert);
        inspectionServices.createInspectionOrder(inspecOrder);

    }

    @GetMapping("/getById")
    public InspectionOrder getDataById(final WebRequest request) {
        InspectionOrder inspec = inspectionServices.loadInspecById(request.getParameter("streckenId"));
        return inspec;

    }

    @PostMapping("/sendById")
    public InspectionOrder sendDataById() {
        return null;
    }

}
