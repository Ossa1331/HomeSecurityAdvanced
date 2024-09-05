package com.example.homesecurity.dao;

import com.example.homesecurity.entity.CO2Sensor;

import java.util.List;

public interface Co2SensorDAO {

    void save(CO2Sensor sensor);

    List<CO2Sensor> findAll();

    List<CO2Sensor> findByName(String name);

    /*void updateStatus(CO2Sensor sensor);

    void updateMeasure(CO2Sensor sensor, Double newMeasure);

    void updateName(CO2Sensor sensor, String newName);
    */

    void updateCO2Sensor(CO2Sensor sensor);

    void deleteCo2SensorById(Long id);
}
