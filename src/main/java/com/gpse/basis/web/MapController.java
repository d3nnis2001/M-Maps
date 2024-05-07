package com.gpse.basis.web;

import com.gpse.basis.domain.GeoData;
import com.gpse.basis.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/map")
public class MapController {

    @Autowired
    private FileService file;

    public MapController(FileService file) {
        this.file = file;
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
}
