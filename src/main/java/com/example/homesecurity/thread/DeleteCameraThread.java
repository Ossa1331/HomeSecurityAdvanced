package com.example.homesecurity.thread;

import com.example.homesecurity.entity.Camera;
import com.example.homesecurity.service.CameraService;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteCameraThread implements Runnable{

    private static final Logger logger= LoggerFactory.getLogger(DeleteCameraThread.class);

    private Camera camera;
    private final CameraService cameraService;

    public DeleteCameraThread(CameraService cameraService) {
        this.cameraService = cameraService;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void run(){
        Platform.runLater(()->{
            try{
                cameraService.deleteCamera(camera);
                logger.info("DeleteCameraThread running...");
            }catch(Exception e){
                logger.error("there has been an error in thread runtime. ", e);
            }
        });
    }

}
