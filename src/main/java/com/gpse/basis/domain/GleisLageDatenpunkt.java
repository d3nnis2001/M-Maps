package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * The type Gleis lage datenpunkt.
 */
@Document(collection = "GleisLageDaten")
public class GleisLageDatenpunkt {
    @MongoId
    private String id;
    private double str_km;
    private  double time_unix;
    private double geschwindigkeit;
    private double z_links_railab_3p;
    private double z_rechts_railab_3p;
    private String dataSetid;

    @Indexed
    private String iDlocation;

    private int v_zul;

    /**
     * Instantiates a new Gleis lage datenpunkt.
     *
     * @param str_km             the str km
     * @param time_unix          the time unix
     * @param geschwindigkeit    the geschwindigkeit
     * @param z_links_railab_3p  the z links railab 3 p
     * @param z_rechts_railab_3p the z rechts railab 3 p
     * @param dataSetid          the data setid
     * @param location           the location
     * @param v_zul              the v zul
     */
    public GleisLageDatenpunkt(double str_km, double time_unix, double geschwindigkeit,
                               double z_links_railab_3p, double z_rechts_railab_3p, String dataSetid,
                               String location, int v_zul) {
        this.str_km = str_km;
        this.time_unix = time_unix;
        this.geschwindigkeit = geschwindigkeit;
        this.z_links_railab_3p = z_links_railab_3p;
        this.z_rechts_railab_3p = z_rechts_railab_3p;
        this.dataSetid = dataSetid;
        this.iDlocation = location;
        this.v_zul = v_zul;
    }

    /**
     * Instantiates a new Gleis lage datenpunkt.
     *
     * @param id                 the id
     * @param str_km             the str km
     * @param time_unix          the time unix
     * @param geschwindigkeit    the geschwindigkeit
     * @param z_links_railab_3p  the z links railab 3 p
     * @param z_rechts_railab_3p the z rechts railab 3 p
     * @param dataSetid          the data setid
     * @param iDlocation         the dlocation
     * @param v_zul              the v zul
     */
    public GleisLageDatenpunkt(String id, double str_km, double time_unix, double geschwindigkeit,
                               double z_links_railab_3p, double z_rechts_railab_3p, String dataSetid,
                               String iDlocation, int v_zul) {
        this.id = id;
        this.str_km = str_km;
        this.time_unix = time_unix;
        this.geschwindigkeit = geschwindigkeit;
        this.z_links_railab_3p = z_links_railab_3p;
        this.z_rechts_railab_3p = z_rechts_railab_3p;
        this.dataSetid = dataSetid;
        this.iDlocation = iDlocation;
        this.v_zul = v_zul;
    }
    public GleisLageDatenpunkt() {
    }


    public String getLocation() {
        return iDlocation;
    }

    public void setLocation(String location) {
        this.iDlocation = location;
    }

    public int getV_zul() {
        return v_zul;
    }

    public void setV_zul(int v_zul) {
        this.v_zul = v_zul;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getStr_km() {
        return str_km;
    }

    public void setStr_km(double str_km) {
        this.str_km = str_km;
    }


    public double getTime_unix() {
        return time_unix;
    }

    public void setTime_unix(double time_unix) {
        this.time_unix = time_unix;
    }

    public double getGeschwindigkeit() {
        return geschwindigkeit;
    }

    public void setGeschwindigkeit(double geschwindigkeit) {
        this.geschwindigkeit = geschwindigkeit;
    }

    public double getZ_links_railab_3p() {
        return z_links_railab_3p;
    }

    public void setZ_links_railab_3p(double z_links_railab_3p) {
        this.z_links_railab_3p = z_links_railab_3p;
    }

    public double getZ_rechts_railab_3p() {
        return z_rechts_railab_3p;
    }

    public void setZ_rechts_railab_3p(double z_rechts_railab_3p) {
        this.z_rechts_railab_3p = z_rechts_railab_3p;
    }

    public String getDataSetid() {
        return dataSetid;
    }

    public void setDataSetid(String dataSetid) {
        this.dataSetid = dataSetid;
    }


}
