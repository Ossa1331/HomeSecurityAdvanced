/*package com.example.homesecurity.thread;

import com.example.homesecurity.dao.AddressDAO;
import com.example.homesecurity.dao.CameraDAO;
import com.example.homesecurity.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.homesecurity.util.DatabaseUtil;
import com.example.homesecurity.util.FileUtil;
import org.springframework.context.annotation.Bean;

public abstract class DatabaseThreads {

    private static final Logger logger= LoggerFactory.getLogger(CO2Sensor.class);
    public static Boolean isDatabaseOperationInProgress=false;
    public synchronized void updateCamera(Camera camera, Event event){
        while(isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException();
            }
        }
        isDatabaseOperationInProgress = true;
        DatabaseUtil.updateMeasurableCamera(camera);
        DatabaseUtil.saveEvent(event);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }
    public synchronized void updateCO2Sensor(CO2Sensor sensor, Event event){
        while(isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException ex) {
                logger.error("exception occurred!");
                throw new RuntimeException();
            }
        }
            isDatabaseOperationInProgress = true;
            DatabaseUtil.updateMeasurableCO2Sensor(sensor);
            DatabaseUtil.saveEvent(event);
            isDatabaseOperationInProgress = false;
            notifyAll();

    }
    public synchronized void updateGlassBreakSensor(GlassBreakSensor sensor, Event event){
        while(isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException();
            }
        }
        isDatabaseOperationInProgress = true;
        DatabaseUtil.updateMeasurableGlassBreakSensor(sensor);
        DatabaseUtil.saveEvent(event);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }
    public synchronized void updateHeatSensor(HeatSensor sensor, Event event){
        while(isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException();
            }
        }
        isDatabaseOperationInProgress = true;
        DatabaseUtil.updateMeasurableHeatSensor(sensor);
        DatabaseUtil.saveEvent(event);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }
    public synchronized void updateMotionSensor(MotionSensor sensor, Event event){
        while(isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException();
            }
        }
        isDatabaseOperationInProgress = true;
        DatabaseUtil.updateMeasurableMotionSensor(sensor);
        DatabaseUtil.saveEvent(event);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }
    public synchronized void updateSmokeSensor(SmokeSensor sensor, Event event){
        while(isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException();
            }
        }
        isDatabaseOperationInProgress = true;
        DatabaseUtil.updateMeasurableSmokeSensor(sensor);
        DatabaseUtil.saveEvent(event);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }
    public synchronized void updateStatusDevice(Device device, Change change){
        while(isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException();
            }
        }
        isDatabaseOperationInProgress = true;
        DatabaseUtil.updateDevice(device);
        DatabaseUtil.saveChange(change);

        isDatabaseOperationInProgress = false;
        notifyAll();
    }
    public synchronized void DeleteDevice(Device device, Change change){
        while(isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException();
            }
        }
        isDatabaseOperationInProgress = true;
        switch(device.getDeviceType()){
            case("Camera"):{
                DatabaseUtil.deleteCamera((Camera)device);
                break;
            }
            case("CO2 Sensor"):{
                DatabaseUtil.deleteCO2Sensor((CO2Sensor) device);
                break;
            }
            case("Glass Break Sensor"):{
                DatabaseUtil.deleteGlassBreakSensor((GlassBreakSensor) device);
                break;
            }
            case("Heat Sensor"):{
                DatabaseUtil.deleteHeatSensor((HeatSensor) device);
                break;
            }
            case("Motion Sensor"):{
                DatabaseUtil.deleteMotionSensor((MotionSensor) device);
                break;
            }
            case("Smoke Sensor"):{
                DatabaseUtil.deleteSmokeSensor((SmokeSensor) device);
                break;
            }

        }
        DatabaseUtil.saveChange(change);
        FileUtil.serializeChange(change);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void updateNameSmokeSensor(SmokeSensor sensor, Change change, String result){
        while(isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException();
            }
        }
        isDatabaseOperationInProgress = true;
        DatabaseUtil.updateSmokeSensor(sensor, result);
        DatabaseUtil.saveChange(change);

        isDatabaseOperationInProgress = false;
        notifyAll();
    }
    public synchronized void updateNameMotionSensor(MotionSensor sensor, Change change, String result){
        while(isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException();
            }
        }
        isDatabaseOperationInProgress = true;
        DatabaseUtil.updateMotionSensor(sensor, result);
        DatabaseUtil.saveChange(change);

        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void updateNameHeatSensor(HeatSensor sensor, Change change, String result){
        while(isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException();
            }
        }
        isDatabaseOperationInProgress = true;
        DatabaseUtil.updateHeatSensor(sensor, result);
        DatabaseUtil.saveChange(change);

        isDatabaseOperationInProgress = false;
        notifyAll();
    }
    public synchronized void updateNameCamera(Camera camera, Change change, String result){
        while(isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException();
            }
        }
        isDatabaseOperationInProgress = true;
        DatabaseUtil.updateCamera(camera, result);
        DatabaseUtil.saveChange(change);

        isDatabaseOperationInProgress = false;
        notifyAll();
    }
    public synchronized void updateNameGlassBreakSensor(GlassBreakSensor sensor, Change change, String result){
        while(isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException();
            }
        }
        isDatabaseOperationInProgress = true;
        DatabaseUtil.updateGlassBreakSensor(sensor, result);
        DatabaseUtil.saveChange(change);

        isDatabaseOperationInProgress = false;
        notifyAll();
    }
    public synchronized void updateNameCO2Sensor(CO2Sensor sensor, Change change, String result){
        while(isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException();
            }
        }
        isDatabaseOperationInProgress = true;
        DatabaseUtil.updateCO2Sensor(sensor, result);
        DatabaseUtil.saveChange(change);

        isDatabaseOperationInProgress = false;
        notifyAll();
    }



}*/
