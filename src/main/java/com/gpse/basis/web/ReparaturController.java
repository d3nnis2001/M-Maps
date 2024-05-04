package com.gpse.basis.web;

import com.gpse.basis.domain.Reparatur;
import com.gpse.basis.services.ReparaturService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

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
}
