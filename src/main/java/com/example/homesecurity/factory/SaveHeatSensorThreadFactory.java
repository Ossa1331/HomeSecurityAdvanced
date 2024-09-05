package com.example.homesecurity.factory;

import com.example.homesecurity.entity.HeatSensor;
import com.example.homesecurity.service.HeatSensorService;
import com.example.homesecurity.thread.SaveHeatSensorThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveHeatSensorThreadFactory {

    private static final Logger logger= LoggerFactory.getLogger(SaveHeatSensorThreadFactory.class);

    private final HeatSensorService heatSensorService;

    @Autowired
    public SaveHeatSensorThreadFactory(HeatSensorService heatSensorService) {
        this.heatSensorService = heatSensorService;
    }

    public SaveHeatSensorThread create(HeatSensor sensor) {
        SaveHeatSensorThread thread = new SaveHeatSensorThread(heatSensorService);
        thread.setSensor(sensor);
        logger.info("SaveHeatSensorThread created");

        return thread;
    }
}
