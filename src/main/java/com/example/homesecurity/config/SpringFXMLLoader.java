package com.example.homesecurity.config;

import javafx.fxml.FXMLLoader;
import javafx.util.Callback;
import org.springframework.context.ApplicationContext;

public class SpringFXMLLoader {

    private final ApplicationContext applicationContext;

    public SpringFXMLLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public FXMLLoader createLoader(String fxmlFile) {
        if (applicationContext == null) {
            throw new IllegalStateException("ApplicationContext is not initialized.");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        return fxmlLoader;
    }
}