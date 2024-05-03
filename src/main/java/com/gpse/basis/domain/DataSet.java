package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Document(collection = "dataSets")
public class DataSet {
    @MongoId
    private String id;

    private String fileName;

    private String streckenId;

    private Date uploadDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStreckenId() {
        return streckenId;
    }

    public void setStreckenId(String streckenId) {
        this.streckenId = streckenId;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public String toString() {
        return fileName + ": " + streckenId + ", " + uploadDate.toString();
    }
}
