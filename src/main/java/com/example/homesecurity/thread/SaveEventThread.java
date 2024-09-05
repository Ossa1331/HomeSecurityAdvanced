package com.example.homesecurity.thread;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.entity.Event;
import com.example.homesecurity.service.EventService;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveEventThread implements Runnable{

    private static final Logger logger= LoggerFactory.getLogger(CO2Sensor.class);

    private Event event;
    private final EventService eventService;

    public SaveEventThread(EventService eventService) {
        this.eventService = eventService;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public void run(){
        Platform.runLater(()->{
            try{
                eventService.saveEvent(event);
                logger.info("SaveEventThread running...");
            }catch(Exception e){
                logger.error("there has been an error in thread runtime. ", e);
            }
        });
    }
}
