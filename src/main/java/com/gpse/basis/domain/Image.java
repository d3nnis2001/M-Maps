package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "Pictures")
public class Image {

    @MongoId
    private String imageId;
    private String orderId;
    // Id of repair order or inspection order
    private String name;
    private String type;
    private byte[] imageData;

    public Image(String imageId, String orderId, String name, String type, byte[] imageData) {
        this.imageId = imageId;
        this.orderId = orderId;
        this.name = name;
        this.type = type;
        this.imageData = imageData;
    }

    public String getImageId() {
        return imageId;
    }

    public void setId(String id) {
        this.imageId = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
