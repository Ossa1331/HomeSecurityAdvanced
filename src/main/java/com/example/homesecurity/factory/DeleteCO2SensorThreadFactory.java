package com.example.homesecurity.factory;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.service.CO2SensorService;
import com.example.homesecurity.thread.DeleteCO2SensorThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteCO2SensorThreadFactory {

    private static final Logger logger= LoggerFactory.getLogger(DeleteCO2SensorThreadFactory.class);
    private final CO2SensorService co2SensorService;

    @Autowired
    public DeleteCO2SensorThreadFactory(CO2SensorService co2SensorService) {
        this.co2SensorService = co2SensorService;
    }

    public DeleteCO2SensorThread create(CO2Sensor sensor) {
        DeleteCO2SensorThread thread = new DeleteCO2SensorThread(co2SensorService);
        thread.setCo2Sensor(sensor);
        logger.info("DeleteCO2SensorThread created");

        return thread;
    }
}
