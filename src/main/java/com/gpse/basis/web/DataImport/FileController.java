package com.gpse.basis.web.DataImport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<List<FileUploadResponse>> importFiles(@RequestPart("files[]") List<MultipartFile> files,
                                                                @RequestParam("additionalData[]") List<String> streckenId) {
        return ResponseEntity.ok(service.handleImport(files, streckenId));
    }

}
