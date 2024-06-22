package com.gpse.basis.services;

import com.gpse.basis.domain.Image;
import com.gpse.basis.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

@Service
public class ImageServiceImpl implements ImageService {

    private ImageRepository imRep;

    @Autowired
    public ImageServiceImpl(ImageRepository im) {
        this.imRep = im;
    }
    @Override
    public void saveImage(String orderId, MultipartFile file) throws IOException {
        String newImageId = generateId();
        Image image = new Image(newImageId, orderId, file.getOriginalFilename(), file.getContentType(), file.getBytes());
        imRep.save(image);
    }

    public String generateId() {
        long timestamp = System.currentTimeMillis();
        Random random = new Random();
        int randomValue = random.nextInt(1000);
        return String.valueOf("image-" + timestamp + randomValue);
    }

    @Override
    public Optional<Image> getImage(String id) {
        return imRep.findById(id);
    }
}
