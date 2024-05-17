package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "Checklist")
public class Checklist {
    private String name;
    private List<CheckPoint>  tasks;
    private List<CheckPoint> material;

    public Checklist(String name, List<String> tasks, List<String> material) {
        this.name = name;
        this.tasks = createCheckPoints(tasks);
        this.material = createCheckPoints(material);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CheckPoint> getTasks() {
        return tasks;
    }

    public void setTasks(List<CheckPoint> tasks) {
        this.tasks = tasks;
    }

    public List<CheckPoint> getMaterial() {
        return material;
    }

    public void setMaterial(List<CheckPoint> material) {
        this.material = material;
    }

    public List<CheckPoint> createCheckPoints(List<String> points) {
        List<CheckPoint> result = new ArrayList<>();
        for (String point: points) {
            result.add(new CheckPoint(point));
        }
        return result;
    }
}
