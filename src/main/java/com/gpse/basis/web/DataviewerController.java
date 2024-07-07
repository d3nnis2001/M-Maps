package com.gpse.basis.web;

import com.gpse.basis.domain.*;
import com.gpse.basis.services.FileService;
import com.gpse.basis.services.ImageService;
import com.gpse.basis.services.ReparaturService;
import com.gpse.basis.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/dataviewer")
public class DataviewerController {
    public final FileService fileService;
    private final UserServices userServices;
    private final ReparaturService reparaturService;
    private final ImageService imageService;

    public DataviewerController(FileService fileService, UserServices userServices,
                                ReparaturService reparaturService, ImageService imageService) {
        this.fileService = fileService;
        this.userServices = userServices;
        this.reparaturService = reparaturService;
        this.imageService = imageService;
    }
    /*
    @GetMapping("/getTrackData")
    public ArrayList<DataSet> getAllTrackLayoutData() { return fileService.getAllTrackData}
    */

    /*
    @GetMapping("/getTrackData")
    public ArrayList<GleisLageDatenpunkt> getAllTrackLayoutData() { return fileService.getTrackData(6100);}
    */

    @Operation(summary = "Lädt Gleislagedaten zu Strecke", description = "Funktion um alle Gleislagedaten zu einer gegebenen Strecke beim mounten zu laden")
    @GetMapping("/getTrackData2")
    public ArrayList<GleisLageDatenpunkt> getAllTrackLayoutData2(@RequestParam int trackId) {
        return fileService.getData(trackId);
    }

    @Operation(summary = "Lädt Gleislagedaten zu Punkt", description = "Funktion um alle Gleislagedaten zu einem gegebenem Punkt beim mounten zu laden")
    @GetMapping("/getPointData")
    public ArrayList<GleisLageDatenpunkt> getAllPointLayoutData(@RequestParam String pointId) {
        return fileService.getPointData(pointId);
    }

    @Operation(summary = "Lädt Punktinformationen", description = "Funktion um die Geodaten zu einem gegebenem Punkt beim mounten zu laden")
    @GetMapping("/getPointInfo")
    public GeoData getPointInfo(@RequestParam String pointId) {
        return fileService.getPointInformation(pointId);
    }

    @Operation(summary = "Lädt Reparaturaufträge zu Punkt", description = "Funktion um die Reparaturaufträge zu einem gegebenem Punkt beim mounten zu laden")
    @GetMapping("/getPointRep")
    public List<Reparatur> getPointRep(@RequestParam Double lat, Double lon) {
        return reparaturService.getReparaturForPoint(lat, lon);
    }

    @Operation(summary = "Lädt Bilder", description = "Funktion um alle Bilder zu einem gegebenem Reparaturauftrag zu laden")
    @GetMapping("/getPointPic")
    public List<Image> getPointPic(@RequestParam String repId) {
        return imageService.getImagesForOrder(repId);
    }

    /*
    @GetMapping("/getTrackData")
    public ArrayList<DataSet> getAllTrackLayoutData() {
        ArrayList<DataSet> dataSets = new ArrayList<>();
        List<DataSet> lst = fileService.getDataSets("all");
        for (int i = 0; i < lst.size(); i++) {
            dataSets.add(lst.get(i));
        }
        return dataSets;
    }*/
}
