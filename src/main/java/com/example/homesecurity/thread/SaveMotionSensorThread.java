package com.example.homesecurity.thread;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.entity.MotionSensor;
import com.example.homesecurity.service.MotionSensorService;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveMotionSensorThread implements Runnable{

    private static final Logger logger= LoggerFactory.getLogger(CO2Sensor.class);

    private MotionSensor sensor;
    private final MotionSensorService motionSensorService;

    public SaveMotionSensorThread(MotionSensorService motionSensorService) {
        this.motionSensorService = motionSensorService;
    }

    public void setSensor(MotionSensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public void run(){
        Platform.runLater(()->{
            try{
                motionSensorService.saveMotionSensor(sensor);
                logger.info("SaveMotionSensorThread running...");
            }catch(Exception e){
                logger.error("there has been an error in thread runtime. ", e);
            }
        });
    }
}
