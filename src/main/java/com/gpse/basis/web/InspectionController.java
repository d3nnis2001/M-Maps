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
        System.out.println("TEST_04");
        return inspec.getInspecData();
    }


    @PostMapping("/senddata")
    public void sendData(final WebRequest request) {
        System.out.println("TEST-SENDDATA");
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
        InspectionOrder inspection = inspec.loadInspecById(request.getParameter("streckenId"));
        return inspection;

    }

    @PostMapping("/sendById")
    public InspectionOrder sendDataById() {
        return null;
    }

}
