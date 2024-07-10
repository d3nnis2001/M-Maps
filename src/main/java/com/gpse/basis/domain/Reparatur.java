package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;

/**
 * The type Reparatur.
 */
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

    /**
     * Instantiates a new Reparatur.
     *
     * @param id                   the id
     * @param track                the track
     * @param from                 the from
     * @param till                 the till
     * @param checklist            the checklist
     * @param remarks              the remarks
     * @param status               the status
     * @param freigabeberechtigter the freigabeberechtigter
     */
    public Reparatur(String id, int track, LocalDate from, LocalDate till, ReparaturChecklist checklist,
                     String remarks, String status, String freigabeberechtigter) {
        this.id = id;
        this.track = track;
        this.from = from;
        this.till = till;
        this.checklist = checklist;
        this.remarks = remarks;
        this.status = status;
        this.freigabeberechtigter = freigabeberechtigter;
    }

    /**
     * Gets track.
     *
     * @return the track
     */
    public int getTrack() {
        return track;
    }

    /**
     * Sets track.
     *
     * @param track the track
     */
    public void setTrack(int track) {
        this.track = track;
    }

    /**
     * Gets from.
     *
     * @return the from
     */
    public LocalDate getFrom() {
        return from;
    }

    /**
     * Sets from.
     *
     * @param from the from
     */
    public void setFrom(LocalDate from) {
        this.from = from;
    }

    /**
     * Gets till.
     *
     * @return the till
     */
    public LocalDate getTill() {
        return till;
    }

    /**
     * Sets till.
     *
     * @param till the till
     */
    public void setTill(LocalDate till) {
        this.till = till;
    }

    /**
     * Gets checklist.
     *
     * @return the checklist
     */
    public ReparaturChecklist getChecklist() {
        return checklist;
    }

    /**
     * Sets checklist.
     *
     * @param checklist the checklist
     */
    public void setChecklist(ReparaturChecklist checklist) {
        this.checklist = checklist;
    }

    /**
     * Gets remarks.
     *
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets remarks.
     *
     * @param remarks the remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets freigabeberechtigter.
     *
     * @return the freigabeberechtigter
     */
    public String getFreigabeberechtigter() {
        return freigabeberechtigter;
    }

    /**
     * Sets freigabeberechtigter.
     *
     * @param freigabeberechtigter the freigabeberechtigter
     */
    public void setFreigabeberechtigter(String freigabeberechtigter) {
        this.freigabeberechtigter = freigabeberechtigter;
    }

    /**
     * Gets geocords.
     *
     * @return the geocords
     */
    public GeoCords getGeocords() {
        return geocords;
    }

    /**
     * Sets geocords.
     *
     * @param geocords the geocords
     */
    public void setGeocords(GeoCords geocords) {
        this.geocords = geocords;
    }

    /**
     * Is archived boolean.
     *
     * @return the boolean
     */
    public boolean isArchived() {
        return archived;
    }

    /**
     * Sets archived.
     *
     * @param archived the archived
     */
    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
