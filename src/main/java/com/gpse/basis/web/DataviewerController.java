package com.gpse.basis.web;

import com.gpse.basis.domain.*;
import com.gpse.basis.services.FileService;
import com.gpse.basis.services.ImageService;
import com.gpse.basis.services.ReparaturService;
import com.gpse.basis.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the Dataviewer.
 */
@RestController
@RequestMapping("/api/dataviewer")
public class DataviewerController {
    /**
     * The File service.
     */
    public final FileService fileService;
    private final UserServices userServices;
    private final ReparaturService reparaturService;
    private final ImageService imageService;

    /**
     * Instantiates a new Dataviewer controller.
     *
     * @param fileService      the file service
     * @param userServices     the user services
     * @param reparaturService the reparatur service
     * @param imageService     the image service
     */
    @Autowired
    public DataviewerController(FileService fileService, UserServices userServices,
                                ReparaturService reparaturService, ImageService imageService) {
        this.fileService = fileService;
        this.userServices = userServices;
        this.reparaturService = reparaturService;
        this.imageService = imageService;
    }

    /**
     * Gets all track layout data 2.
     *
     * @param trackId the track id
     * @return the all track layout data 2
     */
    @Operation(summary = "Lädt Gleislagedaten zu Strecke",
        description = "Funktion um alle Gleislagedaten zu einer gegebenen Strecke beim mounten zu laden")
    @GetMapping("/getTrackData2")
    public ArrayList<GleisLageDatenpunkt> getAllTrackLayoutData2(@RequestParam int trackId) {
        return fileService.getData(trackId);
    }

    /**
     * Gets all point layout data.
     *
     * @param pointId the point id
     * @return the all point layout data
     */
    @Operation(summary = "Lädt Gleislagedaten zu Punkt",
        description = "Funktion um alle Gleislagedaten zu einem gegebenem Punkt beim mounten zu laden")
    @GetMapping("/getPointData")
    public ArrayList<GleisLageDatenpunkt> getAllPointLayoutData(@RequestParam String pointId) {
        return fileService.getPointData(pointId);
    }

    /**
     * Gets point info.
     *
     * @param pointId the point id
     * @return the point info
     */
    @Operation(summary = "Lädt Punktinformationen",
        description = "Funktion um die Geodaten zu einem gegebenem Punkt beim mounten zu laden")
    @GetMapping("/getPointInfo")
    public GeoData getPointInfo(@RequestParam String pointId) {
        return fileService.getPointInformation(pointId);
    }

    /**
     * Gets point rep.
     *
     * @param lat the lat
     * @param lon the lon
     * @return the point rep
     */
    @Operation(summary = "Lädt Reparaturaufträge zu Punkt",
        description = "Funktion um die Reparaturaufträge zu einem gegebenem Punkt beim mounten zu laden")
    @GetMapping("/getPointRep")
    public List<Reparatur> getPointRep(@RequestParam Double lat, Double lon) {
        return reparaturService.getReparaturForPoint(lat, lon);
    }

    /**
     * Gets point pic.
     *
     * @param repId the rep id
     * @return the point pic
     */
    @Operation(summary = "Lädt Bilder",
        description = "Funktion um alle Bilder zu einem gegebenem Reparaturauftrag zu laden")
    @GetMapping("/getPointPic")
    public List<Image> getPointPic(@RequestParam String repId) {
        return imageService.getImagesForOrder(repId);
    }
}
