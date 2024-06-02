package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "CameraImages")
public class CameraImage {
    @MongoId
    private String id;
    private int track_id;
    private String path;

    public CameraImage(int track_id, String path) {
        this.track_id = track_id;
        this.path = path;
    }

    public CameraImage() {

    }

    public CameraImage(String id, int track_id, String path) {
        this.id = id;
        this.track_id = track_id;
        this.path = path;
    }

    public int getTrack_id() {
        return track_id;
    }

    public void setTrack_id(int track_id) {
        this.track_id = track_id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
