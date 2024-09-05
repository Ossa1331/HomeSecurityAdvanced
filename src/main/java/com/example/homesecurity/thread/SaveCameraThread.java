package com.example.homesecurity.thread;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.entity.Camera;
import com.example.homesecurity.service.CameraService;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveCameraThread implements Runnable {
    private static final Logger logger= LoggerFactory.getLogger(CO2Sensor.class);

    private Camera camera;
    private final CameraService cameraService;

    public SaveCameraThread(CameraService cameraService) {
        this.cameraService = cameraService;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void run(){
        Platform.runLater(()->{
            try{
                cameraService.saveCamera(camera);
                logger.info("SaveCameraThread running...");
            }catch(Exception e){
                logger.error("there has been an error in thread runtime. ", e);
            }
        });
    }
}
