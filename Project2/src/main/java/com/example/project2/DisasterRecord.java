package com.example.project2;

public class DisasterRecord {
    private String disasterNo;
    private String disasterType;
    private String location;
    private String department;
    private String priority;  // Add priority field
    private String description;

    // Updated constructor to include priority
    public DisasterRecord(String disasterNo, String disasterType, String location, String department, String priority,String description) {
        this.disasterNo = disasterNo;
        this.disasterType = disasterType;
        this.location = location;
        this.department = department;
        this.priority = priority;  // Initialize priority
        this.description = description;  // Initialize description
    }

    // Getters and Setters
    public String getDisasterNo() {
        return disasterNo;
    }

    public void setDisasterNo(String disasterNo) {
        this.disasterNo = disasterNo;
    }

    public String getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(String disasterType) {
        this.disasterType = disasterType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
