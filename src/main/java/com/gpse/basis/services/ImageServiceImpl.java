package com.gpse.basis.services;

import com.gpse.basis.domain.Image;
import com.gpse.basis.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void saveImage(String orderId, String imageString, String name) {
        String newImageId = generateId();
        Image image = new Image(newImageId, orderId, imageString,name);
        System.out.println("Success!!");
        imRep.save(image);
    }

    public String generateId() {
        long timestamp = System.currentTimeMillis();
        Random random = new Random();
        int randomValue = random.nextInt(1000);
        return String.valueOf("image-" + timestamp + randomValue);
    }

    @Override
    public String getImage(String id) {
        Optional<Image> image = imRep.findById(id);

        return image.get().getImage();
    }
}
