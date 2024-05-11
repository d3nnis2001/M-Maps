package com.gpse.basis.web;

import com.fasterxml.jackson.core.util.RequestPayload;
import com.gpse.basis.domain.DataSet;
import com.gpse.basis.services.FileService;
import com.gpse.basis.domain.FileUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FileController {

    private FileService service;


    @Autowired
    public FileController(FileService service) {
        this.service = service;
    }

    @PostMapping("/uploadFiles")
    public ResponseEntity<List<FileUploadResponse>> importFiles(@RequestParam("paths") List<String> pt,
                                                                @RequestParam("ids") List<String> ids) {

        return ResponseEntity.ok(service.handleImport(pt, ids));
    }

    @GetMapping("/getFiles")
    public List<DataSet> getFilesById(@RequestParam("search") String searchString) {
        return service.getDataSets(searchString);
    }

    @DeleteMapping("/deleteFiles")
    public void deleteFiles(@RequestParam("ids[]") List<String> ids) {
            service.deleteDataSetsById(ids);
    }

    @GetMapping("/getFolder")
    public  ResponseEntity<List<List<String>>> getFolder(@RequestParam("p") String pt) {
        System.out.println(pt);
        try {
             return ResponseEntity.ok(service.readFoler(pt));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ArrayList<>());
        }
    }
}
