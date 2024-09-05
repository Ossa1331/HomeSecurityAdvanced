package com.example.homesecurity.controller;

import com.example.homesecurity.HomeSecurityApplication;
import com.example.homesecurity.config.SpringFXMLLoader;
import com.example.homesecurity.entity.Address;
import com.example.homesecurity.entity.Device;
import com.example.homesecurity.factory.DeleteAddressThreadFactory;
import com.example.homesecurity.factory.SaveAddressThreadFactory;
import com.example.homesecurity.service.AddressService;
import com.example.homesecurity.thread.SaveAddressThread;
import com.example.homesecurity.util.AuthenticationUtil;
import com.example.homesecurity.thread.DeleteAddressThread;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Controller
public class ChooseLocationController {


    @Autowired
    private  AddressService addressService;

    @Autowired
    private SaveAddressThreadFactory saveAddressThreadFactory;

    @Autowired
    private DeleteAddressThreadFactory deleteAddressThreadFactory;

    private static final Logger logger= LoggerFactory.getLogger(AllDevicesController.class);

    public static Address currentLocation;


    @FXML
    private TableView<Address> addressTableView;

    @FXML
    private TableColumn<Address, String> AddressStreetTableColumn;

    @FXML
    private TableColumn<Address, String> AddressHouseNumberTableColumn;

    @FXML
    private TableColumn<Address, String> AddressCityTableColumn;

    @FXML
    private TableColumn<Address, String> AddressPostalCodeTableColumn;

    public void initialize(){


        AddressStreetTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getStreet()));

        AddressHouseNumberTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getHouseNumber().toString()));

        AddressCityTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getCity()));

        AddressStreetTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getStreet()));

        AddressPostalCodeTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getPostalCode().toString()));

        List<Address> allAddresses = addressService.findAddressesByUsername();

        ObservableList<Address> allAddressesObservable = FXCollections.observableList(allAddresses);

        if(!allAddressesObservable.isEmpty()) {

            addressTableView.setItems(allAddressesObservable);

        }
    }
    @FXML
    public void chooseAddress(){
        Optional<Address> clickedAddress=getClickedAddress();
        clickedAddress.ifPresent(address -> currentLocation = address);

        showAllDevicesScreen();
    }
    @FXML
    public void newAddress(){
        Dialog<Address> dialog = new Dialog<>();
        dialog.setTitle("New Address");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField streetField = new TextField();
        streetField.setPromptText("Street");
        TextField houseNumberField = new TextField();
        houseNumberField.setPromptText("House Number");
        TextField cityField = new TextField();
        cityField.setPromptText("City");
        TextField postalCodeField = new TextField();
        postalCodeField.setPromptText("Postal Code");

        grid.add(new Label("Street:"), 0, 0);
        grid.add(streetField, 1, 0);
        grid.add(new Label("House Number:"), 0, 2);
        grid.add(houseNumberField, 1, 2);
        grid.add(new Label("City:"), 0, 3);
        grid.add(cityField, 1, 3);
        grid.add(new Label("Postal Code:"), 0, 4);
        grid.add(postalCodeField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    return new Address(
                            streetField.getText(),
                            Integer.parseInt(houseNumberField.getText()),
                            cityField.getText(),
                            Integer.parseInt(postalCodeField.getText()),
                            AuthenticationUtil.currentUser.getUsername());
                } catch (NumberFormatException ex) {
                    Platform.runLater(()->{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        logger.error("Wrong address format", ex);
                        alert.setTitle("Error");
                        alert.setHeaderText("Format issue");
                        alert.setContentText("Please try again and enter a number");
                        alert.showAndWait();
                    });
                    return null;
                }
            }
            return null;
        });

        Optional<Address> result = dialog.showAndWait();

        result.ifPresent(address -> {
            try{
                SaveAddressThread saveAddressThread = saveAddressThreadFactory.create(address);

                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(saveAddressThread);

                logger.info("New address made by " + AuthenticationUtil.currentUser.getUsername());
            }
            catch(Exception ex) {
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    logger.error("Unable to save Address to database", ex);
                    alert.setTitle("Error");
                    alert.setHeaderText("Database Connection issue");
                    alert.setContentText("Please try again");
                    alert.showAndWait();
                });
            }
        });

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            refreshTable();
        });
        pause.play();

    }

    @FXML
    public void deleteAddress(){
        Optional<Address> clickedAddress=getClickedAddress();

        DeleteAddressThread deleteAddressThread=deleteAddressThreadFactory.create(clickedAddress.get());

        Executor executor = Executors.newSingleThreadExecutor();
        logger.info("Address successfully deleted");
        executor.execute(deleteAddressThread);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            refreshTable();
        });
        pause.play();

    }

    @FXML
    public void backToLogin(){
        FXMLLoader fxmlLoader = new FXMLLoader(HomeSecurityApplication.class.getResource("/views/Login-Screen.fxml"));
        Platform.runLater(()->{
            try {
                Scene scene = new Scene(fxmlLoader.load(), 900, 600);
                HomeSecurityApplication.getMainStage().setTitle("Login");
                HomeSecurityApplication.getMainStage().setScene(scene);
                HomeSecurityApplication.getMainStage().show();
            }catch(IOException e)
            {
                throw new RuntimeException();
            }
        });

    }
    private Optional<Address> getClickedAddress(){

        TableView.TableViewSelectionModel<Address> selectionModel = addressTableView.getSelectionModel();

        if(Optional.ofNullable(selectionModel.getSelectedItem()).isEmpty()){
            Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to retrieve the device");
                alert.setContentText("Please click the device and try again.");
                alert.showAndWait();
                logger.error("The device on the main screen has not been clicked");

            });
            return Optional.empty();
        }
        else{
            return Optional.of(selectionModel.getSelectedItem());
        }
    }

    private void showAllDevicesScreen() {
        Platform.runLater(()->{
            SpringFXMLLoader loader = new SpringFXMLLoader(HomeSecurityApplication.getApplicationContext());

            FXMLLoader fxmlLoader = loader.createLoader("/views/all-devices.fxml");

            fxmlLoader.setControllerFactory(HomeSecurityApplication.getApplicationContext()::getBean);
            try {
                Scene scene = new Scene(fxmlLoader.load(), 900, 600);
                HomeSecurityApplication.getMainStage().setTitle("Device Control");
                HomeSecurityApplication.getMainStage().setScene(scene);
                HomeSecurityApplication.getMainStage().show();
            }catch(IOException ex)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to retrieve the devices from database");
                alert.setContentText("Please restart the database and the application");
                alert.showAndWait();
                logger.error("Connection to database lost.",ex);
            }
        });

    }
    @FXML
    public void refreshTable(){
        List<Address> addressList = addressService.findAddressesByUsername();

        ObservableList<Address> observableAddressList= FXCollections.observableArrayList(addressList);
        addressTableView.setItems(observableAddressList);

        logger.info("Table refreshed");
    }
}
