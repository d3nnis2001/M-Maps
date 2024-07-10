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

/**
 * The type Archiv controller.
 */
@RestController
@RequestMapping("/api/archiv")
public class ArchivController {
    /**
     * The constant ID.
     */
    public static final String ID = "id";
    private InspectionServices inspservice;

    private ReparaturService repservice;

    /**
     * Instantiates a new Archiv controller.
     *
     * @param isp        the isp
     * @param repservice the repservice
     */
    @Autowired
    public ArchivController(InspectionServices isp, ReparaturService repservice) {
        this.inspservice = isp;
        this.repservice = repservice;
    }

    /**
     * Gets all inspectionorders.
     *
     * @return the all inspectionorders
     */
    @PostMapping("/getAllInspectionOrders")
    public List<InspectionOrder> getAllInspectionorders() {
        return inspservice.getArchivedInspectionOrders();
    }

    /**
     * Unarchive inspection order.
     *
     * @param request the request
     */
    @PostMapping("/unarchiveInspectionOrder")
    public void unarchiveInspectionOrder(final WebRequest request) {
        String id = request.getParameter(ID);
        inspservice.unarchiveInspectionOrder(id);
    }

    /**
     * Delete archived reparatur.
     *
     * @param request the request
     */
    @PostMapping("/deleteArchivedReparatur")
    public void deleteArchivedReparatur(final WebRequest request) {
        String id = request.getParameter(ID);
        repservice.deleteArchivedRep(id);
    }

    /**
     * Gets all reparatur.
     *
     * @return the all reparatur
     */
    @PostMapping("/getAllReparatur")
    public List<Reparatur> getAllReparatur() {
        return repservice.getArchivedRep();
    }

    /**
     * Unarchive reparatur.
     *
     * @param request the request
     */
    @PostMapping("/unarchiveReparatur")
    public void unarchiveReparatur(final WebRequest request) {
        String id = request.getParameter(ID);
        repservice.unarchiveRep(id);
    }

    /**
     * Delete archived inspection order.
     *
     * @param request the request
     */
    @PostMapping("/deleteArchivedInspectionOrder")
    public void deleteArchivedInspectionOrder(final WebRequest request) {
        String id = request.getParameter(ID);
        inspservice.deleteArchivedInspectionOrder(id);
    }
}
