package com.example.homesecurity.dao;

import com.example.homesecurity.entity.Address;
import com.example.homesecurity.util.AuthenticationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class AddressDAOImpl implements AddressDAO{

    Logger logger= LoggerFactory.getLogger(AddressDAOImpl.class);

    private final EntityManager entityManager;
    @Autowired
    public AddressDAOImpl(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    @Override
    @Transactional
    public void save(Address address) {
        entityManager.persist(address);

        logger.info("entityManager saved Address");
    }

    @Override
    public List<Address> findAll() {
        String jpql="Select a FROM Address a";
        TypedQuery<Address> query= entityManager.createQuery(jpql, Address.class);

        logger.info("Successfully found all addresses");
        return query.getResultList();
    }

    @Override
    @Transactional
    public void deleteAddress(Long id) {

        String jpql = "DELETE FROM Camera c WHERE address.id=:id";
        entityManager.createQuery(jpql).setParameter("id", id).executeUpdate();
        jpql = "DELETE FROM CO2Sensor c WHERE address.id=:id";
        entityManager.createQuery(jpql).setParameter("id", id).executeUpdate();
        jpql = "DELETE FROM GlassBreakSensor g WHERE address.id=:id";
        entityManager.createQuery(jpql).setParameter("id", id).executeUpdate();
        jpql = "DELETE FROM HeatSensor h WHERE address.id=:id";
        entityManager.createQuery(jpql).setParameter("id", id).executeUpdate();
        jpql = "DELETE FROM MotionSensor m WHERE address.id=:id";
        entityManager.createQuery(jpql).setParameter("id", id).executeUpdate();
        jpql = "DELETE FROM SmokeSensor s WHERE address.id=:id";
        entityManager.createQuery(jpql).setParameter("id", id).executeUpdate();
        jpql = "DELETE FROM Event e WHERE address.id=:id";
        entityManager.createQuery(jpql).setParameter("id", id).executeUpdate();

        Address tempAddress=entityManager.find(Address.class, id);
        entityManager.remove(tempAddress);

        logger.info("Successfully deleted an address");
    }

    @Override
    public List<Address> findByUsername(){

        if(AuthenticationUtil.currentUser.getAdministrator()){
            String jpql="Select a FROM Address a";
            TypedQuery<Address> query=entityManager.createQuery(jpql, Address.class);
            return query.getResultList();
        }
        else{
            String jpql="Select a FROM Address a WHERE a.ownerUsername = :username";
            TypedQuery<Address> query= entityManager.createQuery(jpql, Address.class);
            query.setParameter("username", AuthenticationUtil.currentUser.getUsername());
            return query.getResultList();
        }
    }
}
