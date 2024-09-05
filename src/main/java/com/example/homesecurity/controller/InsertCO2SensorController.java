package com.example.homesecurity.controller;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.entity.Camera;
import com.example.homesecurity.entity.Change;
import com.example.homesecurity.enums.DeviceType;
import com.example.homesecurity.enums.Locations;
import com.example.homesecurity.exception.DuplicateSerialNumberException;
import com.example.homesecurity.exception.EmptyFieldException;
import com.example.homesecurity.factory.SaveCO2SensorThreadFactory;
import com.example.homesecurity.service.CO2SensorService;
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
import com.example.homesecurity.thread.SaveCO2SensorThread;
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
public class InsertCO2SensorController {

    @Autowired
    private SaveCO2SensorThreadFactory saveCO2SensorThreadFactory;

    @Autowired
    private CO2SensorService co2SensorService;
    private static final Logger logger= LoggerFactory.getLogger(InsertCO2SensorController.class);

    @FXML
    private TextField co2SensorNameTextField;

    @FXML
    private TextField co2SensorModelTextField;

    @FXML
    private TextField co2SensorManufacturerTextField;

    @FXML
    private TextField co2SensorSerialNumberTextField;

    @FXML
    private DatePicker co2SensorManufacturingDateDatePicker;

    @FXML
    private ComboBox<String> co2SensorLocationComboBox;


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

        co2SensorLocationComboBox.setItems(observableTypeList);
        co2SensorLocationComboBox.setValue(observableTypeList.getFirst());

        co2SensorManufacturingDateDatePicker.setEditable(false);

        logger.info("InsertCO2SensorController has been initialized");
    }

    public void saveNewCO2Sensor(){

        List<CO2Sensor> allCO2Sensors=co2SensorService.findAllCO2Sensors();

        String co2SensorName=co2SensorNameTextField.getText();
        String co2SensorModel=co2SensorModelTextField.getText();
        String co2SensorManufacturer=co2SensorManufacturerTextField.getText();
        String co2SensorSerialNumber=co2SensorSerialNumberTextField.getText();
        LocalDate co2SensorManufacturingDate=co2SensorManufacturingDateDatePicker.getValue();

        try{
            saveNewCO2SensorHelper(co2SensorName, co2SensorModel, co2SensorManufacturer, co2SensorSerialNumber, co2SensorManufacturingDate, allCO2Sensors);

        }catch(EmptyFieldException ex){
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
                logger.error("Equal serial numbers found while saving co2 sensor");
                alert.showAndWait();
            });
        }
    }

    private void saveNewCO2SensorHelper(String co2SensorName, String co2SensorModel, String co2SensorManufacturer, String co2SensorSerialNumber, LocalDate co2SensorManufacturingDate, List<CO2Sensor> allCO2Sensors) throws EmptyFieldException{

        if(!co2SensorSerialNumber.isEmpty()){
            for(CO2Sensor sensor: allCO2Sensors){
                if(sensor.getSerialNumber().equals(co2SensorSerialNumber)){
                    throw new DuplicateSerialNumberException("CO2 sensor duplicate serial numbers found");
                }
            }
        }

        if(!co2SensorName.isEmpty()&&!co2SensorModel.isEmpty()&& !co2SensorManufacturer.isEmpty()&& !co2SensorSerialNumber.isEmpty()&& co2SensorManufacturingDate !=null){
            Locations deviceLocation=Locations.BACKYARD;

            switch(co2SensorLocationComboBox.getValue()) {
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

            CO2Sensor sensor = createCO2Sensor(co2SensorName, co2SensorModel, co2SensorManufacturer, co2SensorSerialNumber, co2SensorManufacturingDate, deviceLocation);

            Change change=new Change(sensor.getDeviceName(),sensor.getDeviceType().getDeviceType(),"Sensor: " + sensor.getDeviceName()+ " has been successfully saved", LocalDateTime.now());

            SaveCO2SensorThread saveCO2SensorThread = saveCO2SensorThreadFactory.create(sensor);

            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(saveCO2SensorThread);

            logger.info("CO2 sensor has been successfully saved");
            FileUtil.serializeChange(change);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information");
            alert.setHeaderText("Successful operation");
            alert.setContentText("Sensor: " + sensor.getDeviceName()+ " has been successfully saved");
            alert.showAndWait();

        }
        else{
            throw new EmptyFieldException();
        }

    }

    private static CO2Sensor createCO2Sensor(String co2SensorName, String co2SensorModel, String co2SensorManufacturer, String co2SensorSerialNumber, LocalDate co2SensorManufacturingDate, Locations deviceLocation) {
        CO2Sensor sensor = (CO2Sensor) CO2Sensor.builder()
                .deviceManufacturer(co2SensorManufacturer)
                .deviceType(DeviceType.CO2_SENSOR)
                .deviceModel(co2SensorModel)
                .deviceStatus(false)
                .deviceSerialNumber(co2SensorSerialNumber)
                .location(deviceLocation)
                .deviceManufacturingDate(co2SensorManufacturingDate)
                .deviceName(co2SensorName)
                .build();

        sensor.setCurrentCO2(sensor.measure());

        sensor.setAddress(ChooseLocationController.currentLocation);
        return sensor;
    }
}
