package com.gpse.basis.web;

import com.gpse.basis.domain.Image;
import com.gpse.basis.services.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPhoto(final WebRequest request) {
        String orderId = request.getParameter("orderId");
        
        byte[] file = request.getParameter("file").getBytes();
        String name = request.getParameter("file").toString();
        System.out.println("TEST:" + name);
        System.out.println("Controller-Test1");
        //imageService.saveImage(orderId, file);
        System.out.println("Controller-Test2");
        return new ResponseEntity<>(orderId, HttpStatus.OK);
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
