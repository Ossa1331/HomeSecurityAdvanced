package com.example.homesecurity.dao;

import com.example.homesecurity.entity.MotionSensor;

import java.util.List;

public interface MotionSensorDAO {

    void save(MotionSensor sensor);

    List<MotionSensor> findAll();

    List<MotionSensor> findByName(String name);

    /*void updateStatus(MotionSensor sensor);

    void updateMeasure(MotionSensor sensor);

    void updateName(MotionSensor sensor, String newName);

     */

    void updateMotionSensor(MotionSensor sensor);
    void deleteMotionSensorById(Long id);
}
