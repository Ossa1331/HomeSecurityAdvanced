package com.example.homesecurity.thread;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.entity.Event;
import com.example.homesecurity.entity.MotionSensor;
import com.example.homesecurity.service.MotionSensorService;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateMotionSensorThread implements Runnable{

    private static final Logger logger= LoggerFactory.getLogger(CO2Sensor.class);

    private MotionSensor sensor;

    private Event event;
    private final MotionSensorService motionSensorService;

    public UpdateMotionSensorThread(MotionSensorService motionSensorService) {
        this.motionSensorService= motionSensorService;
    }

    public void setSensor(MotionSensor sensor) {
        this.sensor = sensor;
    }

    public void setEvent(Event event){this.event=event;}

    @Override
    public void run(){
        Platform.runLater(()->{
            try{
                if(event==null){
                    motionSensorService.updateMotionSensor(sensor);
                    logger.info("UpdateMotionServiceThread running...");
                }
                else{
                    motionSensorService.updateMotionSensor(sensor,event);
                    logger.info("UpdateMotionServiceThread with event running...");
                }
            }catch(Exception e){
                logger.error("there has been an error in thread runtime. ", e);
            }
        });
    }
}
