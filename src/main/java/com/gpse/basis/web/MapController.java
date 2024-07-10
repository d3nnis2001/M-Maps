package com.gpse.basis.web;

import com.gpse.basis.domain.GeoData;
import com.gpse.basis.domain.VelodynePoint;
import com.gpse.basis.services.DataService;
import com.gpse.basis.domain.Reparatur;
import com.gpse.basis.services.ReparaturService;
import com.gpse.basis.services.RosBagService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Map controller.
 */
@RestController
@RequestMapping("/api/map")
public class MapController {


    private final DataService dataService;

    private final ReparaturService repService;

    private RosBagService rosBag;
    private final String trackString = "trackid";

    /**
     * Instantiates a new Map controller.
     *
     * @param ros         the ros
     * @param dataService the data service
     * @param rps         the rps
     */
    @Autowired
    public MapController(RosBagService ros, DataService dataService, ReparaturService rps) {
        this.rosBag = ros;
        this.dataService = dataService;
        this.repService = rps;
    }

    /**
     * Gets all geo data.
     *
     * @return the all geo data
     */
    @Operation(summary = "Geodata", description = "Holt alle Geodaten die existieren aus der Datenbank.")
    @GetMapping("/gettracks")
    public ArrayList<GeoData> getAllGeoData() {
        return dataService.getGeoData();
    }

    /**
     * Gets part of heatmap.
     *
     * @param request the request
     * @return the part of heatmap
     */
    @Operation(summary = "Heatmap Part", description = "Holt einen Teil der Heatmap Daten.")
    @PostMapping("/getpartheatmap")
    public List<ResponseColor> getPartOfHeatmap(final WebRequest request) {
        String strecke = request.getParameter("strecke");
        String from = request.getParameter("from");
        String till = request.getParameter("till");
        LocalDate fromD = LocalDate.parse(from, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate tillD = LocalDate.parse(till, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDateTime fromDate = fromD.atStartOfDay();
        LocalDateTime tillDate = tillD.atStartOfDay();
        System.out.println("Datum-Form: " + fromDate);
        System.out.println("Datum-Till: " + tillDate);
        List<Map.Entry<DataService.Colors, String>> lst = dataService.getGeoDataByDate(Integer.parseInt(strecke),
            fromDate, tillDate);
        List<ResponseColor> kolor = new ArrayList<>(lst.size());
        for (int i = 0; i < lst.size(); ++i) {
            kolor.add(i, new ResponseColor(lst.get(i).getValue(), lst.get(i).getKey()));
        }
        return kolor;
    }

    /**
     * Gets heatmap.
     *
     * @return the heatmap
     */
    @Operation(summary = "Heatmap", description = "Holt Heatmap Daten mit zugehöriger Farbe.")
    @PostMapping("/getheatmap")
    public List<Map.Entry<DataService.Colors, String>> getHeatmap() {
        return dataService.getHeatmap();
    }

    private class ResponseColor {
        private final String id;
        private int color;

        /**
         * Instantiates a new Response color.
         *
         * @param id    the id
         * @param color the color
         */
        public ResponseColor(String id, DataService.Colors color) {
            this.id = id;
            if (color == DataService.Colors.NORMAL) {
                this.color = 0;
            } else if (color == DataService.Colors.LOW) {
                this.color = 1;
            } else if (color == DataService.Colors.MEDIUM) {
                this.color = 2;
            } else if (color == DataService.Colors.HIGH) {
                this.color = 3;
            }
        }
    }

    /**
     * Get datato geo track double [ ].
     *
     * @param request the request
     * @return the double [ ]
     */
    @Operation(summary = "Geodata nach Strecke", description = "Holt Geodata von einer Strecke.")
    @PostMapping("/getDataGeoTrack")
    public Double[] getDatatoGeoTrack(final WebRequest request) {
        String strecke = request.getParameter("id");
        // links-Abweichung, rechts-abweichung, durschnittliche zulässige geschwindigkeit,
        // durchschnittliche gefahrene Geschwindigkeit
        return dataService.getDataForGeoPart(strecke);
    }

    /**
     * Gets reparaturfor map.
     *
     * @return the reparaturfor map
     */
    @Operation(summary = "Hol alle Reparaturaufträge", description = "Holt Repaufräge aus der Datenbank.")
    @PostMapping("/getReparaturForMap")
    public List<Reparatur> getReparaturforMap() {
        return repService.getReparaturForMap();
    }

    /**
     * Gets camera image for track request.
     *
     * @param request the request
     * @return the camera image for track request
     */
    @Operation(summary = "Images", description = "Holt images von einer Strecke.")
    @PostMapping("/getCameraImageforTrack")
    public List<String> getCameraImageForTrackRequest(final WebRequest request) {
        int trackId = Integer.parseInt(request.getParameter(trackString));
        return rosBag.getImagesForTrack(trackId);
    }

    /**
     * Gets ir camera imagefor track.
     *
     * @param request the request
     * @return the ir camera imagefor track
     */
    @PostMapping("/getIRCameraImageforTrack")
    public List<String> getIRCameraImageforTrack(final WebRequest request) {
        int trackId = Integer.parseInt(request.getParameter(trackString));
        return rosBag.getIRImagesForTrack(trackId);
    }

    /**
     * Gets velodyn pointsfor track.
     *
     * @param request the request
     * @return the velodyn pointsfor track
     */
    @PostMapping("/getVelodynPointsforTrack")
    public List<List<VelodynePoint>> getVelodynPointsforTrack(final WebRequest request) {
        int trackId = Integer.parseInt(request.getParameter(trackString));
        int index = Integer.parseInt(request.getParameter("index"));
        var lst = rosBag.getVelodynePointsForTrack(trackId);
        if (!lst.isEmpty()) {
            return lst.subList(index, index + 1);
        } else {
            return new ArrayList<>();
        }
    }

}
