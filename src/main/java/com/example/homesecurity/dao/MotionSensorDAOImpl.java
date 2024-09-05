package com.example.homesecurity.dao;

import com.example.homesecurity.controller.ChooseLocationController;
import com.example.homesecurity.entity.MotionSensor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public class MotionSensorDAOImpl implements MotionSensorDAO {

    Logger logger= LoggerFactory.getLogger(MotionSensorDAOImpl.class);
    private final EntityManager entityManager;

    @Autowired
    public MotionSensorDAOImpl(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    @Override
    @Transactional
    public void save(MotionSensor sensor){
        entityManager.persist(sensor);
        logger.info("Saved Motion Sensor object");
    }

    @Override
    public List<MotionSensor> findAll() {

        String jpql="Select s FROM MotionSensor s WHERE s.address.id = :id";
        TypedQuery<MotionSensor> query= entityManager.createQuery(jpql, MotionSensor.class);
        query.setParameter("id", ChooseLocationController.currentLocation.getId());
        logger.info("Retrieved all Motion Sensors");

        return query.getResultList();

    }

    @Override
    public List<MotionSensor> findByName(String name) {

        String jpql="Select s FROM MotionSensor s WHERE s.deviceName= :name AND s.address.id = :id";
        TypedQuery<MotionSensor> query= entityManager.createQuery(jpql, MotionSensor.class);
        query.setParameter("id", ChooseLocationController.currentLocation.getId());
        query.setParameter("name", name);
        logger.info("Retrieved all Motion Sensors by name");

        return query.getResultList();
    }
    @Override
    @Transactional
    public void updateMotionSensor(MotionSensor sensor){

        entityManager.merge(sensor);
        logger.info("Updated Motion Sensor object");
    }

    @Override
    @Transactional
    public void deleteMotionSensorById(Long id) {

        MotionSensor tempMotionSensor=entityManager.find(MotionSensor.class, id);
        entityManager.remove(tempMotionSensor);

        logger.info("Deleted Motion Sensor object");
    }
}
