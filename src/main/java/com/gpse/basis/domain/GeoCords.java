package com.gpse.basis.domain;

/**
 * The type Geo cords.
 */
public class GeoCords {
    private String latitude;
    private String longitude;

    /**
     * Instantiates a new Geo cords.
     *
     * @param latitude  the latitude
     * @param longitude the longitude
     */
    public GeoCords(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets latitude.
     *
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Sets latitude.
     *
     * @param latitude the latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets longitude.
     *
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Sets longitude.
     *
     * @param longitude the longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
