package com.example.homesecurity.thread;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.entity.GlassBreakSensor;
import com.example.homesecurity.service.GlassBreakSensorService;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveGlassBreakSensorThread implements Runnable{

    private static final Logger logger= LoggerFactory.getLogger(CO2Sensor.class);

    private GlassBreakSensor sensor;
    private final GlassBreakSensorService glassBreakSensorService;

    public SaveGlassBreakSensorThread(GlassBreakSensorService glassBreakSensorService) {
        this.glassBreakSensorService = glassBreakSensorService;
    }

    public void setSensor(GlassBreakSensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public void run(){
        Platform.runLater(()->{
            try{
                glassBreakSensorService.saveGlassBreakSensor(sensor);
                logger.info("SaveGlassBreakSensorThread running...");
            }catch(Exception e){
                logger.error("there has been an error in thread runtime. ", e);
            }
        });
    }
}
