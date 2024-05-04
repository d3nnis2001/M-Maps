package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.Date;

@Document(collection = "Reparaturauftrag")
public class Reparatur {
    @MongoId
    private String id;
    private int track;
    private Date from;
    private Date till;
    private ArrayList<String> checklist;
    private String remarks;

    public Reparatur(String id, int track, Date from, Date till, ArrayList<String> checklist, String remarks) {
        this.id = id;
        this.track = track;
        this.from = from;
        this.till = till;
        this.checklist = checklist;
        this.remarks = remarks;
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

    public ArrayList<String> getChecklist() {
        return checklist;
    }

    public void setChecklist(ArrayList<String> checklist) {
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
}
