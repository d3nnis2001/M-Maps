package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * The type Gleis lage range.
 */
@Document(collection = "GleisLageRange")
public class GleisLageRange {
    /**
     * The enum Level.
     */
    public enum Level {
        /**
         * Sra level.
         */
        SRA,
        /**
         * Sr 100 level.
         */
        SR100,
        /**
         * Srlim level.
         */
        SRLIM
    }

    @MongoId
    private String id;
    private Level l;

    private double _smaller80_;
    private double _80to120_;
    private double _120to160_;
    private double _160to230_;
    private double _greater230_;

    /**
     * Instantiates a new Gleis lage range.
     *
     * @param l            the l
     * @param _smaller80_  the smaller 80
     * @param _80to120_    the 80 to 120
     * @param _120to160_   the 120 to 160
     * @param _160to230_   the 160 to 230
     * @param _greater230_ the greater 230
     */
    public GleisLageRange(Level l, double _smaller80_, double _80to120_, double _120to160_,
                          double _160to230_, double _greater230_) {
        this.l = l;
        this._smaller80_ = _smaller80_;
        this._80to120_ = _80to120_;
        this._120to160_ = _120to160_;
        this._160to230_ = _160to230_;
        this._greater230_ = _greater230_;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Level getL() {
        return l;
    }

    public void setL(Level l) {
        this.l = l;
    }

    public double get_smaller80_() {
        return _smaller80_;
    }

    public void set_smaller80_(double _smaller80_) {
        this._smaller80_ = _smaller80_;
    }

    public double get_80to120_() {
        return _80to120_;
    }

    public void set_80to120_(double _80to120_) {
        this._80to120_ = _80to120_;
    }

    public double get_120to160_() {
        return _120to160_;
    }

    public void set_120to160_(double _120to160_) {
        this._120to160_ = _120to160_;
    }

    public double get_160to230_() {
        return _160to230_;
    }

    public void set_160to230_(double _160to230_) {
        this._160to230_ = _160to230_;
    }

    public double get_greater230_() {
        return _greater230_;
    }

    public void set_greater230_(double _greater230_) {
        this._greater230_ = _greater230_;
    }

    /**
     * Gets value to velocity.
     *
     * @param vel the vel
     * @return the value to velocity
     */
    public double getValueToVelocity(int vel) {
        if (vel <= 80) {
            return _smaller80_;
        } else if (vel >= 80 && vel <= 120) {
            return _80to120_;
        } else if (vel >= 120 && vel <= 160) {
            return _120to160_;
        } else if (vel >= 160 && vel <= 230) {
            return _160to230_;
        } else {
            return _greater230_;
        }
    }
}
