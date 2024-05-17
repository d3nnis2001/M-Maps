package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document(collection = "ChecklistTemplate")
public class ChecklistTemplate {
    @MongoId
    private String name;
    private List<String> tasks;
    private List<String> material;

    public ChecklistTemplate(String name, List<String> tasks, List<String> material) {
        this.name = name;
        this.tasks = tasks;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }

    public List<String> getMaterial() {
        return material;
    }

    public void setMaterial(List<String> material) {
        this.material = material;
    }
}
