package com.example.homesecurity.controller;

import com.example.homesecurity.entity.Change;
import com.example.homesecurity.entity.HeatSensor;
import com.example.homesecurity.entity.MotionSensor;
import com.example.homesecurity.enums.DeviceType;
import com.example.homesecurity.enums.Locations;
import com.example.homesecurity.exception.DuplicateSerialNumberException;
import com.example.homesecurity.exception.EmptyFieldException;
import com.example.homesecurity.factory.SaveMotionSensorThreadFactory;
import com.example.homesecurity.service.MotionSensorService;
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
import com.example.homesecurity.thread.SaveMotionSensorThread;
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
public class InsertMotionSensorController {

    @Autowired
    private SaveMotionSensorThreadFactory saveMotionSensorThreadFactory;

    @Autowired
    private MotionSensorService motionSensorService;

    private static final Logger logger= LoggerFactory.getLogger(InsertMotionSensorController.class);

    @FXML
    private TextField motionSensorNameTextField;
    @FXML
    private TextField motionSensorModelTextField;
    @FXML
    private TextField motionSensorManufacturerTextField;
    @FXML
    private TextField motionSensorSerialNumberTextField;
    @FXML
    private DatePicker motionSensorManufacturingDateDatePicker;
    @FXML
    private ComboBox<String> motionSensorLocationComboBox;

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

        motionSensorLocationComboBox.setItems(observableTypeList);
        motionSensorLocationComboBox.setValue(observableTypeList.getFirst());

        motionSensorManufacturingDateDatePicker.setEditable(false);

        logger.info("InsertMotionSensorController has been initialized");
    }

    public void saveNewMotionSensor(){

        List<MotionSensor> allMotionSensors=motionSensorService.findAllMotionSensors();

        String motionSensorName=motionSensorNameTextField.getText();
        String motionSensorModel=motionSensorModelTextField.getText();
        String motionSensorManufacturer=motionSensorManufacturerTextField.getText();
        String motionSensorSerialNumber=motionSensorSerialNumberTextField.getText();
        LocalDate motionSensorManufacturingDate=motionSensorManufacturingDateDatePicker.getValue();


        try{

            saveNewMotionSensorHelper(motionSensorName, motionSensorModel, motionSensorManufacturer, motionSensorSerialNumber, motionSensorManufacturingDate, allMotionSensors);

            logger.info("Motion Sensor: " + motionSensorName + " has been successfully saved to database");

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
                logger.error("Equal serial numbers found while saving motion sensor");
                alert.showAndWait();
            });
        }

    }

    private void saveNewMotionSensorHelper(String motionSensorName, String motionSensorModel, String motionSensorManufacturer, String motionSensorSerialNumber, LocalDate motionSensorManufacturingDate, List<MotionSensor> allMotionSensors) {

        if(!motionSensorSerialNumber.isEmpty()){
            for(MotionSensor sensor: allMotionSensors){
                if(sensor.getSerialNumber().equals(motionSensorSerialNumber)){
                    throw new DuplicateSerialNumberException("Motion sensor duplicate serial numbers found");
                }
            }
        }

        if(!motionSensorName.isEmpty()&& !motionSensorModel.isEmpty()
                &&!motionSensorManufacturer.isEmpty()&&!motionSensorSerialNumber.isEmpty()&& motionSensorManufacturingDate !=null){

            Locations deviceLocation=Locations.BACKYARD;

            switch(motionSensorLocationComboBox.getValue()) {
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

            MotionSensor sensor = createSensor(motionSensorName, motionSensorModel, motionSensorManufacturer, motionSensorSerialNumber, motionSensorManufacturingDate, deviceLocation);

            Change change=new Change(sensor.getDeviceName(),sensor.getDeviceType().getDeviceType(),"Sensor: " + sensor.getDeviceName()+ " has been successfully saved", LocalDateTime.now());

            SaveMotionSensorThread saveMotionSensorThread = saveMotionSensorThreadFactory.create(sensor);
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(saveMotionSensorThread);

            FileUtil.serializeChange(change);
            Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information");
                alert.setHeaderText("Successful operation");
                alert.setContentText("Sensor: " + sensor.getDeviceName()+ " has been successfully saved");
                logger.info("Motion Sensor has been saved successfully");
                alert.showAndWait();
            });


        }
        else{
            throw new EmptyFieldException();
        }
    }

    private static MotionSensor createSensor(String motionSensorName, String motionSensorModel, String motionSensorManufacturer, String motionSensorSerialNumber, LocalDate motionSensorManufacturingDate, Locations deviceLocation) {
        MotionSensor sensor = (MotionSensor) MotionSensor.builder()
                .deviceManufacturer(motionSensorManufacturer)
                .deviceType(DeviceType.MOTION_SENSOR)
                .deviceModel(motionSensorModel)
                .deviceStatus(false)
                .deviceSerialNumber(motionSensorSerialNumber)
                .location(deviceLocation)
                .anomalyDetected(false)
                .deviceManufacturingDate(motionSensorManufacturingDate)
                .deviceName(motionSensorName)
                .build();

        sensor.setAddress(ChooseLocationController.currentLocation);
        return sensor;
    }

}
