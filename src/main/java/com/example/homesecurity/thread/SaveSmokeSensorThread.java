package com.example.homesecurity.thread;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.entity.SmokeSensor;
import com.example.homesecurity.service.SmokeSensorService;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveSmokeSensorThread implements Runnable {

    private static final Logger logger= LoggerFactory.getLogger(CO2Sensor.class);

    private SmokeSensor sensor;
    private final SmokeSensorService smokeSensorService;

    public SaveSmokeSensorThread(SmokeSensorService smokeSensorService) {
        this.smokeSensorService = smokeSensorService;
    }

    public void setSensor(SmokeSensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public void run(){
        Platform.runLater(()->{
            try{
                smokeSensorService.saveSmokeSensor(sensor);
                logger.info("SaveSmokeSensorThread running...");
            }catch(Exception e){
                logger.error("there has been an error in thread runtime. ", e);
            }
        });
    }
}
