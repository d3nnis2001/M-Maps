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


    // -> CreateInspectionOrder
    @PostMapping("/senddata")
    public void sendData(final WebRequest request) {
        System.out.println("TEST-SENDDATA");
        String inspectionOrderId = request.getParameter("inspectionOrderId");
        String courseId = request.getParameter("courseId");
        String userId = request.getParameter("userId");
        String startLocation = request.getParameter("startLocation");
        String endLocation = request.getParameter("endLocation");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String department = request.getParameter("department");
        String data = request.getParameter("data");
        String remarks = request.getParameter("remarks");
        String status = request.getParameter("status");
        boolean archiviert = Boolean.valueOf(request.getParameter("archiviert"));
        InspectionOrder inspecOrder = new InspectionOrder(inspectionOrderId, courseId, userId, startLocation, endLocation, startTime, endTime,
            department, data, status, remarks, archiviert);
        inspec.createInspectionOrder(inspecOrder);

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
