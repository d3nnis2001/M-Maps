package com.gpse.basis.web;

import com.gpse.basis.domain.Checklist;
import com.gpse.basis.domain.GeoCords;
import com.gpse.basis.domain.Reparatur;
import com.gpse.basis.services.ChecklistService;
import com.gpse.basis.services.EmailServices;
import com.gpse.basis.services.ReparaturService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * The type Reparatur controller.
 */
@RestController
@RequestMapping("/api/repair")
public class ReparaturController {
    private final String minus = "-";
    private final String name_string = "name";
    private final String slash = "/";
    private final String id_string = "id";
    private final ReparaturService service;
    private final ChecklistService checkService;
    private final EmailServices emailService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Instantiates a new Reparatur controller.
     *
     * @param service      the service
     * @param checkService the check service
     * @param emailService the email service
     */
    @Autowired
    public ReparaturController(ReparaturService service, ChecklistService checkService, EmailServices emailService) {
        this.service = service;
        this.checkService = checkService;
        this.emailService = emailService;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    @Operation(summary = "Hol Reparaturaufträge", description = "Holt alle Reparaturaufträge aus der Datenbank.")
    @GetMapping("/getdata")
    public ArrayList<Reparatur> getData() {
        return service.getRepData();
    }

    /**
     * Send data response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @Operation(summary = "Neuer Reparaturauftrag", description = "Setzt einen neuen Reparaturauftrag in die Datenbank.")
    @PostMapping("/senddata")
    public ResponseEntity<Boolean> sendData(final WebRequest request) {
        String track = request.getParameter("track");
        String date1 = request.getParameter("from");
        LocalDate acDate1 = LocalDate.parse(date1.replace("/", minus), formatter);
        System.out.println(acDate1.toString());
        String date2 = request.getParameter("till");
        LocalDate acDate2 = LocalDate.parse(date2.replace("/", minus), formatter);
        String authorized = request.getParameter("authorized");
        String checklist = request.getParameter("checklist");
        String remarks = request.getParameter("remarks");
        Checklist checker = checkService.loadChecklistByName(checklist);
        System.out.println(checker.getName());
        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");
        GeoCords geo = new GeoCords(latitude, longitude);
        if (latitude.equals("")) {
            geo = null;
        }

        return ResponseEntity.ok(service.addRepairOrder(Integer.parseInt(track), acDate1, acDate2,
            authorized, checker, remarks, geo));
    }

    /**
     * Gets checklists.
     *
     * @return the checklists
     */
    @Operation(summary = "Alle Checklisten", description = "Holt alle Checklisten Namen aus der Datenbank.")
    @PostMapping("/allchecklists")
    public ArrayList<String> getChecklists() {
        return checkService.getAllNames();
    }

    /**
     * Gets rep by id.
     *
     * @param request the request
     * @return the rep by id
     */
    @Operation(summary = "Reparaturauftrag nach ID", description = "Holt die "
        + "Reparaturauftrag Informationen nach der ID.")
    @GetMapping("/getbyid")
    public Reparatur getRepById(final WebRequest request) {
        Reparatur newRep = service.loadRepByName(request.getParameter(id_string));
        return newRep;
    }

    /**
     * Change rep checklist response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @Operation(summary = "Änderung Reparaturauftrag", description = "Ändert state einer Reparaturauftrags Checkliste.")
    @PostMapping("/changeById")
    public ResponseEntity<Boolean> changeRepChecklist(final WebRequest request) {
        String tickedString = request.getParameter("ticked");
        String[] tickedIndices = tickedString.split(",");
        String id = request.getParameter(id_string);
        checkService.changeRepChecklist(id, tickedIndices);
        return ResponseEntity.ok(true);
    }

    /**
     * Change status response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @Operation(summary = "Status Änderung", description = "Ändert Status eines Reparaturauftrags nach ID.")
    @PostMapping("/changestatus")
    public ResponseEntity<Boolean> changeStatus(final WebRequest request) {
        String newStatus = request.getParameter("status");
        String name = request.getParameter(name_string);
        return ResponseEntity.ok(service.changeStatus(name, newStatus));
    }

    /**
     * Delete repair order response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @Operation(summary = "Lösch Reparaturauftrag", description = "Löscht Reparaturauftrag aus Datenbank.")
    @PostMapping("/deleterepairorder")
    public ResponseEntity<Boolean> deleteRepairOrder(final WebRequest request) {
        String name = request.getParameter(name_string);
        return ResponseEntity.ok(service.deleteOrder(name));
    }

    /**
     * Gets ticked list.
     *
     * @param request the request
     * @return the ticked list
     */
    @Operation(summary = "Jetzige Checkliste", description = "Holt state einer Checkliste von einem Reparaturauftrag"
        + " aus der Datenbank.")
    @GetMapping("/getticked")
    public ResponseEntity<ArrayList<String>> getTickedList(final WebRequest request) {
        String id = request.getParameter(id_string);
        return ResponseEntity.ok(checkService.getTickedwithId(id_string));
    }

    /**
     * Sets terminated.
     *
     * @param request the request
     * @return the terminated
     */
    @Operation(summary = "Setz auf terminiert", description = "Setzt Reparaturauftrag auf terminiert"
        + " aus der Datenbank.")
    @PostMapping("/setterminated")
    public ResponseEntity<Boolean> setTerminated(final WebRequest request) {
        String id = request.getParameter(id_string);
        LocalDate date1 = LocalDate.parse(request.getParameter("date").replace("/", minus), formatter);
        return ResponseEntity.ok(checkService.setTerminatedDate(id, date1));
    }

    /**
     * Email track builder response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/emailTrackBuilder")
    public ResponseEntity<Boolean> emailTrackBuilder(final WebRequest request) {
        String trackBuilderEmail = request.getParameter("email");
        String id = request.getParameter(id_string);
        emailService.builtEmailTrackBuilder(trackBuilderEmail, id);
        return ResponseEntity.ok(true);
    }

    /**
     * Gets termination date.
     *
     * @param request the request
     * @return the termination date
     */
    @PostMapping("/getTerminationDate")
    public String getTerminationDate(final WebRequest request) {
        String id = request.getParameter(id_string);
        String date = checkService.getTerminationDate(id_string).toString();
        return date;
    }

}
