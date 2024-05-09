package com.gpse.basis.web;

import com.gpse.basis.domain.InspectionOrder;
import com.gpse.basis.services.InspectionServices;
import org.springframework.web.bind.annotation.GetMapping;
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

    public void sendData(final WebRequest request) {

    }

}
