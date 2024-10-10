package com.example.project2;  // Ensure this matches the package in your EditDisasterController

public class Disaster {
    private int id;
    private String type;
    private String location;
    private String description;
    private String priority;
    private String department;

    // Constructor
    public Disaster(String id, String type, String location, String description, String priority, String department) {
        this.id = Integer.parseInt(id);
        this.type = type;
        this.location = location;
        this.description = description;
        this.priority = priority;
        this.department = department;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
