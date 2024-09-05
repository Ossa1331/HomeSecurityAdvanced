package com.example.homesecurity.dao;

import com.example.homesecurity.entity.Event;

import java.util.List;

public interface EventDAO {

    void save(Event event);

    List<Event> findAll();

    void deleteAll();
}
