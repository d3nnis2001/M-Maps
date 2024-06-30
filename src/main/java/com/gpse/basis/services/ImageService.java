package com.gpse.basis.services;

import com.gpse.basis.domain.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface ImageService {

    void saveImage(String orderId, MultipartFile file) throws IOException;

    Optional<Image> getImage(String id);
}
