package com.gpse.basis.web;

import com.gpse.basis.domain.InspectionOrder;
import com.gpse.basis.services.InspectionServices;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;

/**
 * Contoller für die Prüfaufträge.
 */
@RestController
@RequestMapping("/api/inspection")
public class InspectionController {

    private final InspectionServices inspec;
    private String courseId = "courseId";
    private String inspectionOrderID = "inspectionOrderId";
    private String startLocation = "startLocation";
    private String endLocation = "endLocation";
    private String startTime = "startTime";
    private String endTime = "endTime";
    private String department = "department";
    private String inspectionData = "inspectionData";
    private String remarks = "remarks";
    private String priority = "priority";

    @Autowired
    public InspectionController(final InspectionServices inspec) {
        this.inspec = inspec;
    }

    /**
     * Alle Prüfaufträge aus Backend holen.
     * @return ArrayList
     */
    @Operation(summary = "Holt Daten aus der Datenbank",
        description = "Daten werden in einer ArrayList aus Prüfauftragen zurückgegeben")
    @GetMapping("/getdata")
    public ArrayList<InspectionOrder> getData() {
        return inspec.getInspecData();
    }


    /**
     * Prüfauftragsdaten senden.
     * @param request - Anfrage
     */
    @Operation(summary = "Erstellt Prüfauftrag",
        description = "Übergibt die Daten, um einen neuen Prüfauftrag zu erstellen")
    @PostMapping("/senddata")
    public void sendData(final WebRequest request) {
        ArrayList<String> inspecArray = new ArrayList<>();
        inspecArray.add(request.getParameter(courseId));
        inspecArray.add(request.getParameter(startLocation));
        inspecArray.add(request.getParameter(endLocation));
        inspecArray.add(request.getParameter(startTime));
        inspecArray.add(request.getParameter(endTime));
        inspecArray.add(request.getParameter(department));
        inspecArray.add(request.getParameter(inspectionData));
        inspecArray.add(request.getParameter(remarks));
        inspecArray.add(request.getParameter(priority));
        inspec.createInspectionOrder(inspecArray);
    }

    /**
     * einzelnen Prüfauftrag holen.
     * @param request - Anfrage
     * @return InspectionOrder
     */
    @Operation(summary = "Holt einen Prüfauftrag nach der Id",
        description = "Gibt den Prüfauftrag mit der übergebenen Id zurück ")
    @GetMapping("/getById")
    public InspectionOrder getDataById(final WebRequest request) {
        InspectionOrder inspection = inspec.loadInspecById(request.getParameter(inspectionOrderID));
        return inspection;
    }

    /**
     * Anhand der ID Daten senden.
     * @param request - Anfrage
     */
    @Operation(summary = "Prüfauftrag editieren",
        description = "Übergibt alle ggf. veränderten Attribute, damit der bestehende Prüfauftrag editiert wird")
    @PostMapping("/sendById")
    public void sendDataById(final WebRequest request) {
        InspectionOrder inspectionOrderNew = new InspectionOrder(request.getParameter(inspectionOrderID),
            request.getParameter(courseId), "", request.getParameter(startLocation),
            request.getParameter(endLocation), request.getParameter(startTime),
            request.getParameter(endTime), request.getParameter(department),
            request.getParameter(inspectionData), "", request.getParameter(remarks), false,
            request.getParameter(priority));
        inspec.editInspectionOrder(inspectionOrderNew);
    }

    /**
     * Prüfauftrag löschen.
     * @param request - Anfrage
     */
    @Operation(summary = "Löscht den Prüfauftrag",
        description = "Erhält eine Id, dessn der Prüfauftrag gelöscht wird")
    @PostMapping("/deleteInspectionOrder")
    public void deleteInspectionOrder(final WebRequest request) {
        String id = request.getParameter(inspectionOrderID);
        inspec.deleteInspectionOrder(id);
    }

    /**
     * neuen Status eintragen.
     * @param request - Anfrage
     */
    @Operation(summary = "Status des Prüfauftrags editieren",
        description = "Der Prüfauftrag der übergebenen Id bekommt den übergebenen Status")
    @PostMapping("/sendNewStatus")
    public void sendNewStatus(final WebRequest request) {
        String inspectionOrderId = request.getParameter(inspectionOrderID);
        String status = request.getParameter("status");
        inspec.editStatus(inspectionOrderId, status);
    }

    /**
     * Bewertung abgeben.
     * @param request - Anfrage
     */
    @Operation(summary = "Bewertung speichern",
        description = "An den Prüfauftrag mit der übergebenen Id wird eine Bewertung und ein Abschlussdatum übergeben")
    @PostMapping("/sendReview")
    public void sendReview(final WebRequest request) {
        String inspectionOrderId = request.getParameter(inspectionOrderID);
        String review = request.getParameter("review");
        String date = request.getParameter("date");
        inspec.editReview(inspectionOrderId, review, date);
    }

    /**
     * Prüfauftrag annehmen.
     * @param request - Anfrage
     */
    @Operation(summary = "Prüfauftrag wird angenommen",
        description = "An den Prüfauftrag mit der übergebenen Id wird ein Username übermittelt")
    @PostMapping("/sendUsername")
    public void sendUsername(final WebRequest request) {
        String inspectionOrderId = request.getParameter(inspectionOrderId);
        String username = request.getParameter("username");
        inspec.editUsername(inspectionOrderId, username);
    }



}
