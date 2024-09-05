package com.example.homesecurity.factory;

import com.example.homesecurity.entity.Event;
import com.example.homesecurity.entity.HeatSensor;
import com.example.homesecurity.service.HeatSensorService;
import com.example.homesecurity.thread.UpdateHeatSensorThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateHeatSensorThreadFactory {

    private final Logger logger= LoggerFactory.getLogger(UpdateHeatSensorThreadFactory.class);

    private final HeatSensorService heatSensorService;

    @Autowired
    public UpdateHeatSensorThreadFactory(HeatSensorService heatSensorService) {
        this.heatSensorService =heatSensorService;
    }

    public UpdateHeatSensorThread create(HeatSensor sensor) {
        UpdateHeatSensorThread thread = new UpdateHeatSensorThread(heatSensorService);
        thread.setSensor(sensor);
        logger.info("UpdateHeatSensorThread created");

        return thread;
    }

    public UpdateHeatSensorThread create(HeatSensor sensor, Event event){
        UpdateHeatSensorThread thread=new UpdateHeatSensorThread(heatSensorService);
        thread.setSensor(sensor);
        thread.setEvent(event);
        logger.info("UpdateHeatSensorThread created with event");

        return thread;
    }

}
