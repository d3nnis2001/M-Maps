package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * The type Camera image.
 */
@Document(collection = "CameraImages")
public class CameraImage {

    @MongoId
    private String id;
    private int track_id;
    private String path;

    private int type;

    private int index;

    private String dataSetid;

    /**
     * Instantiates a new Camera image.
     *
     * @param track_id the track id
     * @param path     the path
     * @param t        the t
     * @param dId      the d id
     * @param index    the index
     */
    public CameraImage(int track_id, String path, int t, String dId, int index) {
        this.track_id = track_id;
        this.path = path;
        this.type = t;
        this.dataSetid = dId;
        this.index = index;
    }

    /**
     * Instantiates a new Camera image.
     *
     * @param id       the id
     * @param track_id the track id
     * @param path     the path
     */
    public CameraImage(String id, int track_id, String path) {
        this.id = id;
        this.track_id = track_id;
        this.path = path;
    }

    public CameraImage() {

    }

    public String getDataSetid() {
        return dataSetid;
    }

    public void setDataSetid(String dataSetid) {
        this.dataSetid = dataSetid;
    }
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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
