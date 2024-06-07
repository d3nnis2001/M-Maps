package com.gpse.basis.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;


@Document(collection = "inspectionOrder")
public class InspectionOrder {
    @MongoId
    private String inspectionOrderId;
    private String courseId;
    private String userId;
    private String startLocation;
    private String endLocation;
    private String startTime;
    private String endTime;
    private String department;
    private String inspectionData;
    private String status;
    private String remarks;
    private boolean archived;
    private String priority;

    public InspectionOrder(String inspectionOrderId, String courseId, String userId, String startLocation, String endLocation, String startTime, String endTime,
                           String department, String inspectionData, String status, String remarks, boolean archived, String priority) {
        this.inspectionOrderId = inspectionOrderId;
        this.courseId = courseId;
        this.userId = userId;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startTime = startTime;
        this.endTime = endTime;
        this.department = department;
        this.inspectionData = inspectionData;
        this.status = status;
        this.remarks = remarks;
        this.archived = archived;
        this.priority = priority;
    }

    public String getInspectionOrderId() {
        return inspectionOrderId;
    }

    public void setInspectionOrderId(String inspectionOrderId) {
        this.inspectionOrderId = inspectionOrderId;
    }
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String id) {
        this.courseId = id;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getInspectionData() {
        return inspectionData;
    }

    public void setInspectionData(String inspectionData) {
        this.inspectionData = inspectionData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
