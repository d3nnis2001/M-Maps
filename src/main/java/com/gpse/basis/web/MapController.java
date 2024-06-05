package com.gpse.basis.web;

import com.gpse.basis.domain.GeoData;
import com.gpse.basis.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import com.gpse.basis.services.RosBagService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/map")
public class MapController {

    @Autowired
    private FileService file;

    private RosBagService rosBag;
    public MapController(FileService file, RosBagService ros) {
        this.file = file;
        this.rosBag = ros;
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

    @PostMapping("/getCameraImageforTrack")
    public List<String> getCameraImageForTrackRequest(final WebRequest request) {
        int trackId = Integer.parseInt(request.getParameter("trackid"));
        return rosBag.getImagesForTrack(trackId);
    }

    @PostMapping("/getIRCameraImageforTrack")
    public List<String> getIRCameraImageforTrack(final WebRequest request) {
        int trackId = Integer.parseInt(request.getParameter("trackid"));
        return rosBag.getIRImagesForTrack(trackId);
    }

}
