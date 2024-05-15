package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Document(collection = "RepairChecklist")
public class RepairChecklist {
    private int id;
    private Checklist check;
    private ArrayList<Boolean> selected = new ArrayList<>();
    private Checklist material;
    private ArrayList<Boolean> selectedMat = new ArrayList<>();

    public RepairChecklist(int id, Checklist check, ArrayList<Boolean> selected, Checklist material, ArrayList<Boolean> selectedMat) {
        this.id = id;
        this.check = check;
        this.selected = selected;
        this.material = material;
        this.selectedMat = selectedMat;

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

    public Checklist getMaterial() {
        return material;
    }

    public void setMaterial(Checklist material) {
        this.material = material;
    }

    public ArrayList<Boolean> getSelectedMat() {
        return selectedMat;
    }

    public void setSelectedMat(ArrayList<Boolean> selectedMat) {
        this.selectedMat = selectedMat;
    }
}
