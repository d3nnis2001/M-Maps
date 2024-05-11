package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "GleisLageDaten")
public class GleisLageDatenpunkt {
    @MongoId
    String id;
    private double str_km;
    private  double time_unix;
    private double geschwindigkeit;
    private double z_links_railab_3p;
    private double z_rechts_railab_3p;
    private String dataSetid;

    public GleisLageDatenpunkt(double str_km, double time_unix, double geschwindigkeit, double z_links_railab_3p, double z_rechts_railab_3p, String dataSetid) {
        this.str_km = str_km;
        this.time_unix = time_unix;
        this.geschwindigkeit = geschwindigkeit;
        this.z_links_railab_3p = z_links_railab_3p;
        this.z_rechts_railab_3p = z_rechts_railab_3p;
        this.dataSetid = dataSetid;
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
