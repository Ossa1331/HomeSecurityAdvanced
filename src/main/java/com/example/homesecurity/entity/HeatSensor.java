package com.example.homesecurity.entity;

import com.example.homesecurity.enums.DeviceType;
import com.example.homesecurity.enums.Locations;
import com.example.homesecurity.exception.LowerHigherThanUpperLimitException;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Table(name="heat_sensor")
public non-sealed class HeatSensor extends Device implements CalculateCAndF, Measurable<Double> {

    @Column(name="upper_limit")
    private Double sensorUpperLimit;
    @Column(name="lower_limit")
    private Double sensorLowerLimit;

    @Column(name="temperature_in_c")
    private Double temperatureInC;

    @Column(name="temperature_in_f")
    private Double temperatureInF;

    @ManyToOne
    @JoinColumn(name="address_id")
    private Address address;


    @Transient
    private static final Logger logger= LoggerFactory.getLogger(HeatSensor.class);


    public HeatSensor(Long deviceId, String deviceName, DeviceType deviceType, Boolean deviceStatus, String deviceModel, String deviceSerialNumber, String deviceManufacturer,
                      LocalDate deviceManufacturingDate, Locations location, Double sensorUpperLimit, Double sensorLowerLimit, Double temperatureInC, LocalDateTime dateAdded) {
        super(deviceId, deviceName, deviceStatus,deviceModel,deviceSerialNumber, deviceManufacturer, deviceManufacturingDate, location, deviceType, dateAdded);
        this.sensorLowerLimit=sensorLowerLimit;
        this.sensorUpperLimit=sensorUpperLimit;
        this.temperatureInC=temperatureInC;
        this.temperatureInF=celsiusToFahrenheit(this.temperatureInC);
    }

    public HeatSensor(){}

    public Double celsiusToFahrenheit(Double tempInCelsius){

        this.temperatureInF= (tempInCelsius*(9/5))+32;

        return this.temperatureInF;
    }

    @Override
    public Double fahrenheitToCelsius(Double tempInFahrenheit){

        this.temperatureInC=(tempInFahrenheit-32) *(5/9);

        return this.temperatureInC;
    }


    public Double getTemperatureInC() {
        return temperatureInC;
    }

    public void setTemperatureInC(Double temperatureInC) {
        this.temperatureInC = temperatureInC;
    }

    public Double getSensorUpperLimit() {
        return sensorUpperLimit;
    }

    public Double getSensorLowerLimit() {
        return sensorLowerLimit;
    }

    public Double getTemperatureInF() {
        return temperatureInF;
    }

    public void setTemperatureInF(Double temperatureInF) {
        this.temperatureInF = temperatureInF;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public static HeatSensorBuilder builder(){
        return new HeatSensorBuilder();
    }


    @Override
    public Double measure() {
        if(this.sensorLowerLimit>=this.sensorUpperLimit){
            throw new LowerHigherThanUpperLimitException();
        }
        Random random = new Random();
        this.temperatureInC=this.sensorLowerLimit+ random.nextDouble()*(this.sensorUpperLimit-this.sensorLowerLimit);
        this.temperatureInF=celsiusToFahrenheit(this.temperatureInC);

        logger.info(this.getDeviceName() + " has been measured");

        return this.temperatureInC;
    }


    public static class HeatSensorBuilder extends DeviceBuilder<HeatSensor,HeatSensorBuilder>{

        HeatSensorBuilder(){
            this.entity=new HeatSensor();
        }

        @Override
        protected HeatSensorBuilder self(){
            return this;
        }

        public HeatSensorBuilder sensorUpperLimit(Double sensorUpperLimit){
            entity.sensorUpperLimit=sensorUpperLimit;
            return this;
        }
        public HeatSensorBuilder sensorLowerLimit(Double sensorLowerLimit){
            entity.sensorLowerLimit=sensorLowerLimit;
            return this;
        }
        public HeatSensorBuilder temperatureInC(Double temperatureInC){
            entity.temperatureInC=temperatureInC;
            return this;
        }

        public HeatSensorBuilder address(Address address){
            entity.address=address;
            return this;
        }


    }

}
