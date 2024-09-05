package com.example.homesecurity.controller;

import com.example.homesecurity.HomeSecurityApplication;
import com.example.homesecurity.config.SpringFXMLLoader;
import com.example.homesecurity.exception.EmptyFieldException;
import com.example.homesecurity.exception.UserAlreadyExistsException;
import com.example.homesecurity.exception.WrongCredentialsException;
import com.example.homesecurity.service.AddressService;
import com.example.homesecurity.util.AuthenticationUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class LoginScreenController {

    private static final Logger logger= LoggerFactory.getLogger(LoginScreenController.class);

    public static Boolean userIdentified=false;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private CheckBox isAdministratorCheckBox;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ApplicationContext applicationContext;


    public void loginUser(){

        Platform.runLater(() -> {
            String username=usernameTextField.getText();
            String password=passwordPasswordField.getText();

            try{
                if(!username.isEmpty()&&!password.isEmpty()){
                    try{
                        if(AuthenticationUtil.authenticate(username,password)){
                            userIdentified=true;
                            showOwnerAddressesScreen();
                        }
                        else{
                            throw new WrongCredentialsException();
                        }

                    }
                    catch(WrongCredentialsException ex){

                        Platform.runLater(()->{
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Unsuccessful login attempt");
                            alert.setContentText("Please try again");
                            alert.showAndWait();
                        });
                    }
                }
                else{
                    throw new EmptyFieldException();
                }
            }catch(EmptyFieldException ex){

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Unsuccessful login attempt");
                alert.setContentText("Please fill in all of the fields.");
                logger.error("Some fields were left empty",ex);
                alert.showAndWait();

            }
        });
    }

    public void registerUser(){

        Platform.runLater(() -> {

            String username=usernameTextField.getText();
            String password=passwordPasswordField.getText();

            Boolean isAdmin=isAdministratorCheckBox.isSelected();

            try{
                if(!username.isEmpty()&&!password.isEmpty()){
                    try{
                        AuthenticationUtil.addUser(username,password,isAdmin);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Register Successful");
                        alert.setHeaderText("Register Successful");
                        alert.setContentText("You have been registered");
                        logger.info("Successful registration");
                        alert.showAndWait();
                    }
                   catch(UserAlreadyExistsException ex){
                       Alert alert = new Alert(Alert.AlertType.ERROR);
                       alert.setTitle("Error");
                       alert.setHeaderText("User already exists");
                       alert.setContentText("Please try again");
                       logger.info("User already exists");
                       alert.showAndWait();
                   }
                }
                else{
                    throw new EmptyFieldException();
                }
            } catch(EmptyFieldException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Unsuccessful register attempt");
                alert.setContentText("Please fill in all of the fields.");
                alert.showAndWait();
                logger.error("Some fields were left empty by the user", ex);
            }
        });

    }

    private void showOwnerAddressesScreen() {

        Platform.runLater(() -> {
            try {
                SpringFXMLLoader loader = new SpringFXMLLoader(HomeSecurityApplication.getApplicationContext());
                FXMLLoader fxmlLoader = loader.createLoader("/views/Choose-Location.fxml");

                Scene scene = new Scene(fxmlLoader.load(), 900, 600);
                HomeSecurityApplication.getMainStage().setTitle("Device Control");
                HomeSecurityApplication.getMainStage().setScene(scene);
                HomeSecurityApplication.getMainStage().show();
            }catch(IOException ex)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Unable to retrieve addresses from database");
                alert.setContentText("Please restart the application");
                logger.error("Unable to reach database", ex);
                alert.showAndWait();
            }
        });
    }
}
