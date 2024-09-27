package com.example.project2;

public class DisasterRecord {
    private String disasterNo;
    private String disasterType;
    private String location;
    private String department;

    public DisasterRecord(String disasterNo, String disasterType, String location, String department) {
        this.disasterNo = disasterNo;
        this.disasterType = disasterType;
        this.location = location;
        this.department = department;
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
}
