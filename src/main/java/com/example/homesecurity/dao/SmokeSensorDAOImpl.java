package com.example.homesecurity.dao;

import com.example.homesecurity.controller.ChooseLocationController;
import com.example.homesecurity.entity.SmokeSensor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class SmokeSensorDAOImpl implements SmokeSensorDAO{

    Logger logger= LoggerFactory.getLogger(SmokeSensorDAOImpl.class);

    private final EntityManager entityManager;

    @Autowired
    public SmokeSensorDAOImpl(EntityManager entityManager){

        this.entityManager=entityManager;
        logger.info("Saved Smoke Sensor object");
    }

    @Override
    @Transactional
    public void save(SmokeSensor sensor){
        entityManager.persist(sensor);
    }

    @Override
    public List<SmokeSensor> findAll() {

        String jpql="Select s FROM SmokeSensor s WHERE s.address.id = :id";
        TypedQuery<SmokeSensor> query= entityManager.createQuery(jpql, SmokeSensor.class);
        query.setParameter("id", ChooseLocationController.currentLocation.getId());
        logger.info("Retrieved all Smoke Sensors");

        return query.getResultList();
    }

    @Override
    public List<SmokeSensor> findByName(String name) {

        String jpql="Select s FROM SmokeSensor s WHERE s.deviceName= :name AND s.address.id = :id";
        TypedQuery<SmokeSensor> query= entityManager.createQuery(jpql, SmokeSensor.class);
        query.setParameter("id", ChooseLocationController.currentLocation.getId());
        query.setParameter("name", name);
        logger.info("Retrieved all Smoke Sensors by name");

        return query.getResultList();
    }

    @Override
    @Transactional
    public void updateSmokeSensor(SmokeSensor sensor){
        entityManager.merge(sensor);
        logger.info("Updated Smoke Sensor object");
    }

    @Override
    @Transactional
    public void deleteSmokeSensorById(Long id) {

        SmokeSensor tempSmokeSensor=entityManager.find(SmokeSensor.class, id);
        entityManager.remove(tempSmokeSensor);

        logger.info("Deleted Smoke Sensor object");
    }


}
