package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "GleisVData")
public class GleisVData {
    @MongoId
    String id;
    private int Str_Nr;
    private int Ri;
    private double von_km;
    private double bis_km;
    private int laenge;
    private int v_zul;
    private String datum;
    public GleisVData() {

    }

    public GleisVData(int str, int r, double v1, double v2, double b1, double b2, int l, int v, String d) {
        Str_Nr = str;
        Ri = r;
        von_km = v1 + v2 / 1000;
        bis_km = b1 + b2 / 1000;
        laenge = l;
        v_zul = v;
        datum = d;
    }

    public GleisVData(String id, int str_Nr, int ri, double von_km, double bis_km, int laenge, int v_zul, String datum) {
        this.id = id;
        Str_Nr = str_Nr;
        Ri = ri;
        this.von_km = von_km;
        this.bis_km = bis_km;
        this.laenge = laenge;
        this.v_zul = v_zul;
        this.datum = datum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStr_Nr() {
        return Str_Nr;
    }

    public void setStr_Nr(int str_Nr) {
        Str_Nr = str_Nr;
    }

    public int getRi() {
        return Ri;
    }

    public void setRi(int ri) {
        Ri = ri;
    }

    public double getVon_km() {
        return von_km;
    }

    public void setVon_km(double von_km) {
        this.von_km = von_km;
    }

    public double getBis_km() {
        return bis_km;
    }

    public void setBis_km(double bis_km) {
        this.bis_km = bis_km;
    }

    public int getLaenge() {
        return laenge;
    }

    public void setLaenge(int laenge) {
        this.laenge = laenge;
    }

    public int getV_zul() {
        return v_zul;
    }

    public void setV_zul(int v_zul) {
        this.v_zul = v_zul;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }
}
