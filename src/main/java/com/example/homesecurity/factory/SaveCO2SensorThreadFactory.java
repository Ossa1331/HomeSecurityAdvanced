package com.example.homesecurity.factory;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.service.CO2SensorService;
import com.example.homesecurity.thread.SaveCO2SensorThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveCO2SensorThreadFactory {

    private static final Logger logger= LoggerFactory.getLogger(SaveCO2SensorThreadFactory.class);
    private final CO2SensorService co2SensorService;

    @Autowired
    public SaveCO2SensorThreadFactory(CO2SensorService co2SensorService) {
        this.co2SensorService = co2SensorService;
    }

    public SaveCO2SensorThread create(CO2Sensor sensor) {
        SaveCO2SensorThread thread = new SaveCO2SensorThread(co2SensorService);
        thread.setSensor(sensor);
        logger.info("SaveCO2SensorThread created");

        return thread;
    }
}
