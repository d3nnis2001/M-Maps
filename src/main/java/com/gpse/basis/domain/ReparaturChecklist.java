package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The type Reparatur checklist.
 */
@Document(collection = "RepChecklist")
public class ReparaturChecklist {
    @MongoId
    private String id;
    private Checklist checkPoints;
    private ArrayList<String> checkSel;
    @Field
    private LocalDate terminiert;

    /**
     * Instantiates a new Reparatur checklist.
     *
     * @param id          the id
     * @param checkPoints the check points
     * @param checkSel    the check sel
     */
    public ReparaturChecklist(String id, Checklist checkPoints, ArrayList<String> checkSel) {
        this.id = id;
        this.checkPoints = checkPoints;
        this.checkSel = checkSel;
    }

    /**
     * Gets check points.
     *
     * @return the check points
     */
    public Checklist getCheckPoints() {
        return checkPoints;
    }

    /**
     * Sets check points.
     *
     * @param checkPoints the check points
     */
    public void setCheckPoints(Checklist checkPoints) {
        this.checkPoints = checkPoints;
    }

    /**
     * Gets check sel.
     *
     * @return the check sel
     */
    public ArrayList<String> getCheckSel() {
        return checkSel;
    }

    /**
     * Sets check sel.
     *
     * @param checkSel the check sel
     */
    public void setCheckSel(ArrayList<String> checkSel) {
        this.checkSel = checkSel;
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
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets terminiert.
     *
     * @return the terminiert
     */
    public LocalDate getTerminiert() {
        return terminiert;
    }

    /**
     * Sets terminiert.
     *
     * @param terminiert the terminiert
     */
    public void setTerminiert(LocalDate terminiert) {
        this.terminiert = terminiert;
    }
}
