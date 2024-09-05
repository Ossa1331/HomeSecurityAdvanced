package com.example.homesecurity.service;

import com.example.homesecurity.dao.EventDAO;
import com.example.homesecurity.dao.SmokeSensorDAO;
import com.example.homesecurity.entity.Event;
import com.example.homesecurity.entity.SmokeSensor;
import com.example.homesecurity.util.IsDatabaseOperationInProgress;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmokeSensorService {
    private final SmokeSensorDAO smokeSensorDAO;

    private final EventDAO eventDAO;

    public SmokeSensorService(SmokeSensorDAO smokeSensorDAO, EventDAO eventDAO) {
        this.smokeSensorDAO = smokeSensorDAO;
        this.eventDAO = eventDAO;
    }

    public synchronized void saveSmokeSensor(SmokeSensor sensor) {
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
            smokeSensorDAO.save(sensor);
        } finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }

    public synchronized List<SmokeSensor> findAllSmokeSensors(){
        List<SmokeSensor> allSmokeSensors;
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
            allSmokeSensors=smokeSensorDAO.findAll();
        } finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
        return allSmokeSensors;
    }

    public synchronized void deleteSmokeSensor(SmokeSensor sensor){
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
            smokeSensorDAO.deleteSmokeSensorById(sensor.getDeviceId());
        }
        finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }

    public synchronized void updateSmokeSensor(SmokeSensor sensor){
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
            smokeSensorDAO.updateSmokeSensor(sensor);
        }
        finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }
    public synchronized void updateSmokeSensor(SmokeSensor sensor, Event event){
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
            smokeSensorDAO.updateSmokeSensor(sensor);
            eventDAO.save(event);
        }
        finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }
}
