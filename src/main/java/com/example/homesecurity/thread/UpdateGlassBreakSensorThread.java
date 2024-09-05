package com.example.homesecurity.thread;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.entity.Event;
import com.example.homesecurity.entity.GlassBreakSensor;
import com.example.homesecurity.service.GlassBreakSensorService;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateGlassBreakSensorThread implements Runnable {

    private static final Logger logger= LoggerFactory.getLogger(CO2Sensor.class);

    private GlassBreakSensor sensor;

    private Event event;
    private final GlassBreakSensorService glassBreakSensorService;

    public UpdateGlassBreakSensorThread(GlassBreakSensorService glassBreakSensorService) {
        this.glassBreakSensorService = glassBreakSensorService;
    }

    public void setSensor(GlassBreakSensor sensor) {
        this.sensor = sensor;
    }

    public void setEvent(Event event){ this.event=event;}

    @Override
    public void run(){
        Platform.runLater(()->{
            try{
                if(event==null){
                    glassBreakSensorService.updateGlassBreakSensor(sensor);
                    logger.info("UpdateGlassBreakSensorThread running");
                }
                else{
                    glassBreakSensorService.updateGlassBreakSensor(sensor, event);
                    logger.info("UpdateGlassBreakSensorThread with event running");
                }

            }catch(Exception e){
                logger.error("there has been an error in thread runtime. ", e);
            }
        });
    }
}
