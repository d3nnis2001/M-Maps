package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;

@Document(collection = "Checklist")
public class Checklist {
    @MongoId
    private String name;
    private ArrayList<String> items;
    private ArrayList<Boolean> selected;

    public Checklist(String name, ArrayList<String> items, ArrayList<Boolean> selected) {
        this.name = name;
        this.items = items;
        this.selected = selected;
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

    public ArrayList<Boolean> getSelected() {
        return selected;
    }
    public void setSelected(ArrayList<Boolean> selected) {
        this.selected = selected;
    }
}
