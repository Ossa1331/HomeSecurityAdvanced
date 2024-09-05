package com.example.homesecurity.thread;

import com.example.homesecurity.service.EventService;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteEventsThread implements Runnable {

    private static final Logger logger= LoggerFactory.getLogger(DeleteEventsThread.class);

    private final EventService eventService;

    public DeleteEventsThread(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void run(){
        Platform.runLater(()->{
            try{
                eventService.deleteAllEvents();
                logger.info("DeleteEventsThread running...");
            }catch(Exception e){
                logger.error("there has been an error in thread runtime. ", e);
            }
        });
    }
}
