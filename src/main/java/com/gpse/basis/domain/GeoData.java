package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * The type Geo data.
 */
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

    /**
     * Instantiates a new Geo data.
     *
     * @param strecken_id the strecken id
     * @param longitude   the longitude
     * @param latitude    the latitude
     * @param track_km    the track km
     * @param id          the id
     */
    public GeoData(int strecken_id, double longitude, double latitude, double track_km, String id) {
        this.strecken_id = strecken_id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.track_km = track_km;
        this.dataSetid = id;
    }

    /**
     * Gets data setid.
     *
     * @return the data setid
     */
    public String getDataSetid() {
        return dataSetid;
    }

    /**
     * Sets data setid.
     *
     * @param dataSetid the data setid
     */
    public void setDataSetid(String dataSetid) {
        this.dataSetid = dataSetid;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets track km.
     *
     * @param track_km the track km
     */
    public void setTrack_km(Double track_km) {
        this.track_km = track_km;
    }

    /**
     * Gets strecken id.
     *
     * @return the strecken id
     */
    public int getStrecken_id() {
        return strecken_id;
    }

    /**
     * Sets strecken id.
     *
     * @param strecken_id the strecken id
     */
    public void setStrecken_id(int strecken_id) {
        this.strecken_id = strecken_id;
    }

    /**
     * Gets longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets longitude.
     *
     * @param longitude the longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets latitude.
     *
     * @param latitude the latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets track km.
     *
     * @return the track km
     */
    public double getTrack_km() {
        return track_km;
    }

    /**
     * Sets track km.
     *
     * @param track_km the track km
     */
    public void setTrack_km(double track_km) {
        this.track_km = track_km;
    }

}
