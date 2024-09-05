package com.example.homesecurity.factory;

import com.example.homesecurity.entity.MotionSensor;
import com.example.homesecurity.service.MotionSensorService;
import com.example.homesecurity.thread.SaveMotionSensorThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveMotionSensorThreadFactory {

    private static final Logger logger= LoggerFactory.getLogger(SaveMotionSensorThreadFactory.class);
    private final MotionSensorService motionSensorService;

    @Autowired
    public SaveMotionSensorThreadFactory(MotionSensorService motionSensorService) {
        this.motionSensorService = motionSensorService;
    }

    public SaveMotionSensorThread create(MotionSensor sensor) {
        SaveMotionSensorThread thread = new SaveMotionSensorThread(motionSensorService);
        thread.setSensor(sensor);
        logger.info("SaveMotionSensorThread created");

        return thread;
    }
}
