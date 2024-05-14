package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
public class InspectionOrder {
    @MongoId
    private String inspectionOrderId;
    private String streckenId;
    private String userId;
    private String startOrt;
    private String endOrt;
    private String startZeit;
    private String endZeit;
    private String fachabteilung;
    private String messdaten;
    private String status;
    private String bemerkungen;
    private boolean archiviert;

    public InspectionOrder(String streckenId, String userId, String start, String end, String startTime, String endTime,
                           String fachabteilung, String messdaten, String status, String bemerkungen,
                           boolean archiviert) {
        this.streckenId = streckenId;
        this.userId = userId;
        this.startOrt = start;
        this.endOrt = end;
        this.startZeit = startTime;
        this.endZeit = endTime;
        this.fachabteilung = fachabteilung;
        this.messdaten = messdaten;
        this.status = status;
        this.bemerkungen = bemerkungen;
        this.archiviert = archiviert;
    }

    public InspectionOrder() {

    }
    public String getInspectionOrderId() {
        return inspectionOrderId;
    }

    public void setInspectionOrderId(String inspectionOrderId) {
        this.inspectionOrderId = inspectionOrderId;
    }
    public String getStreckenId() {
        return streckenId;
    }

    public void setStreckenId(String id) {
        this.streckenId = id;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getStartOrt() {
        return startOrt;
    }

    public void setStartOrt(String startOrt) {
        this.startOrt = startOrt;
    }

    public String getEndOrt() {
        return endOrt;
    }

    public void setEndOrt(String endOrt) {
        this.endOrt = endOrt;
    }

    public String getStartZeit() {
        return startZeit;
    }

    public void setStartZeit(String startZeit) {
        this.startZeit = startZeit;
    }

    public String getEndZeit() {
        return endZeit;
    }

    public void setEndZeit(String endZeit) {
        this.endZeit = endZeit;
    }

    public String getFachabteilung() {
        return fachabteilung;
    }

    public void setFachabteilung(String fachabteilung) {
        this.fachabteilung = fachabteilung;
    }

    public String getMessdaten() {
        return messdaten;
    }

    public void setMessdaten(String messdaten) {
        this.messdaten = messdaten;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBemerkungen() {
        return bemerkungen;
    }

    public void setBemerkungen(String bemerkungen) {
        this.bemerkungen = bemerkungen;
    }

    public boolean isArchiviert() {
        return archiviert;
    }

    public void setArchiviert(boolean archiviert) {
        this.archiviert = archiviert;
    }
}
