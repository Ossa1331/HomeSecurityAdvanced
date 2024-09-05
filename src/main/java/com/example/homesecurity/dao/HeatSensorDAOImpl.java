package com.example.homesecurity.dao;

import com.example.homesecurity.controller.ChooseLocationController;
import com.example.homesecurity.entity.HeatSensor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public class HeatSensorDAOImpl implements HeatSensorDAO {

    Logger logger= LoggerFactory.getLogger(HeatSensorDAOImpl.class);
    private final EntityManager entityManager;

    @Autowired
    public HeatSensorDAOImpl(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    @Override
    @Transactional
    public void save(HeatSensor sensor){

        entityManager.persist(sensor);
        logger.info("Heat Sensor successfully saved");
    }

    @Override
    public List<HeatSensor> findAll() {

        String jpql="Select s FROM HeatSensor s WHERE s.address.id = :id";
        TypedQuery<HeatSensor> query= entityManager.createQuery(jpql, HeatSensor.class);
        query.setParameter("id", ChooseLocationController.currentLocation.getId());
        logger.info("Retrieved all Heat Sensors");

        return query.getResultList();

    }

    @Override
    public List<HeatSensor> findByName(String name) {

        String jpql="Select s FROM HeatSensor s WHERE s.deviceName= :name AND s.address.id = :id";
        TypedQuery<HeatSensor> query= entityManager.createQuery(jpql, HeatSensor.class);
        query.setParameter("id", ChooseLocationController.currentLocation.getId());
        query.setParameter("name", name);
        logger.info("Retrieved all Heat Sensors by name");

        return query.getResultList();
    }

    @Override
    @Transactional
    public void updateHeatSensor(HeatSensor sensor){
        entityManager.merge(sensor);
        logger.info("Updated Heat Sensor object");
    }

    @Override
    @Transactional
    public void deleteHeatSensorById(Long id) {

        HeatSensor tempHeatSensor=entityManager.find(HeatSensor.class, id);
        entityManager.remove(tempHeatSensor);
        logger.info("Deleted Heat Sensor object");
    }
}
