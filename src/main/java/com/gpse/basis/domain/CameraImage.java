package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "CameraImages")
public class CameraImage {

    @MongoId
    private String id;
    private int track_id;
    private String path;

    private int type;

    public CameraImage(int track_id, String path, int t) {
        this.track_id = track_id;
        this.path = path;
        this.type = t;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
