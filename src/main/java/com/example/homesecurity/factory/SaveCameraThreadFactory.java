package com.example.homesecurity.factory;

import com.example.homesecurity.entity.Camera;
import com.example.homesecurity.entity.Change;
import com.example.homesecurity.service.CameraService;
import com.example.homesecurity.thread.SaveCameraThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveCameraThreadFactory {

    private static final Logger logger= LoggerFactory.getLogger(SaveCameraThreadFactory.class);

    private final CameraService cameraService;

    @Autowired
    public SaveCameraThreadFactory(CameraService cameraService) {
        this.cameraService = cameraService;
    }

    public SaveCameraThread create(Camera camera) {
        SaveCameraThread thread = new SaveCameraThread(cameraService);
        thread.setCamera(camera);
        logger.info("SaveCameraThread created");

        return thread;
    }
}