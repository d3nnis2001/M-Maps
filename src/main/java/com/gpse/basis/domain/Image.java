package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "Images")
public class Image {

    @MongoId
    private String imageId;
    private String orderId;
    // Id of repair order or inspection order
    private String name;
    private String image;
    // in base64

    public Image(String imageId, String orderId, String image, String name) {
        this.imageId = imageId;
        this.orderId = orderId;
        this.image = image;
        this.name = name;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
