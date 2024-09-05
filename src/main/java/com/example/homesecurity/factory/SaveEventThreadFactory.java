package com.example.homesecurity.factory;

import com.example.homesecurity.entity.Event;
import com.example.homesecurity.service.EventService;
import com.example.homesecurity.thread.SaveEventThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveEventThreadFactory {

    private static final Logger logger= LoggerFactory.getLogger(SaveEventThreadFactory.class);
    private final EventService eventService;

    @Autowired
    public SaveEventThreadFactory(EventService eventService) {
        this.eventService = eventService;
    }

    public SaveEventThread create(Event event) {
        SaveEventThread thread = new SaveEventThread(eventService);
        thread.setEvent(event);
        logger.info("SaveEventThread created");
        return thread;
    }
}
