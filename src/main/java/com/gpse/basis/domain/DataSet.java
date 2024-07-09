package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

/**
 * The type Data set.
 */
@Document(collection = "dataSets")
public class DataSet {
    @MongoId
    private String id;

    private String fileName;

    private int streckenId;

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

    public int getStreckenId() {
        return streckenId;
    }

    public void setStreckenId(int streckenId) {
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
