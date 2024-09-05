package com.example.homesecurity.dao;

import com.example.homesecurity.controller.ChooseLocationController;
import com.example.homesecurity.entity.CO2Sensor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public class Co2SensorDAOImpl implements Co2SensorDAO {

    Logger logger= LoggerFactory.getLogger(Co2SensorDAOImpl.class);

    private final EntityManager entityManager;

    @Autowired
    public Co2SensorDAOImpl(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    @Override
    @Transactional
    public void save(CO2Sensor sensor){
        entityManager.persist(sensor);
    }

    @Override
    public List<CO2Sensor> findAll() {

        String jpql="Select s FROM CO2Sensor s WHERE s.address.id = :id";
        TypedQuery<CO2Sensor> query= entityManager.createQuery(jpql, CO2Sensor.class);
        query.setParameter("id", ChooseLocationController.currentLocation.getId());
        logger.info("Found all co2 sensors");

        return query.getResultList();
    }

    @Override
    public List<CO2Sensor> findByName(String name) {

        String jpql="Select s FROM CO2Sensor s WHERE s.deviceName= :name AND s.address.id = :id";
        TypedQuery<CO2Sensor> query= entityManager.createQuery(jpql, CO2Sensor.class);
        query.setParameter("name", name);
        query.setParameter("id", ChooseLocationController.currentLocation.getId());
        logger.info("Found all co2 sensors by name");

        return query.getResultList();
    }

    @Override
    @Transactional
    public void updateCO2Sensor(CO2Sensor sensor){

        entityManager.merge(sensor);
        logger.info("Updated a CO2Sensor object");
    }

    @Override
    @Transactional
    public void deleteCo2SensorById(Long id) {

        CO2Sensor tempCO2Sensor=entityManager.find(CO2Sensor.class, id);
        entityManager.remove(tempCO2Sensor);
        logger.info("Deleted a CO2Sensor object");
    }
}
