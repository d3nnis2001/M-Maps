package com.gpse.basis.web;

import com.gpse.basis.domain.GeoData;
import com.gpse.basis.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

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
}
