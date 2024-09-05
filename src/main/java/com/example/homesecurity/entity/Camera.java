package com.example.homesecurity.entity;

import com.example.homesecurity.controller.ChooseLocationController;
import com.example.homesecurity.enums.DeviceType;
import com.example.homesecurity.enums.Locations;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Table(name="camera")
public class Camera extends Device implements Measurable<Boolean> {

    @Column(name="current_camera_time")
    private LocalDateTime currentCameraTime;

    @Column(name="human_identified")
    private Boolean humanIdentified;
    @ManyToOne
    @JoinColumn(name="address_id")
    private Address address;
    @Transient
    private static final Logger logger= LoggerFactory.getLogger(Camera.class);

    public Camera(Long deviceId, String deviceName, DeviceType deviceType, Boolean deviceStatus, String deviceModel, String deviceSerialNumber, String deviceManufacturer, LocalDate deviceManufacturingDate, Locations location, LocalDateTime dateAdded) {
        super(deviceId, deviceName, deviceStatus, deviceModel, deviceSerialNumber, deviceManufacturer,deviceManufacturingDate, location, deviceType, dateAdded);

        this.currentCameraTime=LocalDateTime.now();

        this.humanIdentified=false;

        this.address= ChooseLocationController.currentLocation;
    }

    public Camera(){}

    public LocalDateTime getCurrentCameraTime() {
        return currentCameraTime;
    }

    public Boolean getHumanIdentified() {
        return humanIdentified;
    }

    public void setHumanIdentified(Boolean humanIdentified) {
        this.humanIdentified = humanIdentified;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public static CameraBuilder builder(){
        return new CameraBuilder();
    }

    public Boolean measure(){
        Random random = new Random();
        this.humanIdentified=random.nextBoolean();

        logger.info(this.getDeviceName() + " has been measured");

        return this.humanIdentified;
    }

    public static class CameraBuilder extends DeviceBuilder<Camera, CameraBuilder>{

        CameraBuilder(){
            this.entity= new Camera();
        }

        @Override
        protected CameraBuilder self(){
            return this;
        }

        public CameraBuilder humanIdentified(Boolean humanIdentified){
            entity.humanIdentified=humanIdentified;
            return this;
        }

        public CameraBuilder currentCameraTime(LocalDateTime currentCameraTime){
            entity.currentCameraTime=currentCameraTime;
            return this;
        }

        public CameraBuilder address(Address address){
            entity.address=address;
            return this;
        }
    }
}
