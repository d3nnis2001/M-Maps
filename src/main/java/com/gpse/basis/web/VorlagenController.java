package com.gpse.basis.web;

import com.gpse.basis.domain.Vorlage;
import com.gpse.basis.services.VorlagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
@RestController
@RequestMapping("/api/vorlage")
public class VorlagenController {
    private final VorlagenService vorlagenService;
    private String vorlagenId = "vorlagenId";
    private String stringSubject = "stringSubject";
    private String stringBody = "stringBody";


    @Autowired
    public VorlagenController(final VorlagenService vorlagenService) {
        this.vorlagenService = vorlagenService;
    }

    @ResponseBody
    @GetMapping("/getdata/{vorlagenId}")
    public ArrayList<Vorlage> getData(@PathVariable final String vorlagenId) {
        System.out.println("GetMapping Id: " + vorlagenId);
        ArrayList<Vorlage> vorlagenArray = vorlagenService.getVorlagenData();
        System.out.println(vorlagenArray.getFirst().getVorlagenId());
        return vorlagenArray;
    }

    @PostMapping("/senddata")
    public void sendData(final WebRequest request) {
        vorlagenService.deleteVorlage(request.getParameter(vorlagenId));
        Vorlage vorlage = new Vorlage(request.getParameter(vorlagenId), request.getParameter(stringSubject), request.getParameter(stringBody));
        vorlagenService.editVorlage(vorlage);
    }
}
