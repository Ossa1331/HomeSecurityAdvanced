package com.example.homesecurity.controller;

import com.example.homesecurity.entity.CO2Sensor;
import com.example.homesecurity.entity.Change;
import com.example.homesecurity.entity.HeatSensor;
import com.example.homesecurity.enums.DeviceType;
import com.example.homesecurity.enums.Locations;
import com.example.homesecurity.exception.DuplicateSerialNumberException;
import com.example.homesecurity.exception.EmptyFieldException;
import com.example.homesecurity.exception.LowerHigherThanUpperLimitException;
import com.example.homesecurity.factory.SaveHeatSensorThreadFactory;
import com.example.homesecurity.service.HeatSensorService;
import com.example.homesecurity.util.FileUtil;
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
import com.example.homesecurity.thread.SaveHeatSensorThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Controller
public class InsertHeatSensorController {

    @Autowired
    private SaveHeatSensorThreadFactory saveHeatSensorFactory;

    @Autowired
    private HeatSensorService heatSensorService;
    private static final Logger logger= LoggerFactory.getLogger(InsertHeatSensorController.class);

    @FXML
    private TextField heatSensorNameTextField;
    @FXML
    private TextField heatSensorLowerLimitTextField;
    @FXML
    private TextField heatSensorUpperLimitTextField;
    @FXML
    private TextField heatSensorModelTextField;
    @FXML
    private TextField heatSensorManufacturerTextField;
    @FXML
    private TextField heatSensorSerialNumberTextField;
    @FXML
    private DatePicker heatSensorManufacturingDateDatePicker;
    @FXML
    private ComboBox<String> heatSensorLocationComboBox;

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

        heatSensorLocationComboBox.setItems(observableTypeList);
        heatSensorLocationComboBox.setValue(observableTypeList.getFirst());

        heatSensorManufacturingDateDatePicker.setEditable(false);

        logger.info("InsertHeatSensorController has been initialized");
    }

    public void saveNewHeatSensor(){

        List<HeatSensor> allHeatSensors=heatSensorService.findAllHeatSensors();

        String heatSensorName=heatSensorNameTextField.getText();
        String heatSensorModel=heatSensorModelTextField.getText();
        String lowerTemperatureLimitString=heatSensorLowerLimitTextField.getText();
        String upperTemperatureLimitString=heatSensorUpperLimitTextField.getText();
        String heatSensorManufacturer=heatSensorManufacturerTextField.getText();
        String heatSensorSerialNumber=heatSensorSerialNumberTextField.getText();
        LocalDate heatSensorManufacturingDate=heatSensorManufacturingDateDatePicker.getValue();

        try{

            saveNewHeatSensorHelper(heatSensorName, heatSensorModel, lowerTemperatureLimitString, upperTemperatureLimitString, heatSensorManufacturer, heatSensorSerialNumber, heatSensorManufacturingDate, allHeatSensors);

            logger.info("Heat Sensor: " + heatSensorName + " has been successfully saved to database");

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
        catch(NumberFormatException ex){

            Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error saving object to database.");
                alert.setContentText("Invalid Number format");
                logger.error("Please fill in the valid number format.");
                alert.showAndWait();
            });

        }

        catch(DuplicateSerialNumberException ex) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error saving object to database.");
                alert.setContentText("Equal serial numbers found. please try again.");
                logger.error("Equal serial numbers found while saving heat sensor");
                alert.showAndWait();
            });
        }
    }

    private void saveNewHeatSensorHelper(String heatSensorName, String heatSensorModel, String lowerTemperatureLimitString, String upperTemperatureLimitString,
                                         String heatSensorManufacturer, String heatSensorSerialNumber, LocalDate heatSensorManufacturingDate, List<HeatSensor> allHeatSensors) throws EmptyFieldException {

        if(!heatSensorSerialNumber.isEmpty()){
            for(HeatSensor sensor: allHeatSensors){
                if(sensor.getSerialNumber().equals(heatSensorSerialNumber)){
                    throw new DuplicateSerialNumberException("Heat sensor duplicate serial numbers found");
                }
            }
        }

        if(!heatSensorName.isEmpty()&&!heatSensorModel.isEmpty()&&!lowerTemperatureLimitString.isEmpty()
                &&!upperTemperatureLimitString.isEmpty()&&!heatSensorManufacturer.isEmpty()&&!heatSensorSerialNumber.isEmpty() && heatSensorManufacturingDate !=null){

            Double lowerTemperatureLimit=Double.parseDouble(lowerTemperatureLimitString);
            Double upperTemperatureLimit=Double.parseDouble(upperTemperatureLimitString);

            Locations deviceLocation=Locations.BACKYARD;

            switch(heatSensorLocationComboBox.getValue()) {
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

            HeatSensor sensor = createHeatSensor(heatSensorName, heatSensorModel, heatSensorManufacturer, heatSensorSerialNumber, heatSensorManufacturingDate, lowerTemperatureLimit, upperTemperatureLimit, deviceLocation);

                Platform.runLater(()->{
                    try{
                        sensor.measure();

                        sensor.setTemperatureInF(sensor.fahrenheitToCelsius(sensor.getTemperatureInC()));
                        Change change=new Change(sensor.getDeviceName(),sensor.getDeviceType().getDeviceType(),"Sensor: " + sensor.getDeviceName()+ " has been successfully saved", LocalDateTime.now());

                        SaveHeatSensorThread saveHeatSensorThread = saveHeatSensorFactory.create(sensor);

                        Executor executor = Executors.newSingleThreadExecutor();
                        executor.execute(saveHeatSensorThread);

                        FileUtil.serializeChange(change);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information");
                        alert.setHeaderText("Successful operation");
                        alert.setContentText("Sensor: " + sensor.getDeviceName()+ " has been successfully saved");
                        logger.info("Heat Sensor has been successfully saved");
                        alert.showAndWait();
                    }
                    catch(LowerHigherThanUpperLimitException ex){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Failed operation");
                        alert.setContentText("Lower limit is higher than the upper limit. Please try again.");
                        logger.error("Lower value of the sensor was higher than High value", ex);
                        alert.showAndWait();
                    }
                });
            }
        else{
            throw new EmptyFieldException();
        }
    }

    private static HeatSensor createHeatSensor(String heatSensorName, String heatSensorModel, String heatSensorManufacturer, String heatSensorSerialNumber, LocalDate heatSensorManufacturingDate, Double lowerTemperatureLimit, Double upperTemperatureLimit, Locations deviceLocation) {
        HeatSensor sensor = (HeatSensor) HeatSensor.builder()
                .deviceManufacturer(heatSensorManufacturer)
                .sensorLowerLimit(lowerTemperatureLimit)
                .sensorUpperLimit(upperTemperatureLimit)
                .temperatureInC((double) 0)
                .deviceType(DeviceType.HEAT_SENSOR)
                .deviceModel(heatSensorModel)
                .deviceStatus(false)
                .deviceSerialNumber(heatSensorSerialNumber)
                .location(deviceLocation)
                .deviceManufacturingDate(heatSensorManufacturingDate)
                .deviceName(heatSensorName)
                .build();

        sensor.setAddress(ChooseLocationController.currentLocation);
        return sensor;
    }
}
