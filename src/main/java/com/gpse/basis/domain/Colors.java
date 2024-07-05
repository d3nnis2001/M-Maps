package com.gpse.basis.domain;

/**
 * This is the class that stores all the configured project colors.
 */
public class Colors {
    private String primary;
    private String accent;
    private String positive;
    private String negative;
    private String warning;
    private String info;
    private String dark;

    /**
     * This is the Constructor to initialize all color values.
     * @param primary primary color
     * @param accent accent color
     * @param positive positive color
     * @param negative negative color
     * @param warning warning color
     * @param dark dark color
     * @param info info color
     */
    public Colors(String primary, String accent, String positive,
                  String negative, String warning, String dark, String info) {
        this.primary = primary;
        this.accent = accent;
        this.positive = positive;
        this.negative = negative;
        this.warning = warning;
        this.info = info;
        this.dark = dark;
    }
    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getAccent() {
        return accent;
    }

    public void setAccent(String accent) {
        this.accent = accent;
    }

    public String getPositive() {
        return positive;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }

    public String getNegative() {
        return negative;
    }

    public void setNegative(String negative) {
        this.negative = negative;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDark() {
        return dark;
    }

    public void setDark(String dark) {
        this.dark = dark;
    }
}
