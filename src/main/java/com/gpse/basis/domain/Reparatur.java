package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;

@Document(collection = "Reparaturauftrag")
public class Reparatur {
    @MongoId
    private String id;
    private int track;
    private LocalDate from;
    private LocalDate till;
    private ReparaturChecklist checklist;
    private String remarks;
    private String status;
    private String freigabeberechtigter;
    private GeoCords geocords;
    private boolean archived = false;

    public Reparatur(String id, int track, LocalDate from, LocalDate till, ReparaturChecklist checklist, String remarks, String status, String freigabeberechtigter) {
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

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTill() {
        return till;
    }

    public void setTill(LocalDate till) {
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
    public GeoCords getGeocords() {
        return geocords;
    }
    public void setGeocords(GeoCords geocords) {
        this.geocords = geocords;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
