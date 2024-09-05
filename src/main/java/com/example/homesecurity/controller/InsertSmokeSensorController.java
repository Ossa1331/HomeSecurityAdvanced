package com.example.homesecurity.controller;

import com.example.homesecurity.entity.Change;
import com.example.homesecurity.entity.MotionSensor;
import com.example.homesecurity.entity.SmokeSensor;
import com.example.homesecurity.enums.DeviceType;
import com.example.homesecurity.enums.Locations;
import com.example.homesecurity.exception.DuplicateSerialNumberException;
import com.example.homesecurity.exception.EmptyFieldException;
import com.example.homesecurity.factory.SaveSmokeSensorThreadFactory;
import com.example.homesecurity.service.SmokeSensorService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.homesecurity.thread.SaveSmokeSensorThread;
import com.example.homesecurity.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Controller
public class InsertSmokeSensorController {

    @Autowired
    private SaveSmokeSensorThreadFactory saveSmokeSensorThreadFactory;

    @Autowired
    private SmokeSensorService smokeSensorService;

    private static final Logger logger= LoggerFactory.getLogger(InsertSmokeSensorController.class);

    @FXML
    private TextField smokeSensorNameTextField;
    @FXML
    private TextField smokeSensorModelTextField;
    @FXML
    private TextField smokeSensorManufacturerTextField;
    @FXML
    private TextField smokeSensorSerialNumberTextField;
    @FXML
    private DatePicker smokeSensorManufacturingDateDatePicker;
    @FXML
    private ComboBox<String> smokeSensorLocationComboBox;


    public void initialize(){
        List<String> locationList=new ArrayList<>();
        locationList.add("Dining room");
        locationList.add("Bathroom");
        locationList.add("Kitchen");
        locationList.add("Backyard");
        locationList.add("Terrace");
        locationList.add("Courtyard");
        locationList.add("Basement");
        locationList.add("Stairway");
        locationList.add("Utility room");
        locationList.add("Hallway");

        ObservableList<String> observableTypeList= FXCollections.observableList(locationList);

        smokeSensorLocationComboBox.setItems(observableTypeList);
        smokeSensorLocationComboBox.setValue(observableTypeList.getFirst());

        smokeSensorManufacturingDateDatePicker.setEditable(false);

        logger.info("SmokeSensorController has been initialized");
    }

    public void saveNewSmokeSensor(){

        List<SmokeSensor> allSmokeSensors=smokeSensorService.findAllSmokeSensors();

        String smokeSensorName=smokeSensorNameTextField.getText();
        String smokeSensorModel=smokeSensorModelTextField.getText();
        String smokeSensorManufacturer=smokeSensorManufacturerTextField.getText();
        String smokeSensorSerialNumber=smokeSensorSerialNumberTextField.getText();
        LocalDate smokeSensorManufacturingDate=smokeSensorManufacturingDateDatePicker.getValue();

        try{
            saveNewSmokeSensorHelper(smokeSensorName, smokeSensorModel, smokeSensorManufacturer, smokeSensorSerialNumber, smokeSensorManufacturingDate, allSmokeSensors);


        } catch(EmptyFieldException ex){

            Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error saving object to database.");
                alert.setContentText("Please fill all the fields");
                logger.error("Some fields were left empty while filling out the form");
                alert.showAndWait();
            });
        }
        catch(DuplicateSerialNumberException ex){

            Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error saving object to database.");
                alert.setContentText("Equal serial numbers found. please try again.");
                logger.error("Equal serial numbers found while saving motion sensor");
                alert.showAndWait();
            });
        }
    }

    private void saveNewSmokeSensorHelper(String smokeSensorName, String smokeSensorModel, String smokeSensorManufacturer, String smokeSensorSerialNumber, LocalDate smokeSensorManufacturingDate, List<SmokeSensor> allSmokeSensors) {

        if(!smokeSensorSerialNumber.isEmpty()){
            for(SmokeSensor sensor: allSmokeSensors){
                if(sensor.getSerialNumber().equals(smokeSensorSerialNumber)){
                    throw new DuplicateSerialNumberException("Smoke sensor duplicate serial numbers found");
                }
            }
        }



        if(!smokeSensorName.isEmpty()&&!smokeSensorModel.isEmpty()&&!smokeSensorManufacturer.isEmpty()&&!smokeSensorSerialNumber.isEmpty()&& smokeSensorManufacturingDate !=null){
            Locations deviceLocation=Locations.BACKYARD;

            switch(smokeSensorLocationComboBox.getValue()) {
                case ("Bathroom"): {
                    deviceLocation = Locations.BATHROOM;
                    break;
                }
                case ("Bedroom"): {
                    deviceLocation = Locations.BEDROOM;
                    break;
                }
                case ("Dining room"): {
                    deviceLocation = Locations.DINING_ROOM;
                    break;
                }
                case ("Courtyard"): {
                    deviceLocation = Locations.COURTYARD;
                    break;
                }
                case ("Terrace"): {
                    deviceLocation = Locations.TERRACE;
                    break;
                }
                case ("Hallway"): {
                    deviceLocation = Locations.HALLWAY;
                    break;
                }
                case ("Kitchen"): {
                    deviceLocation = Locations.KITCHEN;
                    break;
                }
                case ("Basement"): {
                    deviceLocation = Locations.BASEMENT;
                    break;
                }
                case ("Stairway"): {
                    deviceLocation = Locations.STAIRWAY;
                    break;
                }
                case ("Utility room"): {
                    deviceLocation = Locations.UTILITY_ROOM;
                    break;
                }
            }

            SmokeSensor sensor = createSmokeSensor(smokeSensorManufacturer, smokeSensorModel, smokeSensorSerialNumber, deviceLocation, smokeSensorManufacturingDate, smokeSensorName);

            Change change=new Change(sensor.getDeviceName(),sensor.getDeviceType().getDeviceType(),"Sensor: " + sensor.getDeviceName()+ " has been successfully saved", LocalDateTime.now());

            SaveSmokeSensorThread saveSmokeSensorThread = saveSmokeSensorThreadFactory.create(sensor);

            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(saveSmokeSensorThread);

            FileUtil.serializeChange(change);

            Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Successful operation");
                alert.setContentText("Sensor: " + sensor.getDeviceName()+ " has been successfully saved");
                logger.info("Smoke sensor has been successfully connected");
                alert.showAndWait();
            });
        }
        else{
            throw new EmptyFieldException();
        }
    }

    private static SmokeSensor createSmokeSensor(String smokeSensorManufacturer, String smokeSensorModel, String smokeSensorSerialNumber, Locations deviceLocation, LocalDate smokeSensorManufacturingDate, String smokeSensorName) {
        SmokeSensor sensor = (SmokeSensor) SmokeSensor.builder()
                .deviceManufacturer(smokeSensorManufacturer)
                .deviceType(DeviceType.SMOKE_SENSOR)
                .deviceModel(smokeSensorModel)
                .deviceStatus(false)
                .deviceSerialNumber(smokeSensorSerialNumber)
                .location(deviceLocation)
                .deviceManufacturingDate(smokeSensorManufacturingDate)
                .deviceName(smokeSensorName)
                .build();

        sensor.measure();
        sensor.setAddress(ChooseLocationController.currentLocation);
        return sensor;
    }
}
