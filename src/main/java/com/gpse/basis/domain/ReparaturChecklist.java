package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.util.ArrayList;

@Document(collection = "RepChecklist")
public class ReparaturChecklist {
    @MongoId
    private String id;
    private Checklist checkPoints;
    private ArrayList<String> checkSel;
    @Field
    private LocalDate terminiert;

    public ReparaturChecklist(String id, Checklist checkPoints, ArrayList<String> checkSel) {
        this.id = id;
        this.checkPoints = checkPoints;
        this.checkSel = checkSel;
    }

    public Checklist getCheckPoints() {
        return checkPoints;
    }

    public void setCheckPoints(Checklist checkPoints) {
        this.checkPoints = checkPoints;
    }

    public ArrayList<String> getCheckSel() {
        return checkSel;
    }

    public void setCheckSel(ArrayList<String> checkSel) {
        this.checkSel = checkSel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getTerminiert() {
        return terminiert;
    }

    public void setTerminiert(LocalDate terminiert) {
        this.terminiert = terminiert;
    }
}
