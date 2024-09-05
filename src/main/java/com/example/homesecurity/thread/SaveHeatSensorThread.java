package com.example.homesecurity.thread;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.entity.HeatSensor;
import com.example.homesecurity.service.HeatSensorService;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveHeatSensorThread implements Runnable{

    private static final Logger logger= LoggerFactory.getLogger(CO2Sensor.class);

    private HeatSensor sensor;
    private final HeatSensorService heatSensorService;

    public SaveHeatSensorThread(HeatSensorService heatSensorService) {
        this.heatSensorService =heatSensorService;
    }

    public void setSensor(HeatSensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public void run(){
        Platform.runLater(()->{
            try{
                heatSensorService.saveHeatSensor(sensor);
                logger.info("SaveHeatSensorThread running");
            }catch(Exception e){
                logger.error("there has been an error in thread runtime. ", e);
            }
        });
    }
}
