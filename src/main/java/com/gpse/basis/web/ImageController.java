package com.gpse.basis.web;

import com.gpse.basis.domain.Image;
import com.gpse.basis.services.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPhoto(@RequestParam("orderId") String orderId, @RequestParam("file") MultipartFile file) {
        try {
            imageService.saveImage(orderId, file);
            return new ResponseEntity<>(orderId, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
