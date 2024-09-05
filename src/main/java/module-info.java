module com.example.homesecurity {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires java.sql;
    requires jakarta.persistence;
    requires spring.context;
    requires spring.orm;
    requires spring.jdbc;
    requires spring.data.jpa;
    requires spring.core;
    requires spring.beans;
    requires spring.tx;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires org.hibernate.orm.core;
    requires spring.aop;
    requires spring.boot.starter.aop;
    requires org.aspectj.weaver;

    opens com.example.homesecurity to javafx.fxml, spring.core, spring.beans, spring.context, spring.data.jpa;
    opens com.example.homesecurity.controller to javafx.fxml, spring.core, spring.beans, spring.context, spring.data.jpa;
    opens com.example.homesecurity.config to spring.core, spring.beans, spring.context;
    opens com.example.homesecurity.entity to org.hibernate.orm.core, spring.orm, spring.data.jpa, spring.core;
    opens com.example.homesecurity.dao to spring.core, spring.beans;
    opens com.example.homesecurity.factory to spring.beans, spring.core;

    exports com.example.homesecurity;
    exports com.example.homesecurity.controller;
    exports com.example.homesecurity.entity;
    exports com.example.homesecurity.dao;
    exports com.example.homesecurity.config;
    exports com.example.homesecurity.factory;
    exports com.example.homesecurity.thread to spring.beans;
    exports com.example.homesecurity.service;
}