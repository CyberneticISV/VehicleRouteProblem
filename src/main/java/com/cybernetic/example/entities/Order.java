package com.cybernetic.example.entities;

public class Order {

    private static int count = 0;

    private String id;
    private String locationId;
    private int dimensionValue;
    private double startTime;
    private double endTime;

    public Order(String id, String locationId, int dimensionValue, double startTime, double endTime) {
        this.id = id;
        this.locationId = locationId;
        this.dimensionValue = dimensionValue;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public int getDimensionValue() {
        return dimensionValue;
    }

    public void setDimensionValue(int dimensionValue) {
        this.dimensionValue = dimensionValue;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }
}
