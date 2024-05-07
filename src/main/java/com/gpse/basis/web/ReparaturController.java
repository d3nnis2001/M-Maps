package com.gpse.basis.web;

import com.gpse.basis.domain.Checklist;
import com.gpse.basis.domain.Reparatur;
import com.gpse.basis.domain.Utils;
import com.gpse.basis.services.ChecklistService;
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
    private ChecklistService checkService;
    @Autowired
    public ReparaturController(ReparaturService service, ChecklistService checkService) {
        this.service = service;
        this.checkService = checkService;
    }

    @GetMapping("/getdata")
    public ArrayList<Reparatur> getData() {
        return service.getRepData();
    }
    @PostMapping("/senddata")
    public boolean sendData(final WebRequest request) {
        Utils utils = new Utils();
        String track = request.getParameter("track");
        System.out.println(track);
        String date1 = request.getParameter("from");
        Date acDate1 = utils.transformString(date1);
        String date2 = request.getParameter("till");
        Date acDate2 = utils.transformString(date2);
        String authorized = request.getParameter("authorized");
        String checklist = request.getParameter("checklist");
        System.out.println(checklist);
        String remarks = request.getParameter("remarks");
        System.out.println(remarks);
        Checklist checker = checkService.loadChecklistByUsername(checklist);
        System.out.println(checker.getName());
        return service.addUser(Integer.parseInt(track), acDate1, acDate2, authorized, checker, remarks);
    }
    @PostMapping("/allchecklists")
    public ArrayList<String> getChecklists() {
        return checkService.getAllNames();
    }
}
