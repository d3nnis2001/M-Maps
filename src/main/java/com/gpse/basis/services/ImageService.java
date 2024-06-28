package com.gpse.basis.services;

public interface ImageService {

    void saveImage(String oderId, String image);
    String getImage(String id);
}
