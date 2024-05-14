package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;

@Document(collection = "Checklist")
public class Checklist {
    @MongoId
    private String name;
    private ArrayList<String> items;
    private boolean isMaterialList;

    public Checklist(String name, ArrayList<String> items, boolean isMaterialList) {
        this.name = name;
        this.items = items;
        this.isMaterialList = isMaterialList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }
    public void addItem(String item) {
        items.add(item);
    }
    public int getItemSize() {
        return items.size();
    }

    public boolean isMaterialList() {
        return isMaterialList;
    }

    public void setMaterialList(boolean materialList) {
        isMaterialList = materialList;
    }
}
