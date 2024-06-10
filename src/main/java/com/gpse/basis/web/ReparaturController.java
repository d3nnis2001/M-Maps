package com.gpse.basis.web;

import com.gpse.basis.domain.Checklist;
import com.gpse.basis.domain.GeoCords;
import com.gpse.basis.domain.Reparatur;
import com.gpse.basis.services.ChecklistService;
import com.gpse.basis.services.EmailServices;
import com.gpse.basis.services.ReparaturService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/repair")
public class ReparaturController {
    private ReparaturService service;
    private ChecklistService checkService;
    private final EmailServices emailService;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    public ReparaturController(ReparaturService service, ChecklistService checkService, EmailServices emailService) {
        this.service = service;
        this.checkService = checkService;
        this.emailService = emailService;
    }

    @GetMapping("/getdata")
    public ArrayList<Reparatur> getData() {
        return service.getRepData();
    }
    @PostMapping("/senddata")
    public ResponseEntity<Boolean> sendData(final WebRequest request) {
        String track = request.getParameter("track");
        String date1 = request.getParameter("from");
        LocalDate acDate1 = LocalDate.parse(date1.replace("/", "-"), formatter);
        System.out.println(acDate1.toString());
        String date2 = request.getParameter("till");
        LocalDate acDate2 = LocalDate.parse(date2.replace("/", "-"), formatter);
        String authorized = request.getParameter("authorized");
        String checklist = request.getParameter("checklist");
        String remarks = request.getParameter("remarks");
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
        LocalDate date1 = LocalDate.parse(request.getParameter("date").replace("/", "-"), formatter);
        return ResponseEntity.ok(checkService.setTerminatedDate(id, date1));
    }
    @PostMapping("/upload")
    public void handleFileUpload(@RequestPart(value = "file") final MultipartFile uploadfile) throws IOException {
        saveUploadedFiles(uploadfile);
    }

    private void saveUploadedFiles(final MultipartFile file) throws IOException {
        final byte[] bytes = file.getBytes();
        final Path path = Paths.get("src/main/resources/imagesRepair/" + file.getOriginalFilename());
        Files.write(path, bytes);
    }

    @PostMapping("/emailTrackBuilder")
    public ResponseEntity<Boolean> emailTrackBuilder(final WebRequest request) {
        String trackBuilderEmail = request.getParameter("email");
        emailService.builtEmailTrackBuilder(trackBuilderEmail);
        return ResponseEntity.ok(true);
    }
}
