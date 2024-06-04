package com.gpse.basis.web;

import com.gpse.basis.domain.GeoData;
import com.gpse.basis.domain.Utils;
import com.gpse.basis.services.DataService;
import com.gpse.basis.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;

@RestController
@RequestMapping("/api/map")
public class MapController {

    @Autowired
    private final DataService dataService;
    public MapController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/gettracks")
    public ArrayList<GeoData> getAllGeoData() {
        return dataService.getGeoData();
    }


    @PostMapping("/getpartheatmap")
    public List<ResponseColor> getPartOfHeatmap(final WebRequest request) {
        String strecke = request.getParameter("strecke");
        String from = request.getParameter("from");
        String till = request.getParameter("till");
        System.out.println(from);
        LocalDateTime fromDate = LocalDateTime.parse(from);
        LocalDateTime tillDate = LocalDateTime.parse(till);
        System.out.println("Datum: "+fromDate);
        List<Map.Entry<DataService.Colors, String>> lst = dataService.getGeoDataByDate(Integer.parseInt(strecke), fromDate, tillDate);
        List<ResponseColor> k = new ArrayList<>(lst.size());
        for(int i = 0; i < lst.size(); ++i)
            k.add(i, new ResponseColor(lst.get(i).getValue(), lst.get(i).getKey()));
        return k;
    }

    @PostMapping("/getheatmap")
    public List<Map.Entry<DataService.Colors, String>> getHeatmap() {
        return dataService.getHeatmap();
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
