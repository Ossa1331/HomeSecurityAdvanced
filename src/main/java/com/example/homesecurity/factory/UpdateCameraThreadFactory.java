package com.example.homesecurity.factory;

import com.example.homesecurity.entity.Camera;
import com.example.homesecurity.entity.Event;
import com.example.homesecurity.service.CameraService;
import com.example.homesecurity.thread.UpdateCameraThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateCameraThreadFactory {

    private static final Logger logger= LoggerFactory.getLogger(UpdateCameraThreadFactory.class);

    private final CameraService cameraService;

    @Autowired
    public UpdateCameraThreadFactory(CameraService cameraService) {
        this.cameraService = cameraService;
    }

    public UpdateCameraThread create(Camera camera) {
        UpdateCameraThread thread = new UpdateCameraThread(cameraService);
        thread.setCamera(camera);
        logger.info("UpdatedCameraThread created");

        return thread;
    }
    public UpdateCameraThread create(Camera camera, Event event){
        UpdateCameraThread thread= new UpdateCameraThread(cameraService);
        thread.setCamera(camera);
        thread.setEvent(event);
        logger.info("UpdataCameraThread created with event");

        return thread;
    }
}
