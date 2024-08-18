package com.gpse.basis.web;

import com.gpse.basis.domain.Vorlage;
import com.gpse.basis.services.VorlagenService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;

/**
 * Vorlagencontroller.
 */
@RestController
@RequestMapping("/api/vorlage")
public class VorlagenController {
    private final VorlagenService vorlagenService;
    private String vorlagenId = "vorlagenId";
    private String stringSubject = "stringSubject";
    private String stringBody = "stringBody";


    /**
     * Konstrukor für den Vorlagenservice.
     * @param vorlagenService
     */
    @Autowired
    public VorlagenController(final VorlagenService vorlagenService) {
        this.vorlagenService = vorlagenService;
    }

    /**
     * GetMapping für die Vorlagen.
     * @param vorlagenId - VorlagenId
     * @return - VorlagenArray
     */
    @Operation(summary = "Holt Daten der Vorlagen aus der Datenbank",
        description = "Daten werden in einer ArrayList aus Vorlagenobjekten zurückgegeben")
    @ResponseBody
    @GetMapping("/getdata/{vorlagenId}")
    public ArrayList<Vorlage> getData(@PathVariable final String vorlagenId) {
        System.out.println("GetMapping Id: " + vorlagenId);
        ArrayList<Vorlage> vorlagenArray = vorlagenService.getVorlagenData();
        System.out.println(vorlagenArray.getFirst().getVorlagenId());
        return vorlagenArray;
    }

    /**
     * PostMapping für die Vorlagen.
     * @param request - Anfrage
     */
    @Operation(summary = "Verändert Daten der Vorlage",
        description = "Daten werden als neue Vorlage in der Datenbank gespeichert und die alte überschrieben")
    @PostMapping("/senddata")
    public void sendData(final WebRequest request) {
        vorlagenService.deleteVorlage(request.getParameter(vorlagenId));
        Vorlage vorlage = new Vorlage(request.getParameter(vorlagenId),
            request.getParameter(stringSubject), request.getParameter(stringBody));
        vorlagenService.editVorlage(vorlage);
    }
}
