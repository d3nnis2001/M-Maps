package com.gpse.basis.domain;

/**
 * The type Velodyne point.
 */
public class VelodynePoint {
    private double x;
    private double y;
    private double z;
    private double intensity;

    /**
     * Instantiates a new Velodyne point.
     *
     * @param x         the x
     * @param y         the y
     * @param z         the z
     * @param intensity the intensity
     */
    public VelodynePoint(double x, double y, double z, double intensity) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.intensity = intensity;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }
}
