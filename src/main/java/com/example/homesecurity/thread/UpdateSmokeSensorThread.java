package com.example.homesecurity.thread;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.entity.Event;
import com.example.homesecurity.entity.SmokeSensor;
import com.example.homesecurity.service.SmokeSensorService;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateSmokeSensorThread implements Runnable{

    private static final Logger logger= LoggerFactory.getLogger(CO2Sensor.class);

    private SmokeSensor sensor;

    private Event event;
    private final SmokeSensorService smokeSensorService;

    public UpdateSmokeSensorThread(SmokeSensorService smokeSensorService) {
        this.smokeSensorService= smokeSensorService;
    }

    public void setSensor(SmokeSensor sensor) {
        this.sensor = sensor;
    }

    public void setEvent(Event event){this.event=event;}

    @Override
    public void run(){
        Platform.runLater(()->{
            try{
                if(event==null){
                    smokeSensorService.updateSmokeSensor(sensor);
                    logger.info("UpdateSmokeSensorThread running...");
                }
                else{
                    smokeSensorService.updateSmokeSensor(sensor,event);
                    logger.info("UpdateSmokeSensorThread with event running");
                }

            }catch(Exception e){
                logger.error("there has been an error in thread runtime. ", e);
            }
        });
    }
}
