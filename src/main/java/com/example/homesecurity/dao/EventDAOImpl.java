package com.example.homesecurity.dao;

import com.example.homesecurity.controller.ChooseLocationController;
import com.example.homesecurity.entity.Event;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class EventDAOImpl implements EventDAO{

    Logger logger= LoggerFactory.getLogger(EventDAOImpl.class);

    private final EntityManager entityManager;

    @Autowired
    public EventDAOImpl(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    @Override
    @Transactional
    public void save(Event event){
        entityManager.persist(event);
    }

    @Override
    public List<Event> findAll(){

        String jpql="Select e FROM Event e WHERE e.address.id = :id";
        TypedQuery<Event> query= entityManager.createQuery(jpql, Event.class);
        query.setParameter("id", ChooseLocationController.currentLocation.getId());
        logger.info("Retrieved all Events");

        return query.getResultList();
    }

    @Override
    @Transactional
    public void deleteAll(){
        String jpql = "DELETE FROM Event e";
        entityManager.createQuery(jpql).executeUpdate();

        logger.info("Dismissed all events from database");
    }
}
