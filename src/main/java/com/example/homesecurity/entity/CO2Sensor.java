package com.example.homesecurity.entity;

import com.example.homesecurity.enums.DeviceType;
import com.example.homesecurity.enums.Locations;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Table(name="co2sensor")
@Component
public class CO2Sensor extends Device {
    @Column(name="currentc02")
    private Float currentCO2;

    @ManyToOne
    @JoinColumn(name="address_id")
    private Address address;
    @Transient
    private static final Logger logger= LoggerFactory.getLogger(CO2Sensor.class);


    public CO2Sensor(Long deviceId, String deviceName, DeviceType deviceType, Boolean deviceStatus, String deviceModel, String deviceSerialNumber, String deviceManufacturer, LocalDate deviceManufacturingDate, Float currentCO2, Locations location, LocalDateTime dateAdded) {
        super(deviceId, deviceName, deviceStatus, deviceModel, deviceSerialNumber, deviceManufacturer, deviceManufacturingDate, location, deviceType, dateAdded);
        this.currentCO2=currentCO2;
    }
    public CO2Sensor(){}
    public Float getCurrentCO2() {
        return currentCO2;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setCurrentCO2(Float currentCO2) {
        this.currentCO2 = currentCO2;
    }

    public static CO2SensorBuilder builder(){
        return new CO2SensorBuilder();
    }

    public Float measure(){
        Random random=new Random();
        this.currentCO2=random.nextFloat()*70;

        logger.info(this.getDeviceName() + " has been measured");

        return currentCO2;
    }
    public static class CO2SensorBuilder extends DeviceBuilder<CO2Sensor,CO2SensorBuilder>{
        private Float currentCO2;

        public CO2SensorBuilder(){
            this.entity=new CO2Sensor();
        }

        @Override
        protected CO2SensorBuilder self(){
            return this;
        }

        public CO2SensorBuilder currentCO2(Float currentCO2){
            entity.currentCO2=currentCO2;
            return this;
        }
        public CO2SensorBuilder address(Address address){
            entity.address=address;
            return this;
        }
    }
}
