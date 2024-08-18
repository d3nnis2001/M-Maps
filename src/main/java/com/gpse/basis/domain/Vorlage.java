package com.gpse.basis.domain;

import org.springframework.data.annotation.Id;

/**
 * Klasse für die Email Vorlagen.
 */
public class Vorlage {
    @Id
    private String vorlagenId;
    private String stringSubject;
    private String stringBody;

    /**
     * Konstruktor für die Vorlagen.
     * @param vorlagenId - VorlagenID
     * @param stringSubject - Betreff
     * @param stringBody - Body
     */
    public Vorlage(String vorlagenId, String stringSubject, String stringBody) {
        this.vorlagenId = vorlagenId;
        this.stringSubject = stringSubject;
        this.stringBody = stringBody;
    }

    public String getVorlagenId() {
        return vorlagenId;
    }

    public void setVorlagenId(String vorlagenId) {
        this.vorlagenId = vorlagenId;
    }

    public String getStringSubject() {
        return stringSubject;
    }

    public void setStringSubject(String stringSubject) {
        this.stringSubject = stringSubject;
    }

    public String getStringBody() {
        return stringBody;
    }

    public void setStringBody(String stringBody) {
        this.stringBody = stringBody;
    }
}
