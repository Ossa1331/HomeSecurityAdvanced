package com.example.homesecurity.factory;

import com.example.homesecurity.entity.Event;
import com.example.homesecurity.entity.GlassBreakSensor;
import com.example.homesecurity.service.GlassBreakSensorService;
import com.example.homesecurity.thread.UpdateGlassBreakSensorThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateGlassBreakSensorThreadFactory {

    private static final Logger logger= LoggerFactory.getLogger(UpdateGlassBreakSensorThreadFactory.class);

    private final GlassBreakSensorService glassBreakSensorService;

    @Autowired
    public UpdateGlassBreakSensorThreadFactory(GlassBreakSensorService glassBreakSensorService) {
        this.glassBreakSensorService = glassBreakSensorService;
    }

    public UpdateGlassBreakSensorThread create(GlassBreakSensor sensor) {
        UpdateGlassBreakSensorThread thread = new UpdateGlassBreakSensorThread(glassBreakSensorService);
        thread.setSensor(sensor);
        logger.info("UpdateGlassBreakSensorThread created");

        return thread;
    }

    public UpdateGlassBreakSensorThread create(GlassBreakSensor sensor, Event event){
        UpdateGlassBreakSensorThread thread= new UpdateGlassBreakSensorThread(glassBreakSensorService);
        thread.setSensor(sensor);
        thread.setEvent(event);
        logger.info("UpdateGlassBreakSensorThread created with event");

        return thread;
    }

}
