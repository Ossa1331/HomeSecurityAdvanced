package com.example.homesecurity.thread;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.service.CO2SensorService;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteCO2SensorThread implements Runnable {

    private static final Logger logger= LoggerFactory.getLogger(DeleteCO2SensorThread.class);

    private CO2Sensor sensor;
    private final CO2SensorService co2SensorService;

    public DeleteCO2SensorThread(CO2SensorService co2SensorService) {
        this.co2SensorService = co2SensorService;
    }

    public void setCo2Sensor(CO2Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public void run(){
        Platform.runLater(()->{
            try{
                co2SensorService.deleteCO2Sensor(sensor);
                logger.info("DeleteCO2SensorThread running...");
            }catch(Exception e){
                logger.error("there has been an error in thread runtime. ", e);
            }
        });
    }
}
