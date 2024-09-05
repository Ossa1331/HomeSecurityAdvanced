package com.example.homesecurity;

import com.example.homesecurity.config.SpringFXMLLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.IOException;


public class HomeSecurityApplication extends Application {

    private static ApplicationContext applicationContext;

    public ConfigurableApplicationContext springContext;
    private static Stage mainStage;

    @Override
    public void init(){
        System.out.println("Starting Spring Boot application...");
        SpringApplicationBuilder builder= new SpringApplicationBuilder(SpringBootHelperApp.class);
        springContext=builder.application().run();
        applicationContext = springContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void start(Stage stage) throws IOException {

        SpringFXMLLoader loader = new SpringFXMLLoader(applicationContext);

        FXMLLoader fxmlLoader = loader.createLoader("/views/Login-Screen.fxml");

        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent root= fxmlLoader.load();
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Home Security");
        stage.setScene(scene);
        stage.show();

        mainStage=stage;
    }

    @Override
    public void stop(){

        if(springContext!=null)
            springContext.close();
    }

    public static Stage getMainStage(){
        return mainStage;
    }

    public static void main(String[] args) {
        launch(args);

    }
}