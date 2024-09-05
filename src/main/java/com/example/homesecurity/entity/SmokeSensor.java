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
 @Table(name="smoke_sensor")
public class SmokeSensor extends Device implements Measurable<Double> {
     @Column(name = "current_obscuration")
     private Double currentObscuration;

     @Transient
     private static final Logger logger= LoggerFactory.getLogger(SmokeSensor.class);

     @ManyToOne
     @JoinColumn(name="address_id")
     private Address address;

     public SmokeSensor(Long deviceId, String deviceName, DeviceType deviceType, Boolean deviceStatus, String deviceModel, String deviceSerialNumber, String deviceManufacturer, LocalDate deviceManufacturingDate, Locations location, Double currentObscuration, LocalDateTime dateAdded) {
        super(deviceId, deviceName, deviceStatus, deviceManufacturer,deviceModel,deviceSerialNumber, deviceManufacturingDate, location, deviceType, dateAdded);
        this.currentObscuration=currentObscuration;
     }

     public SmokeSensor(){}
     public Double getCurrentObscuration() {
        return currentObscuration;
    }
     public void setCurrentObscuration(Double currentObscuration) {
         this.currentObscuration = currentObscuration;
     }

     public Address getAddress() {
         return address;
     }

     public void setAddress(Address address) {
         this.address = address;
     }

     public static SmokeSensorBuilder builder(){
        return new SmokeSensorBuilder();
    }

    @Override
    public Double measure(){
        Random random = new Random();
        this.currentObscuration=random.nextDouble()*80;

        logger.info(this.deviceName + " has been measured");

        return this.currentObscuration;
    }
    public static class SmokeSensorBuilder extends DeviceBuilder<SmokeSensor, SmokeSensorBuilder>{


        SmokeSensorBuilder(){
            this.entity=new SmokeSensor();
        }

        protected SmokeSensorBuilder self(){
            return this;
        }

        public SmokeSensorBuilder currentObscuration(Double currentObscuration){
            entity.currentObscuration=currentObscuration;
            return this;
        }

       public SmokeSensorBuilder address(Address address){
           entity.address=address;
           return this;
       }

    }
}
