package com.gpse.basis.domain;

public class CheckPoint {
    private String name;
    private boolean done;

    public CheckPoint(String name) {
        this.name = name;
        this.done = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void switchDone() {
        this.done = !this.done;
    }
}
