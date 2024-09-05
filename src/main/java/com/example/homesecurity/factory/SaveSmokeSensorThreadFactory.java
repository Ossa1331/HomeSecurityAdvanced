package com.example.homesecurity.factory;

import com.example.homesecurity.entity.SmokeSensor;
import com.example.homesecurity.service.SmokeSensorService;
import com.example.homesecurity.thread.SaveSmokeSensorThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveSmokeSensorThreadFactory {

    private static final Logger logger= LoggerFactory.getLogger(SaveSmokeSensorThreadFactory.class);
    private final SmokeSensorService smokeSensorService;

    @Autowired
    public SaveSmokeSensorThreadFactory(SmokeSensorService smokeSensorService) {
        this.smokeSensorService = smokeSensorService;
    }

    public SaveSmokeSensorThread create(SmokeSensor sensor) {
        SaveSmokeSensorThread thread = new SaveSmokeSensorThread(smokeSensorService);
        thread.setSensor(sensor);
        logger.info("SaveSmokeSensorThread created");

        return thread;
    }
}
