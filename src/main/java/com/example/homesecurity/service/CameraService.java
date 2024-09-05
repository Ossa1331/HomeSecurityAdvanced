package com.example.homesecurity.service;

import com.example.homesecurity.dao.CameraDAO;
import com.example.homesecurity.dao.EventDAO;
import com.example.homesecurity.entity.Camera;
import com.example.homesecurity.entity.Event;
import com.example.homesecurity.util.IsDatabaseOperationInProgress;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CameraService {

    private final CameraDAO cameraDAO;

    private final EventDAO eventDAO;

    public CameraService(CameraDAO cameraDAO, EventDAO eventDAO) {

        this.cameraDAO = cameraDAO;
        this.eventDAO=eventDAO;
    }

    public synchronized void saveCamera(Camera camera) {
        while (IsDatabaseOperationInProgress.isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted", ex);
            }
        }
        IsDatabaseOperationInProgress.isDatabaseOperationInProgress = true;
        try {
            cameraDAO.save(camera);
        } finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }

    public synchronized List<Camera> findAllCameras(){
        List<Camera> allCameras;
        while(IsDatabaseOperationInProgress.isDatabaseOperationInProgress){
            try {
                wait();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted", ex);
            }
        }
        IsDatabaseOperationInProgress.isDatabaseOperationInProgress = true;
        try {
            allCameras=cameraDAO.findAll();
        } finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
        return allCameras;
    }

    public synchronized void deleteCamera(Camera camera){
        while(IsDatabaseOperationInProgress.isDatabaseOperationInProgress){
            try {
                wait();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted", ex);
            }
        }
        IsDatabaseOperationInProgress.isDatabaseOperationInProgress = true;
        try{
            cameraDAO.deleteCameraById(camera.getDeviceId());
        }
        finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }
    public synchronized void updateCamera(Camera camera){
        while(IsDatabaseOperationInProgress.isDatabaseOperationInProgress){
            try {
                wait();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted", ex);
            }
        }
        IsDatabaseOperationInProgress.isDatabaseOperationInProgress = true;
        try{
            cameraDAO.updateCamera(camera);
        }
        finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }
    public synchronized void updateCamera(Camera camera, Event event){
        while(IsDatabaseOperationInProgress.isDatabaseOperationInProgress){
            try {
                wait();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted", ex);
            }
        }
        IsDatabaseOperationInProgress.isDatabaseOperationInProgress = true;
        try{
            cameraDAO.updateCamera(camera);
            eventDAO.save(event);
        }
        finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }
}