package com.example.homesecurity.factory;

import com.example.homesecurity.entity.MotionSensor;
import com.example.homesecurity.service.MotionSensorService;
import com.example.homesecurity.thread.DeleteMotionSensorThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteMotionSensorThreadFactory {

    private static final Logger logger= LoggerFactory.getLogger(DeleteMotionSensorThreadFactory.class);

    private final MotionSensorService motionSensorService;

    @Autowired
    public DeleteMotionSensorThreadFactory(MotionSensorService motionSensorService) {
        this.motionSensorService = motionSensorService;
    }

    public DeleteMotionSensorThread create(MotionSensor sensor) {
        DeleteMotionSensorThread thread = new DeleteMotionSensorThread(motionSensorService);
        thread.setMotionSensor(sensor);
        logger.info("DeleteMotionSensorThread created");

        return thread;
    }
}
