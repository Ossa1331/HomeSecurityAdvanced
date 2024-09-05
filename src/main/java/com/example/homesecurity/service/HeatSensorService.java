package com.example.homesecurity.service;

import com.example.homesecurity.dao.EventDAO;
import com.example.homesecurity.dao.HeatSensorDAO;
import com.example.homesecurity.entity.*;
import com.example.homesecurity.util.IsDatabaseOperationInProgress;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeatSensorService {
    private final HeatSensorDAO heatSensorDAO;

    private final EventDAO eventDAO;


    public HeatSensorService(HeatSensorDAO heatSensorDAO, EventDAO eventDAO) {

        this.heatSensorDAO = heatSensorDAO;
        this.eventDAO=eventDAO;
    }

    public synchronized void saveHeatSensor(HeatSensor sensor) {
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
            heatSensorDAO.save(sensor);
        } finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }
    public synchronized List<HeatSensor> findAllHeatSensors(){
        List<HeatSensor> allHeatSensors;
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
            allHeatSensors=heatSensorDAO.findAll();
        } finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
        return allHeatSensors;
    }

    public synchronized void deleteHeatSensor(HeatSensor sensor){
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
            heatSensorDAO.deleteHeatSensorById(sensor.getDeviceId());
        }
        finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }
    public synchronized void updateHeatSensor(HeatSensor sensor){
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
            heatSensorDAO.updateHeatSensor(sensor);
        }
        finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }
    public synchronized void updateHeatSensor(HeatSensor sensor, Event event){
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
            heatSensorDAO.updateHeatSensor(sensor);
            eventDAO.save(event);
        }
        finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }
}
