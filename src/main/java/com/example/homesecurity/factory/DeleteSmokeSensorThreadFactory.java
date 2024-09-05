package com.example.homesecurity.factory;

import com.example.homesecurity.entity.SmokeSensor;
import com.example.homesecurity.service.SmokeSensorService;
import com.example.homesecurity.thread.DeleteSmokeSensorThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteSmokeSensorThreadFactory {

    private static final Logger logger= LoggerFactory.getLogger(DeleteSmokeSensorThreadFactory.class);

    private final SmokeSensorService smokeSensorService;

    @Autowired
    public DeleteSmokeSensorThreadFactory(SmokeSensorService smokeSensorService) {
        this.smokeSensorService = smokeSensorService;
    }

    public DeleteSmokeSensorThread create(SmokeSensor sensor) {
        DeleteSmokeSensorThread thread = new DeleteSmokeSensorThread(smokeSensorService);
        thread.setSmokeSensor(sensor);
        logger.info("DeleteSmokeSensorThread created");

        return thread;
    }
}
