package com.example.homesecurity.factory;

import com.example.homesecurity.entity.GlassBreakSensor;
import com.example.homesecurity.service.GlassBreakSensorService;
import com.example.homesecurity.thread.SaveGlassBreakSensorThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveGlassBreakSensorThreadFactory {

    private static final Logger logger= LoggerFactory.getLogger(SaveGlassBreakSensorThreadFactory.class);

    private final GlassBreakSensorService glassBreakSensorService;

    @Autowired
    public SaveGlassBreakSensorThreadFactory(GlassBreakSensorService glassBreakSensorService) {
        this.glassBreakSensorService = glassBreakSensorService;
    }

    public SaveGlassBreakSensorThread create(GlassBreakSensor sensor) {
        SaveGlassBreakSensorThread thread = new SaveGlassBreakSensorThread(glassBreakSensorService);
        thread.setSensor(sensor);
        logger.info("SaveGlassBreakSensorThread created");

        return thread;
    }
}
