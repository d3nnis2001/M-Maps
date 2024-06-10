package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "GeoTrackData")
public class GeoData {
    @MongoId
    private String id;

    @Indexed
    private int strecken_id;
    private double longitude;

    private double latitude;
    @Indexed
    private Double track_km;

    private String dataSetid;

    public String getDataSetid() {
        return dataSetid;
    }

    public void setDataSetid(String dataSetid) {
        this.dataSetid = dataSetid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTrack_km(Double track_km) {
        this.track_km = track_km;
    }

    public GeoData(int strecken_id, double longitude, double latitude, double track_km, String id) {
        this.strecken_id = strecken_id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.track_km = track_km;
        this.dataSetid = id;
    }
    public int getStrecken_id() {
        return strecken_id;
    }

    public void setStrecken_id(int strecken_id) {
        this.strecken_id = strecken_id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getTrack_km() {
        return track_km;
    }

    public void setTrack_km(double track_km) {
        this.track_km = track_km;
    }

}
