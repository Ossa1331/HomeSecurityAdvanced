package com.example.homesecurity.controller;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.entity.Change;
import com.example.homesecurity.entity.GlassBreakSensor;
import com.example.homesecurity.enums.DeviceType;
import com.example.homesecurity.enums.Locations;
import com.example.homesecurity.exception.DuplicateSerialNumberException;
import com.example.homesecurity.exception.EmptyFieldException;
import com.example.homesecurity.factory.SaveGlassBreakSensorThreadFactory;
import com.example.homesecurity.service.GlassBreakSensorService;
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
import com.example.homesecurity.thread.SaveGlassBreakSensorThread;
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
public class InsertGlassBreakSensorController {

    @Autowired
    private SaveGlassBreakSensorThreadFactory saveGlassBreakSensorThreadFactory;

    @Autowired
    private GlassBreakSensorService glassBreakSensorService;
    private static final Logger logger= LoggerFactory.getLogger(InsertGlassBreakSensorController.class);

    @FXML
    private TextField glassBreakSensorNameTextField;
    @FXML
    private TextField glassBreakSensorModelTextField;
    @FXML
    private TextField glassBreakSensorManufacturerTextField;
    @FXML
    private TextField glassBreakSensorSerialNumberTextField;
    @FXML
    private DatePicker glassBreakSensorManufacturingDateDatePicker;
    @FXML
    private ComboBox<String> glassBreakSensorLocationComboBox;


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

        glassBreakSensorLocationComboBox.setItems(observableTypeList);
        glassBreakSensorLocationComboBox.setValue(observableTypeList.getFirst());

        glassBreakSensorManufacturingDateDatePicker.setEditable(false);
        logger.info("InsertGlassBreakSensorController has been initialized");
    }

    public void saveNewGlassBreakSensor(){

        List<GlassBreakSensor> allGlassBreakSensors=glassBreakSensorService.findAllGlassBreakSensors();

        String glassBreakSensorName=glassBreakSensorNameTextField.getText();
        String glassBreakSensorModel=glassBreakSensorModelTextField.getText();
        String glassBreakSensorManufacturer=glassBreakSensorManufacturerTextField.getText();
        String glassBreakSensorSerialNumber=glassBreakSensorSerialNumberTextField.getText();
        LocalDate glassBreakSensorManufacturingDate=glassBreakSensorManufacturingDateDatePicker.getValue();
        
        try {
            saveNewGlassBreakSensorHelper(glassBreakSensorName, glassBreakSensorManufacturer, glassBreakSensorModel, glassBreakSensorSerialNumber, glassBreakSensorManufacturingDate, allGlassBreakSensors);

            logger.info("Glass Break Sensor: " + glassBreakSensorName + " has successfully been saved to database");

        }
        catch(EmptyFieldException ex){
            Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                String message="Some fields were left empty";
                alert.setTitle("Error");
                alert.setHeaderText("Error saving object to database.");
                alert.setContentText("Please fill all the fields");
                logger.error(message,ex);
                alert.showAndWait();
            });
        }

        catch(DuplicateSerialNumberException ex){

            Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error saving object to database.");
                alert.setContentText("Equal serial numbers found. please try again.");
                logger.error("Equal serial numbers found while saving glass break sensor");
                alert.showAndWait();
            });
        }
    }

    private void saveNewGlassBreakSensorHelper(String glassBreakSensorName, String glassBreakSensorManufacturer,
                                               String glassBreakSensorModel, String glassBreakSensorSerialNumber, LocalDate glassBreakSensorManufacturingDate, List<GlassBreakSensor> allGlassBreakSensors) throws EmptyFieldException {

        if(!glassBreakSensorSerialNumber.isEmpty()){
            for(GlassBreakSensor sensor: allGlassBreakSensors){
                if(sensor.getSerialNumber().equals(glassBreakSensorSerialNumber)){
                    throw new DuplicateSerialNumberException("Glass break sensor duplicate serial numbers found");
                }
            }
        }

        if(!glassBreakSensorName.isEmpty() && !glassBreakSensorManufacturer.isEmpty()
        &&!glassBreakSensorModel.isEmpty() && !glassBreakSensorSerialNumber.isEmpty() && glassBreakSensorManufacturingDate !=null){

            Locations deviceLocation=Locations.BACKYARD;

            switch(glassBreakSensorLocationComboBox.getValue()) {
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

            GlassBreakSensor sensor = createGlassBreakSensor(glassBreakSensorName, glassBreakSensorManufacturer, glassBreakSensorModel, glassBreakSensorSerialNumber, glassBreakSensorManufacturingDate, deviceLocation);

            Change change=new Change(sensor.getDeviceName(),sensor.getDeviceType().getDeviceType(),"Sensor: " + sensor.getDeviceName()+ " has been successfully saved", LocalDateTime.now());

            SaveGlassBreakSensorThread saveGlassBreakSensorThread = saveGlassBreakSensorThreadFactory.create(sensor);

            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(saveGlassBreakSensorThread);

            logger.info("Glass Break Sensor has been successfully saved");

            FileUtil.serializeChange(change);

            Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information");
                alert.setHeaderText("Successful operation");
                alert.setContentText("Sensor: " + sensor.getDeviceName()+ " has been successfully saved");
                alert.showAndWait();
            });
        }
        else{
            throw new EmptyFieldException();
        }
    }

    private static GlassBreakSensor createGlassBreakSensor(String glassBreakSensorName, String glassBreakSensorManufacturer, String glassBreakSensorModel, String glassBreakSensorSerialNumber, LocalDate glassBreakSensorManufacturingDate, Locations deviceLocation) {
        GlassBreakSensor sensor = (GlassBreakSensor) GlassBreakSensor.builder()
                .deviceManufacturer(glassBreakSensorManufacturer)
                .deviceType(DeviceType.GLASS_BREAK_SENSOR)
                .deviceModel(glassBreakSensorModel)
                .deviceStatus(false)
                .deviceSerialNumber(glassBreakSensorSerialNumber)
                .glassBroken(false)
                .location(deviceLocation)
                .deviceManufacturingDate(glassBreakSensorManufacturingDate)
                .deviceName(glassBreakSensorName)
                .build();

        sensor.setAddress(ChooseLocationController.currentLocation);
        return sensor;
    }

}
    
