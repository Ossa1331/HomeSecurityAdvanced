package com.example.homesecurity.thread;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.entity.Event;
import com.example.homesecurity.entity.HeatSensor;
import com.example.homesecurity.service.HeatSensorService;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateHeatSensorThread implements Runnable {

    private static final Logger logger= LoggerFactory.getLogger(CO2Sensor.class);

    private HeatSensor sensor;

    private Event event;
    private final HeatSensorService heatSensorService;

    public UpdateHeatSensorThread(HeatSensorService heatSensorService) {
        this.heatSensorService= heatSensorService;
    }

    public void setSensor(HeatSensor sensor) {
        this.sensor = sensor;
    }

    public void setEvent(Event event){this.event=event;}

    @Override
    public void run(){
        Platform.runLater(()->{
            try{
                if(event==null){
                    heatSensorService.updateHeatSensor(sensor);
                    logger.info("UpdateHeatSensorThread running...");
                }
                else{
                    heatSensorService.updateHeatSensor(sensor, event);
                    logger.info("UpdateHeatSensorThread with event running...");
                }
            }catch(Exception e){
                logger.error("there has been an error in thread runtime. ", e);
            }
        });
    }

}
