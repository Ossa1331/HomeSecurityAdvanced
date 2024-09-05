package com.example.homesecurity.enums;

public enum DeviceType {

    CAMERA("Camera"),
    CO2_SENSOR("CO2 Sensor"),
    HEAT_SENSOR("Heat Sensor"),
    GLASS_BREAK_SENSOR("Glass Break Sensor"),
    MOTION_SENSOR("Motion Sensor"),
    SMOKE_SENSOR("Smoke Sensor");

    private final String deviceType;

    DeviceType(String deviceType){
        this.deviceType=deviceType;
    }

    public String getDeviceType(){
        return deviceType;
    }

}
