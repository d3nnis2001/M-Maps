package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * This class represents the settings of the application, i.e. the settings, the logo and the colors of the application.
 */
@Document(collection = "settings")
public class Settings {
    @MongoId
    private String id;
    private String impressum;
    private Colors colors;

    public Settings(String impressum, Colors colors) {
        this.impressum = impressum;
        this.colors = colors;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImpressum() {
        return impressum;
    }

    public void setImpressum(String impressum) {
        this.impressum = impressum;
    }
    public Colors getColors() {
        return colors;
    }

    public void setColors(Colors colors) {
        this.colors = colors;
    }
}
