package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "RepairChecklist")
public class RepairChecklist {
    private int id;
    private Checklist check;
    private ArrayList<Boolean> selected = new ArrayList<>();

    public RepairChecklist(int id, Checklist check, ArrayList<Boolean> selected) {
        this.id = id;
        this.check = check;
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Checklist getCheck() {
        return check;
    }

    public void setCheck(Checklist check) {
        this.check = check;
    }

    public ArrayList<Boolean> getSelected() {
        return selected;
    }

    public void setSelected(ArrayList<Boolean> selected) {
        this.selected = selected;
    }
    public int getChecklistSize() {
        return check.getItemSize();
    }
}
