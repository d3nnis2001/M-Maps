package com.gpse.basis.web;

import com.gpse.basis.services.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

/**
 * Controller Fpr Images.
 */
@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/images")
public class ImageController {

    private ImageService imageService;
    @Autowired
    public ImageController(final ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Foto hochladen.
     * @param request - Anfrage
     */
    @Operation(summary = "Foto hochladen",
        description = "Mithilfe einer OrderId wird ein Foto hochgeladen")
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

    /**
     * Foto herunterladen.
     * @param request - Anfrage
     * @return String
     */
    @Operation(summary = "Foto aus der Datenbank holen",
        description = "Mithilfe einer angegebenen Id wird ein Bild aus der Datenbank geholt")
    @GetMapping("/download")
    public String getImage(final WebRequest request) {
        String image = imageService.getImage(request.getParameter("inspectionOrderId"));
        return image;
    }

}
