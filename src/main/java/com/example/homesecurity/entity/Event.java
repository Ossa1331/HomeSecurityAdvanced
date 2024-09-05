package com.example.homesecurity.entity;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Entity
@Table(name="event_log")
public class Event {
    @Transient
    private static final Logger logger= LoggerFactory.getLogger(Event.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;
    @Column(name="time_added")
    private LocalDateTime currentEventTriggered;
    @Column(name="message")
    private String message;
    @Column(name="device_name")
    private String deviceName;
    @Column(name="device_type")
    private String deviceType;
    @ManyToOne
    @JoinColumn(name="address_id")
    private Address address;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Event (LocalDateTime currentEventTriggered, String deviceName, String deviceType) {
        this.currentEventTriggered = currentEventTriggered;
        this.deviceType=deviceType;
        this.deviceName=deviceName;
    }
    public Event(){

    }


    public LocalDateTime getCurrentEventTriggered() {
        return currentEventTriggered;
    }

    public void checkTriggerEvent(GlassBreakSensor sensor){
        this.deviceName = sensor.getDeviceName();
        this.deviceType=sensor.getDeviceType().getDeviceType();
        logger.info("the glass break sensor: " + sensor.getDeviceName()+ " has been accessed");
        if(sensor.getGlassBreak() && sensor.getDeviceStatus()) {
            this.message= "Warning: Glass Sensor" + sensor.getDeviceName() + "has been triggered, your glass" +
                    " integrity may be compromised";
        }
        else if(!sensor.getGlassBreak()&& sensor.getDeviceStatus()){
            this.message= "Glass Sensor" +sensor.getDeviceName() + "hasn't been triggered, your establishment is safe for now";
        }
        else{
            this.message=  "Glass Sensor" + sensor.getDeviceName() + "is off, please turn it on.";
        }

    }

    public void checkTriggerEvent(HeatSensor sensor){
        this.deviceName = sensor.getDeviceName();
        this.deviceType=sensor.getDeviceType().getDeviceType();
        if(!sensor.getDeviceStatus()){
            this.message= "Heat Sensor "+sensor.getDeviceName() +" is off, please turn it on.";
        }
        else if(sensor.getTemperatureInC()> 23){

            this.message= "Warning: Heat Sensor "+sensor.getDeviceName()+" is detecting a heat spike, your house" +
                    " temperature may be compromised";
        }
        else if(sensor.getSensorLowerLimit()<0){
            this.message= "Warning: Heat Sensor "+ sensor.getDeviceName()+ " is detecting unusually low temperatures";
        }
        else{
            this.message= "Heat Sensor "+ sensor.getDeviceName() + " is detecting normal temperatures. Everything is okay";
        }
    }
    public void checkTriggerEvent(CO2Sensor sensor){
        this.deviceName = sensor.getDeviceName();
        this.deviceType=sensor.getDeviceType().getDeviceType();
        if(!sensor.getDeviceStatus()){
            this.message= "CO2 Sensor "+sensor.getDeviceName() +" is off, please turn it on.";
        }
        else if(sensor.getCurrentCO2()<0.05){
            this.message= "CO2 Sensor " +sensor.getDeviceName() +" detects that CO2 is in excellent rates. Everything is okay";
        }
        else if(sensor.getCurrentCO2()>0.04 && sensor.getCurrentCO2()<0.2){
            this.message= "CO2 Sensor " +sensor.getDeviceName() +" detects that CO2 is in acceptable rates. Everything is fine";
        }
        else if(sensor.getCurrentCO2()>0.2 && sensor.getCurrentCO2()<0.5){
            this.message= "Warning CO2 Sensor " + sensor.getDeviceName()+ "detects that CO2 is in abnormal rates. You may experience " +
                    " symptoms such as headaches and increased heart rate.";
        }
        else{
            this.message= "Warning CO2 Sensor " + sensor.getDeviceName()+ "detects that CO2 is in critical rates. Evacuate immediately.";
        }
    }
    public void checkTriggerEvent(Camera sensor){
        this.deviceName = sensor.getDeviceName();
        this.deviceType=sensor.getDeviceType().getDeviceType();
        if(!sensor.getDeviceStatus()) {
            this.message= "Camera " + sensor.getDeviceName() + " is off, please turn it on.";
        }
        else if(!sensor.getHumanIdentified()){
            this.message="Camera " + sensor.getDeviceName() + " isn't detecting a human, everything is okay";
        }
        else{
            this.message="Warning: Camera " + sensor.getDeviceName() + " is detecting is detecting a human, your establishment may be compromised";
        }
    }
    public void checkTriggerEvent(MotionSensor sensor){
        this.deviceName = sensor.getDeviceName();
        this.deviceType=sensor.getDeviceType().getDeviceType();
        if(!sensor.getDeviceStatus()) {
            this.message= "Motion Sensor " + sensor.getDeviceName() + " is off, please turn it on.";
        }
        else if(!sensor.isAnomalyDetected()){
            this.message= "Motion Sensor " + sensor.getDeviceName() + " isn't detecting motion, everything is okay";
            }
        else{
            this.message= "Warning Motion Sensor " + sensor.getDeviceName() + " is detecting motion, your establishment may be compromised";
        }
    }
    public void checkTriggerEvent(SmokeSensor sensor){
        this.deviceName = sensor.getDeviceName();
        this.deviceType=sensor.getDeviceType().getDeviceType();
        if(!sensor.getDeviceStatus()) {
            this.message= "Smoke Sensor " + sensor.getDeviceName() + " is off, please turn it on.";
        }
        else if(sensor.getCurrentObscuration()<20){
            this.message= "Smoke Sensor " + sensor.getDeviceName() + " isn't detecting smoke, everything is okay.";
        }
        else if(sensor.getCurrentObscuration()<50&&sensor.getCurrentObscuration()>20){
            this.message= "Warning Motion Sensor " + sensor.getDeviceName() + " is detecting mild amounts of smoke, your establishment might be at risk";
        }
        else{
            this.message= "Warning Smoke Sensor " + sensor.getDeviceName() + " is detecting large amounts of smoke, your establishment is at great risk";
        }
    }
}
