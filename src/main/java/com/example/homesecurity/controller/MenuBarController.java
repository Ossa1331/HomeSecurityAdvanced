package com.example.homesecurity.controller;

import com.example.homesecurity.HomeSecurityApplication;
import com.example.homesecurity.config.SpringFXMLLoader;
import com.example.homesecurity.exception.NoPrivilegeException;
import com.example.homesecurity.util.AuthenticationUtil;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class MenuBarController {

    private static final Logger logger= LoggerFactory.getLogger(MenuBarController.class);

    public void showAllDevicesScreen() {
        Platform.runLater(()->{
            SpringFXMLLoader loader = new SpringFXMLLoader(HomeSecurityApplication.getApplicationContext());

            FXMLLoader fxmlLoader = loader.createLoader("/views/all-devices.fxml");

            fxmlLoader.setControllerFactory(HomeSecurityApplication.getApplicationContext()::getBean);
            try {
                Scene scene = new Scene(fxmlLoader.load(), 900, 600);
                HomeSecurityApplication.getMainStage().setTitle("Device Control");
                HomeSecurityApplication.getMainStage().setScene(scene);
                HomeSecurityApplication.getMainStage().show();
            }catch(IOException e)
            {
                throw new RuntimeException();
            }
        });


    }
    public void showInsertCameraScreen() {
        Platform.runLater(()->{
            try{
                if(AuthenticationUtil.currentUser.getAdministrator()){
                    SpringFXMLLoader loader = new SpringFXMLLoader(HomeSecurityApplication.getApplicationContext());

                    FXMLLoader fxmlLoader = loader.createLoader("/views/Insert-Camera.fxml");

                    fxmlLoader.setControllerFactory(HomeSecurityApplication.getApplicationContext()::getBean);
                    try {
                        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
                        HomeSecurityApplication.getMainStage().setTitle("Insert Camera");
                        HomeSecurityApplication.getMainStage().setScene(scene);
                        HomeSecurityApplication.getMainStage().show();
                    }catch(IOException e)
                    {
                        throw new RuntimeException();
                    }
                }
                else{
                    throw new NoPrivilegeException();
                }
            }catch (NoPrivilegeException ex){

                logger.error("No privileges to access Insert Camera",ex);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No privileges for this account");
                alert.setContentText("Please log in as an administrator");
                alert.showAndWait();

            }
        });


    }
    public void showInsertGlassBreakScreen() {
        Platform.runLater(()->{
            try{
                if(AuthenticationUtil.currentUser.getAdministrator()){
                    SpringFXMLLoader loader = new SpringFXMLLoader(HomeSecurityApplication.getApplicationContext());

                    FXMLLoader fxmlLoader = loader.createLoader("/views/Insert-GlassBreak.fxml");

                    fxmlLoader.setControllerFactory(HomeSecurityApplication.getApplicationContext()::getBean);
                    try {
                        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
                        HomeSecurityApplication.getMainStage().setTitle("Insert Glass Break Sensor");
                        HomeSecurityApplication.getMainStage().setScene(scene);
                        HomeSecurityApplication.getMainStage().show();
                    }catch(IOException e)
                    {
                        throw new RuntimeException();
                    }
                }
                else{
                    throw new NoPrivilegeException();
                }
            }catch(NoPrivilegeException ex){

                logger.error("No privileges to access Insert Glass Break Sensor Screen",ex);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No privileges for this account");
                alert.setContentText("Please log in as an administrator");
                alert.showAndWait();

            }
        });

    }
    public void showCO2Screen() {
        Platform.runLater(()->{
            try{
                if(AuthenticationUtil.currentUser.getAdministrator()){
                    SpringFXMLLoader loader = new SpringFXMLLoader(HomeSecurityApplication.getApplicationContext());

                    FXMLLoader fxmlLoader = loader.createLoader("/views/Insert-CO2.fxml");

                    fxmlLoader.setControllerFactory(HomeSecurityApplication.getApplicationContext()::getBean);
                    try {
                        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
                        HomeSecurityApplication.getMainStage().setTitle("Insert CO2 Sensor");
                        HomeSecurityApplication.getMainStage().setScene(scene);
                        HomeSecurityApplication.getMainStage().show();
                    }catch(IOException e)
                    {
                        throw new RuntimeException();
                    }
                }
                else{
                    throw new NoPrivilegeException();
                }
            }catch(NoPrivilegeException ex){

                logger.error("No privileges to access Insert CO2 Sensor Screen",ex);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No privileges for this account");
                alert.setContentText("Please log in as an administrator");
                alert.showAndWait();

            }
        });


    }
    public void showMotionScreen() {
        Platform.runLater(()->{
            try{
                if(AuthenticationUtil.currentUser.getAdministrator()){
                    SpringFXMLLoader loader = new SpringFXMLLoader(HomeSecurityApplication.getApplicationContext());

                    FXMLLoader fxmlLoader = loader.createLoader("/views/Insert-Motion.fxml");

                    fxmlLoader.setControllerFactory(HomeSecurityApplication.getApplicationContext()::getBean);
                    try {
                        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
                        HomeSecurityApplication.getMainStage().setTitle("Insert Motion Sensor");
                        HomeSecurityApplication.getMainStage().setScene(scene);
                        HomeSecurityApplication.getMainStage().show();
                    }catch(IOException e)
                    {
                        throw new RuntimeException();
                    }
                }
                else{
                    throw new NoPrivilegeException();
                }
            } catch(NoPrivilegeException ex){

                logger.error("No privileges to access Insert Motion Sensor Screen", ex);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No privileges for this account");
                alert.setContentText("Please log in as an administrator");
                alert.showAndWait();

            }

        });

    }
    public void showHeatScreen() {
        Platform.runLater(()->{
            try{
                if(AuthenticationUtil.currentUser.getAdministrator()){
                    SpringFXMLLoader loader = new SpringFXMLLoader(HomeSecurityApplication.getApplicationContext());

                    FXMLLoader fxmlLoader = loader.createLoader("/views/Insert-Heat.fxml");

                    fxmlLoader.setControllerFactory(HomeSecurityApplication.getApplicationContext()::getBean);
                    try {
                        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
                        HomeSecurityApplication.getMainStage().setTitle("Insert Heat Sensor");
                        HomeSecurityApplication.getMainStage().setScene(scene);
                        HomeSecurityApplication.getMainStage().show();
                    }catch(IOException e)
                    {
                        throw new RuntimeException();
                    }
                }
                else{
                    throw new NoPrivilegeException();
                }
            }catch(NoPrivilegeException ex){

                logger.error("No privileges to access Insert Heat Sensor Screen",ex);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No privileges for this account");
                alert.setContentText("Please log in as an administrator");
                alert.showAndWait();

            }
        });


    }
    public void showSmokeScreen() {
        Platform.runLater(()->{
            try{
                if(AuthenticationUtil.currentUser.getAdministrator()){
                    SpringFXMLLoader loader = new SpringFXMLLoader(HomeSecurityApplication.getApplicationContext());

                    FXMLLoader fxmlLoader = loader.createLoader("/views/Insert-Smoke.fxml");

                    fxmlLoader.setControllerFactory(HomeSecurityApplication.getApplicationContext()::getBean);
                    try {
                        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
                        HomeSecurityApplication.getMainStage().setTitle("Insert Smoke Sensor");
                        HomeSecurityApplication.getMainStage().setScene(scene);
                        HomeSecurityApplication.getMainStage().show();
                    }catch(IOException e)
                    {
                        throw new RuntimeException();
                    }
                }
                else{
                    throw new NoPrivilegeException();
                }
            }catch(NoPrivilegeException ex){

                logger.error("No privileges to access Insert Smoke Sensor Screen",ex);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No privileges for this account");
                alert.setContentText("Please log in as an administrator");
                alert.showAndWait();

            }

        });

    }
    public void showChangeLogScreen() {
        Platform.runLater(()->{
            SpringFXMLLoader loader = new SpringFXMLLoader(HomeSecurityApplication.getApplicationContext());

            FXMLLoader fxmlLoader = loader.createLoader("/views/Change-Log.fxml");

            fxmlLoader.setControllerFactory(HomeSecurityApplication.getApplicationContext()::getBean);
            try {
                Scene scene = new Scene(fxmlLoader.load(), 900, 600);
                HomeSecurityApplication.getMainStage().setTitle("Measure Results");
                HomeSecurityApplication.getMainStage().setScene(scene);
                HomeSecurityApplication.getMainStage().show();
            }catch(IOException e)
            {
                throw new RuntimeException();
            }
        });

    }
    public void showMeasureScreen() {
        Platform.runLater(()->{
            SpringFXMLLoader loader = new SpringFXMLLoader(HomeSecurityApplication.getApplicationContext());

            FXMLLoader fxmlLoader = loader.createLoader("/views/Measure-Results.fxml");

            fxmlLoader.setControllerFactory(HomeSecurityApplication.getApplicationContext()::getBean);
            try {
                Scene scene = new Scene(fxmlLoader.load(), 900, 600);
                HomeSecurityApplication.getMainStage().setTitle("Measure Results");
                HomeSecurityApplication.getMainStage().setScene(scene);
                HomeSecurityApplication.getMainStage().show();
            }catch(IOException e)
            {
                throw new RuntimeException();
            }
        });

    }
    public void showEventScreen() {
        Platform.runLater(()->{
            SpringFXMLLoader loader = new SpringFXMLLoader(HomeSecurityApplication.getApplicationContext());

            FXMLLoader fxmlLoader = loader.createLoader("/views/Event-Log.fxml");

            fxmlLoader.setControllerFactory(HomeSecurityApplication.getApplicationContext()::getBean);
            try {
                Scene scene = new Scene(fxmlLoader.load(), 900, 600);
                HomeSecurityApplication.getMainStage().setTitle("Event Log");
                HomeSecurityApplication.getMainStage().setScene(scene);
                HomeSecurityApplication.getMainStage().show();
            }catch(IOException e)
            {
                throw new RuntimeException();
            }
        });

    }
    public void showLogInScreen() {
        Platform.runLater(()->{
            SpringFXMLLoader loader = new SpringFXMLLoader(HomeSecurityApplication.getApplicationContext());

            FXMLLoader fxmlLoader = loader.createLoader("/views/Login-Screen.fxml");

            fxmlLoader.setControllerFactory(HomeSecurityApplication.getApplicationContext()::getBean);
            try {
                Scene scene = new Scene(fxmlLoader.load(), 900, 600);
                HomeSecurityApplication.getMainStage().setTitle("Please Log In");
                HomeSecurityApplication.getMainStage().setScene(scene);
                HomeSecurityApplication.getMainStage().show();
            }catch(IOException e)
            {
                throw new RuntimeException();
            }
        });

    }
}
