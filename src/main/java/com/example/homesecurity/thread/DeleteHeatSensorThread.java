package com.example.homesecurity.thread;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.entity.HeatSensor;
import com.example.homesecurity.service.HeatSensorService;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteHeatSensorThread implements Runnable {

    private static final Logger logger= LoggerFactory.getLogger(CO2Sensor.class);

    private HeatSensor sensor;
    private final HeatSensorService heatSensorService;

    public DeleteHeatSensorThread(HeatSensorService heatSensorService) {
        this.heatSensorService = heatSensorService;
    }

    public void setHeatSensor(HeatSensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public void run(){
        Platform.runLater(()->{
            try{
                heatSensorService.deleteHeatSensor(sensor);
            }catch(Exception e){
                logger.error("there has been an error in thread runtime. ", e);
            }
        });
    }
}
