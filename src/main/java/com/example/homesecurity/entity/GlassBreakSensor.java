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
@Table(name="glass_break_sensor")
@Component
public class GlassBreakSensor extends Device implements Measurable<Boolean> {

    @Column(name="glass_broken")
    private Boolean glassBreak;

    @ManyToOne
    @JoinColumn(name="address_id")
    private Address address;

    @Transient
    private static final Logger logger= LoggerFactory.getLogger(GlassBreakSensor.class);
    public GlassBreakSensor(Long deviceId, String deviceName, DeviceType deviceType, Boolean deviceStatus, String deviceModel, String deviceSerialNumber, String deviceManufacturer, LocalDate deviceManufacturingDate, Locations location, LocalDateTime dateAdded) {
        super(deviceId, deviceName, deviceStatus, deviceModel, deviceSerialNumber, deviceManufacturer, deviceManufacturingDate, location , deviceType, dateAdded);
        this.glassBreak=false;
    }
    public GlassBreakSensor(){}
    public Boolean getGlassBreak() {
        return glassBreak;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setGlassBreak(Boolean glassBreak) {
        this.glassBreak = glassBreak;
    }

    public static GlassBreakSensorBuilder builder(){
        return new GlassBreakSensorBuilder();
    }

    @Override
    public Boolean measure(){
        Random random = new Random();
        this.glassBreak=random.nextBoolean();


        logger.info(this.getDeviceName() + " has been measured");

        return this.glassBreak;
    }

    public static class GlassBreakSensorBuilder extends DeviceBuilder<GlassBreakSensor,GlassBreakSensorBuilder>{

        public GlassBreakSensorBuilder(){
            this.entity=new GlassBreakSensor();
        }

        public GlassBreakSensorBuilder glassBroken(Boolean glassBroken){
            entity.glassBreak=glassBroken;
            return this;
        }
        public GlassBreakSensorBuilder address(Address address){
            entity.address=address;
            return this;
        }

        @Override
        protected GlassBreakSensorBuilder self(){
            return this;
        }
    }

}
