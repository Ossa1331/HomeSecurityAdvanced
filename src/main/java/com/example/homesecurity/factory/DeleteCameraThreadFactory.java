package com.example.homesecurity.factory;

import com.example.homesecurity.entity.Camera;
import com.example.homesecurity.service.CameraService;
import com.example.homesecurity.thread.DeleteCameraThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteCameraThreadFactory {

    private static final Logger logger= LoggerFactory.getLogger(DeleteCameraThreadFactory.class);

    private final CameraService cameraService;

    @Autowired
    public DeleteCameraThreadFactory(CameraService cameraService) {
        this.cameraService = cameraService;
    }

    public DeleteCameraThread create(Camera camera) {
        DeleteCameraThread thread = new DeleteCameraThread(cameraService);
        thread.setCamera(camera);
        logger.info("DeleteCameraThread created");

        return thread;
    }
}