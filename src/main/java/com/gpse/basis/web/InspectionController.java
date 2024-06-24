package com.gpse.basis.web;

import com.gpse.basis.domain.InspectionOrder;
import com.gpse.basis.services.InspectionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/inspection")
public class InspectionController {

    private final InspectionServices inspec;
    @Autowired
    public InspectionController(final InspectionServices inspec) {
        this.inspec = inspec;
    }
    @GetMapping("/getdata")
    public ArrayList<InspectionOrder> getData() {
        return inspec.getInspecData();
    }


    @PostMapping("/senddata")
    public void sendData(final WebRequest request) {
        ArrayList<String> inspecArray = new ArrayList<>();
        inspecArray.add(request.getParameter("courseId"));
        inspecArray.add(request.getParameter("startLocation"));
        inspecArray.add(request.getParameter("endLocation"));
        inspecArray.add(request.getParameter("startTime"));
        inspecArray.add(request.getParameter("endTime"));
        inspecArray.add(request.getParameter("department"));
        inspecArray.add(request.getParameter("inspectionData"));
        inspecArray.add(request.getParameter("remarks"));
        inspec.createInspectionOrder(inspecArray);
    }

    @GetMapping("/getById")
    public InspectionOrder getDataById(final WebRequest request) {
        InspectionOrder inspection = inspec.loadInspecById(request.getParameter("inspectionOrderId"));
        return inspection;
    }

    @PostMapping("/sendById")
    public void sendDataById(final WebRequest request) {
        InspectionOrder inspectionOrderNew = new InspectionOrder(request.getParameter("inspectionOrderId"),
            request.getParameter("courseId"), "", request.getParameter("startLocation"),
            request.getParameter("endLocation"), request.getParameter("startTime"),
            request.getParameter("endTime"), request.getParameter("department"),
            request.getParameter("inspectionData"), "", request.getParameter("remarks"), false);
        inspec.editInspectionOrder(inspectionOrderNew);
    }

    @PostMapping("/deleteInspectionOrder")
    public void deleteInspectionOrder(final WebRequest request) {
        String id = request.getParameter("inspectionOrderId");
        System.out.println("TEST: " +  id);
        inspec.deleteInspectionOrder(id);
    }

}
