package com.gpse.basis.web;

import com.gpse.basis.domain.GeoData;
import com.gpse.basis.domain.GleisLageDatenpunkt;
import com.gpse.basis.services.FileService;
import com.gpse.basis.services.GeoDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.lang.reflect.Array;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/map")
public class MapController {

    @Autowired
    private FileService file;
    private GeoDataService geoService;
    public MapController(FileService file, GeoDataService geoService) {
        this.file = file;
        this.geoService = geoService;
    }

    @GetMapping("/gettracks")
    public ArrayList<GeoData> getAllGeoData() {
        return file.getGeoData();
    }

    @PostMapping("/gettrack")
    public ArrayList<GeoData> getTrackGeoData(final WebRequest request) {
        String trackID = request.getParameter("trackid");
        System.out.println(trackID);
        assert trackID != null;
        return file.getTrackGeoData(Integer.parseInt(trackID));
    }

    @PostMapping("/getparttrack")
    public ArrayList<GeoData> getPartTrack(final WebRequest request) {
        int from = Integer.parseInt(request.getParameter("from"));
        int till = Integer.parseInt(request.getParameter("till"));
        return file.getPartGeoData(from, till);
    }
    @PostMapping("/fromcolours")
    public ArrayList<GeoData> getGleisDaten(final WebRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        ArrayList<GeoData> allGeoPoints = geoService.getAllGeoPoints();
        int arrSize = allGeoPoints.size();
        for (int i = 0; i < allGeoPoints.size();i++) {
            double from = 0;
            double till;
            if (i != 0 && arrSize - 1 != i) {
                from = (allGeoPoints.get(i-1).getTrack_km() +
                    allGeoPoints.get(i).getTrack_km())/2000;
                till = (allGeoPoints.get(i).getTrack_km() +
                    allGeoPoints.get(i+1).getTrack_km())/2000;
            } else if(arrSize-1 == i) {
                from = (allGeoPoints.get(i-1).getTrack_km() +
                    allGeoPoints.get(i).getTrack_km())/2000;
                till = Integer.MAX_VALUE;

            } else {
                till = allGeoPoints.get(i).getTrack_km();
            }

            allGeoPoints.get(i).setColour(geoService.getGleisColour(id, from, till));
            System.out.println(i);
        }
        return allGeoPoints;
    }
}
