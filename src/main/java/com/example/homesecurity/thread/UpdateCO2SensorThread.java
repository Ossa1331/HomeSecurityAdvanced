package com.example.homesecurity.thread;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.entity.Event;
import com.example.homesecurity.service.CO2SensorService;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateCO2SensorThread implements Runnable{

    private static final Logger logger= LoggerFactory.getLogger(CO2Sensor.class);

    private CO2Sensor sensor;

    private Event event;
    private final CO2SensorService co2SensorService;

    public UpdateCO2SensorThread(CO2SensorService co2SensorService) {
        this.co2SensorService = co2SensorService;
    }

    public void setSensor(CO2Sensor sensor) {
        this.sensor = sensor;
    }

    public void setEvent(Event event) { this.event=event;}

    @Override
    public void run(){
        Platform.runLater(()->{
            try{
                if(event==null){
                    co2SensorService.updateCO2Sensor(sensor);
                    logger.info("UpdateCO2SensorThread running");
                }
                else{
                    co2SensorService.updateCO2Sensor(sensor,event);
                    logger.info("UpdateCO2SensorThread with event running");
                }

            }catch(Exception e){
                logger.error("there has been an error in thread runtime. ", e);
            }
        });
    }
}
