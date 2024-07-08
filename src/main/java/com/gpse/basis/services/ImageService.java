package com.gpse.basis.services;

import com.gpse.basis.domain.Image;

import java.util.List;

public interface ImageService {

    void saveImage(String oderId, String image, String name);

    String getImage(String id);

    List<Image> getImagesForOrder(String orderId);
}
