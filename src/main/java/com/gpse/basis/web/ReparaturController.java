package com.gpse.basis.web;

import com.gpse.basis.domain.Checklist;
import com.gpse.basis.domain.GeoCords;
import com.gpse.basis.domain.Reparatur;
import com.gpse.basis.domain.Utils;
import com.gpse.basis.services.ChecklistService;
import com.gpse.basis.services.ReparaturService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("/api/repair")
public class ReparaturController {
    private ReparaturService service;
    private ChecklistService checkService;
    Utils utils = new Utils();
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
    public ResponseEntity<Boolean> sendData(final WebRequest request) {
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
        Checklist checker = checkService.loadChecklistByName(checklist);
        System.out.println(checker.getName());
        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");
        GeoCords geo = new GeoCords(latitude, longitude);
        if (latitude.equals("") ) {
            geo = null;
        }

        return ResponseEntity.ok(service.addRepairOrder(Integer.parseInt(track), acDate1, acDate2, authorized, checker, remarks, geo));
    }
    @PostMapping("/allchecklists")
    public ArrayList<String> getChecklists() {
        return checkService.getAllNames();
    }

    @GetMapping("/getbyid")
    public Reparatur getRepById(final WebRequest request) {
        Reparatur newRep = service.loadRepByName(request.getParameter("id"));
        return newRep;
    }

    @PostMapping("/changeById")
    public ResponseEntity<Boolean> changeRepChecklist(final WebRequest request) {
        String tickedString = request.getParameter("ticked");
        String[] tickedIndices = tickedString.split(",");
        String id = request.getParameter("id");
        checkService.changeRepChecklist(id, tickedIndices);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/changestatus")
    public ResponseEntity<Boolean> changeStatus(final WebRequest request) {
        String newStatus = request.getParameter("status");
        String name = request.getParameter("name");
        return ResponseEntity.ok(service.changeStatus(name, newStatus));
    }

    @PostMapping("/deleterepairorder")
    public ResponseEntity<Boolean> deleteRepairOrder(final WebRequest request) {
        String name = request.getParameter("name");
        return ResponseEntity.ok(service.deleteOrder(name));
    }

    @PostMapping("/getticked")
    public ResponseEntity<ArrayList<String>> getTickedList(final WebRequest request) {
        String id = request.getParameter("id");
        return ResponseEntity.ok(checkService.getTickedwithId(id));
    }

    @PostMapping("/setterminated")
    public ResponseEntity<Boolean> setTerminated(final WebRequest request) {
        String id = request.getParameter("id");
        Date date1 = utils.transformString(request.getParameter("date"));
        return ResponseEntity.ok(checkService.setTerminatedDate(id, date1));
    }
}
