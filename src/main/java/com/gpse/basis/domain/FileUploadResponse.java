package com.gpse.basis.domain;

public class FileUploadResponse {
    private String name;
    private boolean accepted;

    private String reason;

    public FileUploadResponse(String name, boolean accepted, String reason) {
        this.name = name;
        this.accepted = accepted;
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
