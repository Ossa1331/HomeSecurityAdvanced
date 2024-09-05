package com.example.homesecurity.factory;

import com.example.homesecurity.entity.Event;
import com.example.homesecurity.entity.SmokeSensor;
import com.example.homesecurity.service.SmokeSensorService;
import com.example.homesecurity.thread.UpdateSmokeSensorThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateSmokeSensorThreadFactory {

    private static final Logger logger = LoggerFactory.getLogger(UpdateSmokeSensorThreadFactory.class);

    private final SmokeSensorService smokeSensorService;

    @Autowired
    public UpdateSmokeSensorThreadFactory(SmokeSensorService smokeSensorService) {
        this.smokeSensorService = smokeSensorService;
    }

    public UpdateSmokeSensorThread create(SmokeSensor sensor) {
        UpdateSmokeSensorThread thread = new UpdateSmokeSensorThread(smokeSensorService);
        thread.setSensor(sensor);
        logger.info("UpdateSmokeSensorThread created");

        return thread;
    }

    public UpdateSmokeSensorThread create(SmokeSensor sensor, Event event){
        UpdateSmokeSensorThread thread = new UpdateSmokeSensorThread(smokeSensorService);
        thread.setSensor(sensor);
        thread.setEvent(event);
        logger.info("UpdateSmokeSensorThread created with event");

        return thread;
    }

}
