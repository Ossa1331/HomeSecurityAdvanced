package com.example.homesecurity.factory;

import com.example.homesecurity.entity.HeatSensor;
import com.example.homesecurity.service.HeatSensorService;
import com.example.homesecurity.thread.DeleteHeatSensorThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteHeatSensorThreadFactory {

    private static final Logger logger= LoggerFactory.getLogger(DeleteHeatSensorThreadFactory.class);

    private final HeatSensorService heatSensorService;

    @Autowired
    public DeleteHeatSensorThreadFactory(HeatSensorService heatSensorService) {
        this.heatSensorService = heatSensorService;
    }

    public DeleteHeatSensorThread create(HeatSensor sensor) {
        DeleteHeatSensorThread thread = new DeleteHeatSensorThread(heatSensorService);
        thread.setHeatSensor(sensor);
        logger.info("DeleteHeatSensorThread created");

        return thread;
    }
}
