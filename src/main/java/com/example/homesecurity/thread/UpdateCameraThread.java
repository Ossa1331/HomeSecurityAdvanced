package com.example.homesecurity.thread;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.entity.Camera;
import com.example.homesecurity.entity.Event;
import com.example.homesecurity.service.CameraService;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateCameraThread implements Runnable{

    private static final Logger logger= LoggerFactory.getLogger(CO2Sensor.class);

    private Camera camera;

    private Event event;
    private final CameraService cameraService;

    public UpdateCameraThread(CameraService cameraService) {
        this.cameraService = cameraService;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setEvent(Event event){this.event=event;}

    @Override
    public void run(){
        Platform.runLater(()->{
            try{
                if(event==null){
                    cameraService.updateCamera(camera);
                    logger.info("UpdateCameraThread running...");
                }
                else{
                    cameraService.updateCamera(camera,event);
                    logger.info("UpdateCameraThread with event running...");
                }
            }catch(Exception e){
                logger.error("there has been an error in thread runtime. ", e);
            }
        });
    }
}
