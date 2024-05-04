package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "Checklist")
public class Checklist {
    private String id;
    private String name;
    private ArrayList<String> items;

    public Checklist(String id, String name, ArrayList<String> items) {
        this.id = id;
        this.name = name;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
