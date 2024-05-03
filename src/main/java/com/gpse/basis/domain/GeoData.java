package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "GeoTrackData")
public class GeoData {
    private int strecken_id;
    private double longitude;
    @MongoId
    private double latitude;
    private int track_km;
    public GeoData(int strecken_id, double longitude, double latitude, int track_km) {
        this.strecken_id = strecken_id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.track_km = track_km;
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

    public int getTrack_km() {
        return track_km;
    }

    public void setTrack_km(int track_km) {
        this.track_km = track_km;
    }

}
