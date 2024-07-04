package com.gpse.basis.domain;

public class Colors {
    private String primary;
    private String secondary;
    public Colors(String primary, String secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }
    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }
}
