package com.example.homesecurity.dao;

import com.example.homesecurity.controller.ChooseLocationController;
import com.example.homesecurity.entity.GlassBreakSensor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class GlassBreakSensorDAOImpl implements GlassBreakSensorDAO {

    Logger logger= LoggerFactory.getLogger(GlassBreakSensorDAOImpl.class);

    private final EntityManager entityManager;

    @Autowired
    public GlassBreakSensorDAOImpl(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    @Override
    @Transactional
    public void save(GlassBreakSensor sensor){

        entityManager.persist(sensor);
        logger.info("Saved GlassBreakSensor successfully");
    }

    @Override
    public List<GlassBreakSensor> findAll() {

        String jpql="Select s FROM GlassBreakSensor s WHERE s.address.id= :id";
        TypedQuery<GlassBreakSensor> query= entityManager.createQuery(jpql, GlassBreakSensor.class);
        query.setParameter("id", ChooseLocationController.currentLocation.getId());
        logger.info("Retrieved all Glass Break Sensors");

        return query.getResultList();

    }

    @Override
    public List<GlassBreakSensor> findByName(String name) {

        String jpql="Select s FROM GlassBreakSensor s WHERE s.deviceName= :name AND s.address.id = :id";
        TypedQuery<GlassBreakSensor> query= entityManager.createQuery(jpql, GlassBreakSensor.class);
        query.setParameter("id", ChooseLocationController.currentLocation.getId());
        query.setParameter("name", name);
        logger.info("Found all Glass Break Sensors by name");

        return query.getResultList();
    }

    @Override
    @Transactional
    public void updateGlassBreakSensor(GlassBreakSensor sensor){

        entityManager.merge(sensor);
        logger.info("Updated Glass Break Successfully");
    }

    @Override
    @Transactional
    public void deleteGlassBreakSensorById(Long id) {

        GlassBreakSensor tempGlassBreakSensor =entityManager.find(GlassBreakSensor.class, id);
        entityManager.remove(tempGlassBreakSensor);

        logger.info("Deleted Glass Break Sensor object");
    }
}
