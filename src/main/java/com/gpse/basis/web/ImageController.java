package com.gpse.basis.web;

import com.gpse.basis.domain.Image;
import com.gpse.basis.services.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPhoto(@RequestParam("orderId") String orderId,
                                              @RequestParam("file") MultipartFile[] files) {
        try {
            for (MultipartFile file : files) {
                String name = file.getOriginalFilename();
                byte[] bytes = file.getBytes();
                // Hier kann die Datei gespeichert oder weiter verarbeitet werden
                System.out.println("Received file: " + name);
                // imageService.saveImage(orderId, bytes);
            }
            return new ResponseEntity<>(orderId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error uploading files", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/download")
    public ResponseEntity<Image> getPhoto(@PathVariable String id) {
        Optional<Image> image = imageService.getImage(id);
        if (image.isPresent()) {
            return new ResponseEntity<>(image.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
