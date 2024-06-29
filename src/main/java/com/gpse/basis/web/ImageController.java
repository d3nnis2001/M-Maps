package com.gpse.basis.web;

import com.gpse.basis.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/images")
public class ImageController {

    private ImageService imageService;
    @Autowired
    public ImageController(final ImageService imageService) {
        this.imageService = imageService;
    }
    @PostMapping("/upload")
    public void uploadPhoto(final WebRequest request) {
        String orderId = request.getParameter("orderId");
        String image = request.getParameter("image");
        String name = request.getParameter("name");
        System.out.println("C-orderId: " + orderId);
        System.out.println("C-name: " + name);
        if (image != null) {
            System.out.println("C-image: Success");
        }
        imageService.saveImage(orderId, image, name);
    }



    @GetMapping("/download")
    public void getPhoto(final WebRequest request) {

    }

}
