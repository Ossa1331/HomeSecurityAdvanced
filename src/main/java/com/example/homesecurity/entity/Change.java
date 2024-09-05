package com.example.homesecurity.entity;

import com.example.homesecurity.controller.ChooseLocationController;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Change implements Serializable {

    private String deviceName;
    private String deviceType;
    private String message;
    private LocalDateTime timeAdded;
    private Long currentLocationChange;


    public Change(String deviceName, String deviceType, String message, LocalDateTime time_added){
        this.deviceName=deviceName;
        this.deviceType=deviceType;
        this.message=message;
        this.timeAdded=time_added;
        this.currentLocationChange= ChooseLocationController.currentLocation.getId();
    }

    public Change(String message, LocalDateTime timeAdded){
        this.message=message;
        this.timeAdded=timeAdded;
    }
    public LocalDateTime getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(LocalDateTime timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Long getCurrentLocationChange() {
        return currentLocationChange;
    }

    public void setCurrentLocationChange(Long currentLocationChange) {
        this.currentLocationChange = currentLocationChange;
    }
}
