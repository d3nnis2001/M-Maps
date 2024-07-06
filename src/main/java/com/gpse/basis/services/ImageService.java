package com.gpse.basis.services;

/**
 * Interface für den ImageService.
 */
public interface ImageService {

    void saveImage(String oderId, String image, String name);
    String getImage(String id);
}
