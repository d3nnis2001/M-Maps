package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.util.ArrayList;
import java.util.Date;

@Document(collection = "Reparaturauftrag")
public class Reparatur {
    @MongoId
    private String id;
    private int track;
    private Date from;
    private Date till;
    private ReparaturChecklist checklist;
    private String remarks;
    private String status;
    private String freigabeberechtigter;

    public Reparatur(String id, int track, Date from, Date till, ReparaturChecklist checklist, String remarks, String status, String freigabeberechtigter) {
        this.id = id;
        this.track = track;
        this.from = from;
        this.till = till;
        this.checklist = checklist;
        this.remarks = remarks;
        this.status = status;
        this.freigabeberechtigter = freigabeberechtigter;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTill() {
        return till;
    }

    public void setTill(Date till) {
        this.till = till;
    }

    public ReparaturChecklist getChecklist() {
        return checklist;
    }

    public void setChecklist(ReparaturChecklist checklist) {
        this.checklist = checklist;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFreigabeberechtigter() {
        return freigabeberechtigter;
    }

    public void setFreigabeberechtigter(String freigabeberechtigter) {
        this.freigabeberechtigter = freigabeberechtigter;
    }
}
