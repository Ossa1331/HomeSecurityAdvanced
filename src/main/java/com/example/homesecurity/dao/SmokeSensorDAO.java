package com.example.homesecurity.dao;

import com.example.homesecurity.entity.SmokeSensor;
import jakarta.persistence.EntityManager;

import java.util.List;

public interface SmokeSensorDAO {

    void save(SmokeSensor sensor);

    List<SmokeSensor> findAll();

    List<SmokeSensor> findByName(String name);

    /*void updateStatus(SmokeSensor sensor);

    void updateMeasure(SmokeSensor sensor,Double newMeasure);

    void updateName(SmokeSensor sensor, String newName);
    */

    void updateSmokeSensor(SmokeSensor sensor);

    void deleteSmokeSensorById(Long id);

}
