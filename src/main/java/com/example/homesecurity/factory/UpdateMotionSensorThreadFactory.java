package com.example.homesecurity.factory;

import com.example.homesecurity.entity.Event;
import com.example.homesecurity.entity.MotionSensor;
import com.example.homesecurity.service.MotionSensorService;
import com.example.homesecurity.thread.UpdateMotionSensorThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateMotionSensorThreadFactory {

    private static final Logger logger = LoggerFactory.getLogger(UpdateMotionSensorThreadFactory.class);

    private final MotionSensorService motionSensorService;

    @Autowired
    public UpdateMotionSensorThreadFactory(MotionSensorService motionSensorService) {
        this.motionSensorService = motionSensorService;
    }

    public UpdateMotionSensorThread create(MotionSensor sensor) {
        UpdateMotionSensorThread thread = new UpdateMotionSensorThread(motionSensorService);
        thread.setSensor(sensor);
        logger.info("UpdateMotionSensorThread created");

        return thread;
    }

    public UpdateMotionSensorThread create(MotionSensor sensor, Event event){
        UpdateMotionSensorThread thread= new UpdateMotionSensorThread(motionSensorService);
        thread.setSensor(sensor);
        thread.setEvent(event);
        logger.info("UpdateMotionSensorThread created with event");

        return thread;
    }

}
