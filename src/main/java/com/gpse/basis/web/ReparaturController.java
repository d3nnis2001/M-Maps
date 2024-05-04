package com.gpse.basis.web;

import com.gpse.basis.domain.Reparatur;
import com.gpse.basis.services.ReparaturService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/api/repair")
public class ReparaturController {
    private ReparaturService service;
    @Autowired
    public ReparaturController(ReparaturService service) {
        this.service = service;
    }

    @GetMapping("/getdata")
    public ArrayList<Reparatur> getData() {
        return service.getRepData();
    }
    @PostMapping("/senddata")
    public boolean sendData(final WebRequest request) {
        String track = request.getParameter("track");
        String date1 = request.getParameter("from");
        String date2 = request.getParameter("till");
        String authorized = request.getParameter("authorized");
        String checklist = request.getParameter("checklist");
        String remarks = request.getParameter("remarks");
        return true;
    }
}
