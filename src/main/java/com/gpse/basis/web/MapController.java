package com.gpse.basis.web;

import com.gpse.basis.domain.GeoData;
import com.gpse.basis.domain.Utils;
import com.gpse.basis.services.DataService;
import com.gpse.basis.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;

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
    public List<ResponseColor> getPartTrack(final WebRequest request) {
        int from = Integer.parseInt(request.getParameter("from"));
        int till = Integer.parseInt(request.getParameter("till"));
        List<Map.Entry<DataService.Colors, String>> lst = file.getPartGeoData(from, till);
        List<ResponseColor> k = new ArrayList<>(lst.size());
        for(int i = 0; i < lst.size(); ++i)
            k.add(i, new ResponseColor(lst.get(i).getValue(), lst.get(i).getKey()));
        return k;
    }

    @PostMapping("getpartofheatmap")
    public List<ResponseColor> getPartOfHeatmap(final WebRequest request) {
        Utils util = new Utils();
        String strecke = request.getParameter("strecke");
        String from = request.getParameter("from");
        String till = request.getParameter("till");
        Date fromDate = util.transformString(from);
        Date tillDate = util.transformString(till);
        List<Map.Entry<DataService.Colors, String>> lst = file.getPartHeatmap(Integer.parseInt(strecke), fromDate, tillDate);
        List<ResponseColor> k = new ArrayList<>(lst.size());
        for(int i = 0; i < lst.size(); ++i)
            k.add(i, new ResponseColor(lst.get(i).getValue(), lst.get(i).getKey()));
        return k;
    }

    private class ResponseColor {
        public String id;
        public int color;

        public ResponseColor(String id, DataService.Colors color) {
            this.id = id;
            if(color == DataService.Colors.NORMAL)
                this.color = 0;
            else if(color == DataService.Colors.LOW)
                this.color = 1;
            else if(color == DataService.Colors.MEDIUM)
                this.color = 2;
            else if(color == DataService.Colors.HIGH)
                this.color = 3;
        }
    }
}
