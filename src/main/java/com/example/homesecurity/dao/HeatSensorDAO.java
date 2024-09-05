package com.example.homesecurity.dao;

import com.example.homesecurity.entity.HeatSensor;

import java.util.List;

public interface HeatSensorDAO {

    void save(HeatSensor sensor);

    List<HeatSensor> findAll();

    List<HeatSensor> findByName(String name);

    /*void updateStatus(HeatSensor sensor);

    void updateMeasure(HeatSensor sensor, Double newMeasure);

    void updateName(HeatSensor sensor, String newName);

     */

    void updateHeatSensor(HeatSensor sensor);

    void deleteHeatSensorById(Long id);
}
