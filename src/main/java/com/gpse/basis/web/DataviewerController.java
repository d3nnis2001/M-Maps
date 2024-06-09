package com.gpse.basis.web;

import com.gpse.basis.domain.DataSet;
import com.gpse.basis.domain.GleisLageDatenpunkt;
import com.gpse.basis.domain.UserModel;
import com.gpse.basis.services.FileService;
import com.gpse.basis.services.UserServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/dataviewer")
public class DataviewerController {
    public final FileService fileService;
    private final UserServices userServices;

    public DataviewerController(FileService fileService, UserServices userServices) {
        this.fileService = fileService;
        this.userServices = userServices;
    }
    /*
    @GetMapping("/getTrackData")
    public ArrayList<DataSet> getAllTrackLayoutData() { return fileService.getAllTrackData}
    */

    @GetMapping("/getTrackData")
    public ArrayList<GleisLageDatenpunkt> getAllTrackLayoutData() { return fileService.getTrackData(6100);}

    @GetMapping("/getTrackData2")
    public ArrayList<GleisLageDatenpunkt> getAllTrackLayoutData2(@RequestParam int trackId) { return fileService.getData(trackId);}

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
