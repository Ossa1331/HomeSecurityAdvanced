package com.example.homesecurity.entity;

import com.example.homesecurity.enums.DeviceType;
import com.example.homesecurity.enums.Locations;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class Device extends DatabaseEntity{

    @Column(name="status")
    protected Boolean deviceStatus;

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    protected DeviceType deviceType;

    @Column(name="model")
    protected String deviceModel;

    @Column(name="serial_number")
    protected String deviceSerialNumber;

    @Column(name="manufacturer")
    protected String deviceManufacturer;

    @Column(name="manufacturing_date")
    protected LocalDate deviceManufacturingDate;

    @Column(name="date_added")
    protected LocalDateTime deviceAdded;

    @Enumerated(EnumType.STRING)
    @Column(name="location")
    protected Locations location;

    public Device(Long deviceId, String deviceName, Boolean deviceStatus, String deviceModel, String deviceSerialNumber,
                  String deviceManufacturer, LocalDate deviceManufacturingDate, Locations location, DeviceType deviceType, LocalDateTime deviceAdded) {
        super(deviceId,deviceName);
        this.deviceStatus = deviceStatus;
        this.deviceManufacturer=deviceManufacturer;
        this.deviceManufacturingDate= deviceManufacturingDate;
        this.location=location;
        this.deviceModel=deviceModel;
        this.deviceSerialNumber=deviceSerialNumber;
        this.deviceAdded=deviceAdded;
        this.deviceType=deviceType;
    }

    public Device(){

    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }


    public Boolean getDeviceStatus() {
        return deviceStatus;
    }

    public String getModel() {
        return deviceModel;
    }

    public void setDeviceModel(String model) {
        this.deviceModel = model;
    }

    public String getSerialNumber() {
        return deviceSerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.deviceSerialNumber = serialNumber;
    }

    public String getDeviceManufacturer() {
        return deviceManufacturer;
    }

    public void setDeviceManufacturer(String deviceManufacturer) {
        this.deviceManufacturer = deviceManufacturer;
    }

    public LocalDate getDeviceManufacturingDate() {
        return deviceManufacturingDate;
    }

    public void setDeviceManufacturingDate(LocalDate deviceManufacturingDate) {
        this.deviceManufacturingDate = deviceManufacturingDate;
    }

    public Locations getLocation() {
        return location;
    }

    public void setLocation(Locations location) {
        this.location = location;
    }

    public void setDeviceStatus(Boolean deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String deviceStatusToString(){
        if(this.getDeviceStatus()) {
            return "ON";
        }
        else{
            return "OFF";
        }
    }
    public LocalDateTime getDeviceAdded() {
        return deviceAdded;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }


    public static abstract class DeviceBuilder<T extends Device, B extends DeviceBuilder<T,B>> extends EntityBuilder<T,B>{

        DeviceBuilder(){}

        public B deviceStatus(Boolean deviceStatus){
            entity.deviceStatus=deviceStatus;
            return (B)this;
        }
        public B deviceModel(String deviceModel){
            entity.deviceModel=deviceModel;
            entity.deviceAdded=LocalDateTime.now();
            return (B)this;
        }
        public B deviceSerialNumber(String deviceSerialNumber){
            entity.deviceSerialNumber=deviceSerialNumber;
            return (B)this;
        }
        public B deviceManufacturer(String deviceManufacturer){
            entity.deviceManufacturer=deviceManufacturer;
            return (B)this;
        }
        public B deviceManufacturingDate(LocalDate deviceManufacturingDate){
            entity.deviceManufacturingDate=deviceManufacturingDate;
            return (B)this;
        }
        public B deviceDateAdded(LocalDateTime deviceAdded){
            entity.deviceAdded=deviceAdded;
            return (B)this;
        }
        public B location(Locations location){
            entity.location=location;
            return (B)this;
        }
        public B deviceType(DeviceType deviceType){
            entity.deviceType=deviceType;
            return (B)this;
        }


    }


}
