package com.example.homesecurity.dao;

import com.example.homesecurity.entity.GlassBreakSensor;

import java.util.List;

public interface GlassBreakSensorDAO {

    void save(GlassBreakSensor sensor);

    List<GlassBreakSensor> findAll();

    List<GlassBreakSensor> findByName(String name);

   /* void updateStatus(GlassBreakSensor sensor);

    void updateMeasure(GlassBreakSensor sensor);

    void updateName(GlassBreakSensor sensor, String newName); */

    public void updateGlassBreakSensor(GlassBreakSensor sensor);

    void deleteGlassBreakSensorById(Long id);
}
