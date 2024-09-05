package com.example.homesecurity.entity;

import com.example.homesecurity.enums.DeviceType;
import com.example.homesecurity.enums.Locations;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Table(name="motion_sensor")
public class MotionSensor extends Device implements Measurable<Boolean> {


    @Column(name="anomaly_detected")
    private Boolean anomalyDetected;

    @Transient
    private static final Logger logger= LoggerFactory.getLogger(MotionSensor.class);

    @ManyToOne
    @JoinColumn(name="address_id")
    private Address address;


    public MotionSensor(Long deviceId, String deviceName, DeviceType deviceType, Boolean deviceStatus, String deviceModel, String deviceSerialNumber, String deviceManufacturer, LocalDate deviceManufacturingDate, Locations location, LocalDateTime deviceAdded) {
        super(deviceId, deviceName, deviceStatus, deviceModel,deviceSerialNumber, deviceManufacturer,deviceManufacturingDate, location, deviceType, deviceAdded);
        anomalyDetected=false;
    }

    public MotionSensor(){}
    public Boolean isAnomalyDetected() {
        return anomalyDetected;
    }

    public void setAnomalyDetected(Boolean anomalyDetected) {
        this.anomalyDetected = anomalyDetected;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public static MotionSensorBuilder builder(){
        return new MotionSensorBuilder();
    }

    @Override
    public Boolean measure(){
        Random random = new Random();
        this.anomalyDetected=random.nextBoolean();

        logger.info(this.getDeviceName() + " has been measured");

        return this.anomalyDetected;
    }


    public static class MotionSensorBuilder extends DeviceBuilder<MotionSensor, MotionSensorBuilder>{
        MotionSensorBuilder(){
            this.entity=new MotionSensor();
        }

        public MotionSensorBuilder anomalyDetected(Boolean anomalyDetected){
            entity.anomalyDetected=anomalyDetected;
            return this;
        }
        public MotionSensorBuilder address(Address address){
            entity.address=address;
            return this;
        }

        @Override
        protected MotionSensorBuilder self() {
            return this;
        }
    }
}
