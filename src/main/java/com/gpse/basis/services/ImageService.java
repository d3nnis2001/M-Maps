package com.gpse.basis.services;

public interface ImageService {

    void saveImage(String oderId, String image, String name);
    String getImage(String id);
}
