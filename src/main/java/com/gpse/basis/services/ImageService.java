package com.gpse.basis.services;

/**
 * Interface für den ImageService.
 */
import com.gpse.basis.domain.Image;

import java.util.List;

public interface ImageService {

    void saveImage(String oderId, String image, String name);

    String getImage(String id);

    List<Image> getImagesForOrder(String orderId);
}
