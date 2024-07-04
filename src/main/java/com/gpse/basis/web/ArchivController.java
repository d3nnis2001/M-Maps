package com.gpse.basis.web;

import com.gpse.basis.domain.InspectionOrder;
import com.gpse.basis.domain.Reparatur;
import com.gpse.basis.services.InspectionServices;
import com.gpse.basis.services.ReparaturService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestController
@RequestMapping("/api/archiv")
public class ArchivController {
    private InspectionServices inspservice;

    private ReparaturService repservice;

    @Autowired
    public ArchivController(InspectionServices isp, ReparaturService repservice) {
        this.inspservice = isp;
        this.repservice = repservice;
    }

    @PostMapping("/getAllInspectionOrders")
    public List<InspectionOrder> getAllInspectionorders() {
        return inspservice.getArchivedInspectionOrders();
    }

    @PostMapping("/unarchiveInspectionOrder")
    public void unarchiveInspectionOrder(final WebRequest request) {
        String id = request.getParameter("id");
        inspservice.unarchiveInspectionOrder(id);
    }

    @PostMapping("/deleteArchivedReparatur")
    public void deleteArchivedReparatur(final WebRequest request) {
        String id = request.getParameter("id");
        repservice.deleteArchivedRep(id);
    }

    @PostMapping("/getAllReparatur")
    public List<Reparatur> getAllReparatur() {
        return repservice.getArchivedRep();
    }

    @PostMapping("/unarchiveReparatur")
    public void unarchiveReparatur(final WebRequest request) {
        String id = request.getParameter("id");
        repservice.unarchiveRep(id);
    }

    @PostMapping("/deleteArchivedInspectionOrder")
    public void deleteArchivedInspectionOrder(final WebRequest request) {
        String id = request.getParameter("id");
        inspservice.deleteArchivedInspectionOrder(id);
    }
}
