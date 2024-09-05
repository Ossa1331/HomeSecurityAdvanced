package com.example.homesecurity.factory;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.entity.Event;
import com.example.homesecurity.service.CO2SensorService;
import com.example.homesecurity.thread.UpdateCO2SensorThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UpdateCO2SensorThreadFactory {

    private static final Logger logger= LoggerFactory.getLogger(UpdateCO2SensorThreadFactory.class);
    private final CO2SensorService co2SensorService;

    @Autowired
    public UpdateCO2SensorThreadFactory(CO2SensorService co2SensorService) {
        this.co2SensorService = co2SensorService;
    }

    public UpdateCO2SensorThread create(CO2Sensor sensor) {
        UpdateCO2SensorThread thread = new UpdateCO2SensorThread(co2SensorService);
        thread.setSensor(sensor);
        logger.info("UpdateCO2SensorThread created");

        return thread;
    }

    public UpdateCO2SensorThread create(CO2Sensor sensor, Event event){
        UpdateCO2SensorThread thread= new UpdateCO2SensorThread(co2SensorService);
        thread.setSensor(sensor);
        thread.setEvent(event);
        logger.info("UpdateCO2SensorThread created with event");

        return thread;
    }

}
