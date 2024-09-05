package com.example.homesecurity.dao;

import com.example.homesecurity.controller.ChooseLocationController;
import com.example.homesecurity.entity.Camera;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CameraDAOImpl implements CameraDAO{

    Logger logger= LoggerFactory.getLogger(CameraDAOImpl.class);

    private final EntityManager entityManager;

    @Autowired
    public CameraDAOImpl(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    @Override
    @Transactional
    public void save(Camera camera){
        entityManager.persist(camera);
    }

    @Override
    public List<Camera> findAll() {

        String jpql="Select c FROM Camera c WHERE c.address.id=:id";
        TypedQuery<Camera> query= entityManager.createQuery(jpql, Camera.class);
        query.setParameter("id", ChooseLocationController.currentLocation.getId());

        logger.info("Found all cameras");

        return query.getResultList();
    }

    @Override
    public List<Camera> findByName(String name) {

        String jpql="Select c FROM Camera c WHERE c.deviceName= :name AND c.address.id=:id";
        TypedQuery<Camera> query= entityManager.createQuery(jpql, Camera.class);
        query.setParameter("name", name);
        query.setParameter("id", ChooseLocationController.currentLocation.getId());

        logger.info("Found all cameras by name");

        return query.getResultList();
    }

    @Override
    @Transactional
    public void updateCamera(Camera camera){

        entityManager.merge(camera);
        logger.info("Updated a camera object");
    }

    @Override
    @Transactional
    public void deleteCameraById(Long id) {

        Camera tempCamera=entityManager.find(Camera.class, id);
        entityManager.remove(tempCamera);

        logger.info("Deleted a camera object");
    }
}
