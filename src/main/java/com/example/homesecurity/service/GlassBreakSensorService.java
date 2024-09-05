package com.example.homesecurity.service;

import com.example.homesecurity.dao.EventDAO;
import com.example.homesecurity.dao.GlassBreakSensorDAO;
import com.example.homesecurity.entity.Event;
import com.example.homesecurity.entity.GlassBreakSensor;
import com.example.homesecurity.util.IsDatabaseOperationInProgress;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlassBreakSensorService {

    private final GlassBreakSensorDAO glassBreakSensorDAO;

    private final EventDAO eventDAO;

    public GlassBreakSensorService(GlassBreakSensorDAO glassBreakSensorDAO, EventDAO eventDAO) {
        this.glassBreakSensorDAO = glassBreakSensorDAO;
        this.eventDAO=eventDAO;
    }

    public synchronized void saveGlassBreakSensor(GlassBreakSensor sensor) {
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
            glassBreakSensorDAO.save(sensor);
        } finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }
    public synchronized List<GlassBreakSensor> findAllGlassBreakSensors(){
        List<GlassBreakSensor> allGlassBreakSensors;
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
            allGlassBreakSensors=glassBreakSensorDAO.findAll();
        } finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
        return allGlassBreakSensors;
    }

    public synchronized void deleteGlassBreakSensor(GlassBreakSensor sensor){
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
            glassBreakSensorDAO.deleteGlassBreakSensorById(sensor.getDeviceId());
        }
        finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }

    public synchronized void updateGlassBreakSensor(GlassBreakSensor sensor){
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
            glassBreakSensorDAO.updateGlassBreakSensor(sensor);
        }
        finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }
    public synchronized void updateGlassBreakSensor(GlassBreakSensor sensor, Event event){
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
            glassBreakSensorDAO.updateGlassBreakSensor(sensor);
            eventDAO.save(event);
        }
        finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }
}
