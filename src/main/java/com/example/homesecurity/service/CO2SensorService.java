package com.example.homesecurity.service;

import com.example.homesecurity.dao.Co2SensorDAO;
import com.example.homesecurity.dao.EventDAO;
import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.entity.Event;
import com.example.homesecurity.util.IsDatabaseOperationInProgress;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CO2SensorService {

    private final Co2SensorDAO co2SensorDAO;

    private final EventDAO eventDAO;

    public CO2SensorService(Co2SensorDAO co2SensorDAO, EventDAO eventDAO) {
        this.co2SensorDAO = co2SensorDAO;
        this.eventDAO=eventDAO;
    }

    public synchronized void saveCO2Sensor(CO2Sensor sensor) {
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
            co2SensorDAO.save(sensor);
        } finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }
    public synchronized List<CO2Sensor> findAllCO2Sensors(){
        List<CO2Sensor> allCO2Sensors;
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
            allCO2Sensors=co2SensorDAO.findAll();
        } finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
        return allCO2Sensors;
    }
    public synchronized void deleteCO2Sensor(CO2Sensor sensor){
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
            co2SensorDAO.deleteCo2SensorById(sensor.getDeviceId());
        }
        finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }
    public synchronized void updateCO2Sensor(CO2Sensor sensor){
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
            co2SensorDAO.updateCO2Sensor(sensor);
        }
        finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }

    public synchronized void updateCO2Sensor(CO2Sensor sensor, Event event){
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
            co2SensorDAO.updateCO2Sensor(sensor);
            eventDAO.save(event);
        }
        finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }
}
