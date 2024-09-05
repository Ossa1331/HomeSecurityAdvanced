package com.example.homesecurity.controller;

import com.example.homesecurity.entity.*;
import com.example.homesecurity.exception.EmptyFieldException;
import com.example.homesecurity.exception.NoPrivilegeException;
import com.example.homesecurity.factory.*;
import com.example.homesecurity.service.*;
import com.example.homesecurity.thread.*;
import com.example.homesecurity.util.AuthenticationUtil;
import com.example.homesecurity.util.FileUtil;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Controller
public class AllDevicesController {

    @Autowired
    private CameraService cameraService;

    @Autowired
    private CO2SensorService co2SensorService;

    @Autowired
    private GlassBreakSensorService glassBreakSensorService;

    @Autowired
    private HeatSensorService heatSensorService;

    @Autowired
    private MotionSensorService motionSensorService;

    @Autowired
    private SmokeSensorService smokeSensorService;

    @Autowired
    private DeleteCameraThreadFactory deleteCameraThreadFactory;

    @Autowired
    private DeleteCO2SensorThreadFactory deleteCO2SensorThreadFactory;

    @Autowired
    private DeleteGlassBreakSensorThreadFactory deleteGlassBreakSensorThreadFactory;

    @Autowired
    private DeleteHeatSensorThreadFactory deleteHeatSensorThreadFactory;

    @Autowired
    private DeleteMotionSensorThreadFactory deleteMotionSensorThreadFactory;

    @Autowired
    private DeleteSmokeSensorThreadFactory deleteSmokeSensorThreadFactory;

    @Autowired
    private UpdateCameraThreadFactory updateCameraThreadFactory;

    @Autowired
    private UpdateCO2SensorThreadFactory updateCO2SensorThreadFactory;

    @Autowired
    private UpdateGlassBreakSensorThreadFactory updateGlassBreakSensorThreadFactory;

    @Autowired
    private UpdateHeatSensorThreadFactory updateHeatSensorThreadFactory;

    @Autowired
    private UpdateMotionSensorThreadFactory updateMotionSensorThreadFactory;

    @Autowired
    private UpdateSmokeSensorThreadFactory updateSmokeSensorThreadFactory;

    private static final Logger logger= LoggerFactory.getLogger(AllDevicesController.class);

    @FXML
    private TextField deviceNameTextField;

    @FXML
    private ComboBox<String> deviceTypeComboBox;

    @FXML
    private TableView<Device> deviceTableView;

    @FXML
    private TableColumn<Device, String> deviceNameTableColumn;

    @FXML
    private TableColumn<Device, String> deviceStatusTableColumn;

    @FXML
    private TableColumn<Device, String> deviceModelTableColumn;

    @FXML
    private TableColumn<Device, String> deviceLocationTableColumn;

    @FXML
    private TableColumn<Device, String> deviceManufacturerTableColumn;

    @FXML
    private TableColumn<Device, String> deviceDateAddedtableColumn;

    @FXML
    private TableColumn<Device, String> deviceTypeTableColumn;

    @FXML
    private TableColumn<Device, String> deviceManufacturingDateTableColumn;

    public void initialize(){
        List<Device> deviceList = findAllDevices();

        ObservableList<Device> observableDeviceList= FXCollections.observableArrayList(deviceList);

        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter formatter2= DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        List<String> typeList=new ArrayList<>();
        typeList.add("Camera");
        typeList.add("CO2 Sensor");
        typeList.add("Glass Break Sensor");
        typeList.add("Heat Sensor");
        typeList.add("Motion Sensor");
        typeList.add("Smoke Sensor");

        ObservableList<String> observableTypeList=FXCollections.observableList(typeList);

        deviceTypeComboBox.setItems(observableTypeList);
        deviceTypeComboBox.setValue(observableTypeList.getFirst());

        deviceNameTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getDeviceName()));

        deviceStatusTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().deviceStatusToString()));

        deviceModelTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getModel()));

        deviceLocationTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getLocation().getLocation()));

        deviceManufacturerTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getDeviceManufacturer()));

        deviceManufacturingDateTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getDeviceManufacturingDate().format(formatter)));

        deviceDateAddedtableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getDeviceAdded().format(formatter2)));

        deviceTypeTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getDeviceType().getDeviceType()));


        deviceTableView.setItems(observableDeviceList);

        logger.info("AllDevicesController Initialized");

    }

    private List<Device> findAllDevices() {
        List<Device> deviceList=new ArrayList<>();

        List<Camera> cameraList=cameraService.findAllCameras();
        deviceList.addAll(cameraList);

        List<CO2Sensor> co2SensorList=co2SensorService.findAllCO2Sensors();
        deviceList.addAll(co2SensorList);

        List<GlassBreakSensor> glassBreakSensorList=glassBreakSensorService.findAllGlassBreakSensors();
        deviceList.addAll(glassBreakSensorList);

        List<HeatSensor> heatSensorList = heatSensorService.findAllHeatSensors();
        deviceList.addAll(heatSensorList);

        List<MotionSensor> motionSensorList=motionSensorService.findAllMotionSensors();
        deviceList.addAll(motionSensorList);

        List<SmokeSensor> smokeSensorList =smokeSensorService.findAllSmokeSensors();
        deviceList.addAll(smokeSensorList);

        logger.info("Found all devices");
        return deviceList;
    }

    public void clearFilters(){
        List<Device> deviceList = findAllDevices();

        ObservableList<Device> observableDeviceList= FXCollections.observableArrayList(deviceList);
        deviceTableView.setItems(observableDeviceList);

        logger.info("Cleared filters");
    }

    public void applyFilter(){
        String deviceType= deviceTypeComboBox.getValue();

        switch(deviceType){
            case("Camera"):{
                String deviceName=deviceNameTextField.getText();
                List<Camera> allCameras= cameraService.findAllCameras();
                List<Device> allCamerasCasted = new ArrayList<>(allCameras);

                List<Device> filteredCameras=allCamerasCasted.parallelStream()
                        .filter(c->c.getDeviceName().contains(deviceName))
                        .collect(Collectors.toList());

                ObservableList<Device> observableCameraList=FXCollections.observableArrayList(filteredCameras);

                deviceTableView.setItems(observableCameraList);
                break;
            }
            case("CO2 Sensor"):{
                String deviceName=deviceNameTextField.getText();
                List<CO2Sensor> allCO2Sensors= co2SensorService.findAllCO2Sensors();
                List<Device> allCO2SensorsCasted = new ArrayList<>(allCO2Sensors);

                List<Device> filteredCO2Sensors=allCO2SensorsCasted.parallelStream()
                        .filter(c->c.getDeviceName().contains(deviceName))
                        .collect(Collectors.toList());

                ObservableList<Device> observableCO2SensorList=FXCollections.observableArrayList(filteredCO2Sensors);

                deviceTableView.setItems(observableCO2SensorList);
                break;
            }
            case("Glass Break Sensor"):{
                String deviceName=deviceNameTextField.getText();

                List<GlassBreakSensor> allGlassBreakSensors= glassBreakSensorService.findAllGlassBreakSensors();
                List<Device> allGlassBreakSensorsCasted = new ArrayList<>(allGlassBreakSensors);

                List<Device> filteredGlassBreakSensors=allGlassBreakSensorsCasted.parallelStream()
                        .filter(c->c.getDeviceName().contains(deviceName))
                        .collect(Collectors.toList());

                ObservableList<Device> observableGlassBreakSensorList=FXCollections.observableArrayList(filteredGlassBreakSensors);

                deviceTableView.setItems(observableGlassBreakSensorList);
                break;
            }
            case("Heat Sensor"):{
                String deviceName=deviceNameTextField.getText();
                List<HeatSensor> allHeatSensors= heatSensorService.findAllHeatSensors();
                List<Device> allHeatSensorsCasted = new ArrayList<>(allHeatSensors);

                List<Device> filteredHeatSensors=allHeatSensorsCasted.parallelStream()
                        .filter(c->c.getDeviceName().contains(deviceName))
                        .collect(Collectors.toList());

                ObservableList<Device> observableHeatSensorList=FXCollections.observableArrayList(filteredHeatSensors);

                deviceTableView.setItems(observableHeatSensorList);
                break;
            }
            case("Motion Sensor"):{
                String deviceName=deviceNameTextField.getText();

                List<MotionSensor> allMotionSensors = motionSensorService.findAllMotionSensors();
                List<Device> allMotionSensorsCasted = new ArrayList<>(allMotionSensors);

                List<Device> filteredMotionSensors=allMotionSensorsCasted.parallelStream()
                        .filter(c->c.getDeviceName().contains(deviceName))
                        .collect(Collectors.toList());

                ObservableList<Device> observableMotionSensorList=FXCollections.observableArrayList(filteredMotionSensors);

                deviceTableView.setItems(observableMotionSensorList);
                break;
            }
            case("Smoke Sensor"):{
                String deviceName=deviceNameTextField.getText();

                List<SmokeSensor> allSmokeSensors= smokeSensorService.findAllSmokeSensors();
                List<Device> allSmokeSensorsCasted = new ArrayList<>(allSmokeSensors);

                List<Device> filteredSmokeSensors=allSmokeSensorsCasted.parallelStream()
                        .filter(c->c.getDeviceName().contains(deviceName))
                        .collect(Collectors.toList());

                ObservableList<Device> observableSmokeSensorList=FXCollections.observableArrayList(filteredSmokeSensors);

                deviceTableView.setItems(observableSmokeSensorList);
                break;
            }
        }
        logger.info("Successfully applied a filter on All Devices Screen");
    }

    public void deleteDevice(){
        try{
            if(AuthenticationUtil.currentUser.getAdministrator()){
                Optional<Device> selectedDeviceOptional=getClickedDevice();

                if(selectedDeviceOptional.isEmpty()){
                    Platform.runLater(()->{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Failed to retrieve the device");
                        alert.setContentText("Please click the device and try again.");
                        alert.showAndWait();
                        logger.error("The device on the main screen has not been clicked");
                    });
                }
                else{
                    Device selectedDevice=selectedDeviceOptional.get();
                    initialize();
                    Change change=new Change(selectedDevice.getDeviceName(), selectedDevice.getDeviceType().getDeviceType(), "Device: " + selectedDevice.getDeviceName() + " has successfully been deleted.", LocalDateTime.now());

                    switch(selectedDevice.getDeviceType().getDeviceType()){
                        case "Camera":{
                            DeleteCameraThread deleteCameraThread = deleteCameraThreadFactory.create((Camera)selectedDevice);
                            Executor executor = Executors.newSingleThreadExecutor();
                            executor.execute(deleteCameraThread);
                            logger.info("Camera deleted");
                            break;
                        }
                        case "CO2 Sensor":{
                            DeleteCO2SensorThread deleteCO2SensorThread = deleteCO2SensorThreadFactory.create((CO2Sensor)selectedDevice);
                            Executor executor = Executors.newSingleThreadExecutor();
                            executor.execute(deleteCO2SensorThread);
                            logger.info("C02 Sensor deleted");
                            break;
                        }
                        case "Glass Break Sensor":{
                            DeleteGlassBreakSensorThread deleteGlassBreakSensorThread = deleteGlassBreakSensorThreadFactory.create((GlassBreakSensor) selectedDevice);
                            Executor executor = Executors.newSingleThreadExecutor();
                            executor.execute(deleteGlassBreakSensorThread);
                            logger.info("Glass Break Sensor deleted");
                            break;
                        }
                        case "Heat Sensor":{
                            DeleteHeatSensorThread deleteHeatSensorThread = deleteHeatSensorThreadFactory.create((HeatSensor) selectedDevice);
                            Executor executor = Executors.newSingleThreadExecutor();
                            executor.execute(deleteHeatSensorThread);
                            logger.info("Heat Sensor deleted");
                            break;
                        }
                        case "Motion Sensor":{
                            DeleteMotionSensorThread deleteMotionSensorThread = deleteMotionSensorThreadFactory.create((MotionSensor) selectedDevice);
                            Executor executor = Executors.newSingleThreadExecutor();
                            executor.execute(deleteMotionSensorThread);
                            logger.info("Motion Sensor deleted");
                            break;
                        }
                        case "Smoke Sensor":{
                            DeleteSmokeSensorThread deleteSmokeSensorThread = deleteSmokeSensorThreadFactory.create((SmokeSensor) selectedDevice);
                            Executor executor = Executors.newSingleThreadExecutor();
                            executor.execute(deleteSmokeSensorThread);
                            logger.info("Smoke Sensor deleted");
                            break;
                        }
                    }
                    FileUtil.serializeChange(change);
                }
            } else{
                throw new NoPrivilegeException();
            }
        } catch(NoPrivilegeException ex){
            Platform.runLater(()->{
                logger.error("No privileges to disconnect sensors");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No privileges for this account");
                alert.setContentText("Please log in as an administrator");
                alert.showAndWait();
            });
        }

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            clearFilters();
        });
        pause.play();
    }

    public void updateDevice(){
        try{
            if(AuthenticationUtil.currentUser.getAdministrator()){
                    Optional<Device> selectedDeviceOptional=getClickedDevice();

                    if(selectedDeviceOptional.isEmpty()) {
                        Platform.runLater(()->{
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Failed to retrieve the device");
                            alert.setContentText("Please click the device and try again.");
                            alert.showAndWait();
                        });

                    }else {

                        Device device=selectedDeviceOptional.get();
                        switch(device.getDeviceType().getDeviceType()){
                            case("Camera"):{
                                Platform.runLater(()->{
                                    TextInputDialog dialog= new TextInputDialog();
                                    dialog.setTitle("Rename Camera");
                                    dialog.setHeaderText("Enter The new name for the camera: ");
                                    dialog.setContentText("New Name: ");
                                    String result;

                                    try{
                                        result=dialog.showAndWait().get();

                                        try{
                                            if(!result.isEmpty()){
                                                Change change=new Change(device.getDeviceName(), device.getDeviceType().getDeviceType(), "Camera: " + device.getDeviceName() + " has successfully been updated.", LocalDateTime.now());
                                                device.setDeviceName(result);

                                                UpdateCameraThread updateCameraThread = updateCameraThreadFactory.create((Camera) device);
                                                Executor executor = Executors.newSingleThreadExecutor();
                                                executor.execute(updateCameraThread);

                                                logger.info("Camera name changed");

                                                FileUtil.serializeChange(change);

                                                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                                                pause.setOnFinished(event -> {
                                                    clearFilters();
                                                });
                                                pause.play();
                                            }
                                            else{
                                                throw new EmptyFieldException();
                                            }
                                        }catch( EmptyFieldException ex){
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Error");
                                            alert.setHeaderText("Text field is empty");
                                            alert.setContentText("Please fill out the empty text field to update the name");
                                            alert.showAndWait();
                                        }
                                    }
                                    catch(Exception ignored){

                                    }
                                });
                                break;
                            }
                            case("Glass Break Sensor"):{

                                Platform.runLater(()->{
                                    TextInputDialog dialog= new TextInputDialog();
                                    dialog.setTitle("Rename Glass Break Sensor");
                                    dialog.setHeaderText("Enter The new name for the Glass Break Sensor: ");
                                    dialog.setContentText("New Name: ");

                                    String result;
                                    try{
                                        result=dialog.showAndWait().get();

                                        try{
                                            if(!result.isEmpty()){
                                                Change change=new Change(device.getDeviceName(), device.getDeviceType().getDeviceType(), "Glass Break Sensor: " + device.getDeviceName() + " has successfully been updated.", LocalDateTime.now());
                                                device.setDeviceName(result);

                                                UpdateGlassBreakSensorThread updateGlassBreakSensorThread = updateGlassBreakSensorThreadFactory.create((GlassBreakSensor) device);
                                                Executor executor = Executors.newSingleThreadExecutor();
                                                executor.execute(updateGlassBreakSensorThread);

                                                logger.info("Glass Break Sensor name changed");

                                                FileUtil.serializeChange(change);

                                                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                                                pause.setOnFinished(event -> {
                                                    clearFilters();
                                                });
                                                pause.play();

                                            }
                                            else{
                                                throw new EmptyFieldException();
                                            }
                                        }catch( EmptyFieldException ex){
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Error");
                                            alert.setHeaderText("Text field is empty");
                                            alert.setContentText("Please fill out the empty text field to update the name");
                                            logger.error("Empty field while changing Glass Break Sensor name");
                                            alert.showAndWait();
                                        }

                                    }
                                    catch(Exception ignored){

                                    }
                                });

                                break;

                            }
                            case("CO2 Sensor"):{

                                Platform.runLater(()->{
                                    TextInputDialog dialog= new TextInputDialog();
                                    dialog.setTitle("Rename CO2 Sensor");
                                    dialog.setHeaderText("Enter The new name for the CO2 Sensor: ");
                                    dialog.setContentText("New Name: ");
                                    String result;

                                    try{
                                        result=dialog.showAndWait().get();
                                        try{
                                            if(!result.isEmpty()){
                                                Change change=new Change(device.getDeviceName(), device.getDeviceType().getDeviceType(), "CO2 Sensor: " + device.getDeviceName() + " has successfully been updated.", LocalDateTime.now());
                                                device.setDeviceName(result);

                                                UpdateCO2SensorThread updateCO2SensorThread = updateCO2SensorThreadFactory.create((CO2Sensor) device);
                                                Executor executor = Executors.newSingleThreadExecutor();
                                                executor.execute(updateCO2SensorThread);

                                                logger.info("CO2 sensor name changed");

                                                FileUtil.serializeChange(change);

                                                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                                                pause.setOnFinished(event -> {
                                                    clearFilters();
                                                });
                                                pause.play();

                                            }
                                            else{
                                                throw new EmptyFieldException();
                                            }
                                        }catch( EmptyFieldException ex){
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Error");
                                            alert.setHeaderText("Text field is empty");
                                            alert.setContentText("Please fill out the empty text field to update the name");
                                            logger.error("Empty field while changing CO2 sensor name", ex);
                                            alert.showAndWait();
                                        }

                                    }
                                    catch(Exception ignored){}
                                });
                                break;
                            }
                            case("Heat Sensor"):{

                                Platform.runLater(()->{
                                    TextInputDialog dialog= new TextInputDialog();
                                    dialog.setTitle("Rename Heat Sensor");
                                    dialog.setHeaderText("Enter The new name for the Heat Sensor: ");
                                    dialog.setContentText("New Name: ");

                                    String result;
                                    try{
                                        result=dialog.showAndWait().get();
                                        try{
                                            if(!result.isEmpty()){
                                                Change change=new Change(device.getDeviceName(), device.getDeviceType().getDeviceType(), "Heat Sensor: " + device.getDeviceName() + " has successfully been updated.", LocalDateTime.now());
                                                device.setDeviceName(result);

                                                UpdateHeatSensorThread updateHeatSensorThread = updateHeatSensorThreadFactory.create((HeatSensor) device);
                                                Executor executor = Executors.newSingleThreadExecutor();
                                                executor.execute(updateHeatSensorThread);

                                                logger.info("Heat sensor name changed");

                                                FileUtil.serializeChange(change);

                                                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                                                pause.setOnFinished(event -> {
                                                    clearFilters();
                                                });
                                                pause.play();

                                            }
                                            else{
                                                throw new EmptyFieldException();
                                            }
                                        }catch( EmptyFieldException ex){
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Error");
                                            alert.setHeaderText("Text field is empty");
                                            alert.setContentText("Please fill out the empty text field to update the name");
                                            logger.error("Empty field while changing Heat sensor name", ex);
                                            alert.showAndWait();
                                        }
                                    }
                                    catch(Exception ignored){}
                                });
                                break;
                            }
                            case("Smoke Sensor"):{

                                Platform.runLater(()->{
                                    TextInputDialog dialog= new TextInputDialog();
                                    dialog.setTitle("Rename Smoke Sensor");
                                    dialog.setHeaderText("Enter The new name for the Smoke Sensor: ");
                                    dialog.setContentText("New Name: ");

                                    String result;
                                    try{
                                        result=dialog.showAndWait().get();
                                        try{
                                            if(!result.isEmpty()){
                                                Change change=new Change(device.getDeviceName(), device.getDeviceType().getDeviceType(), "Smoke Sensor: " + device.getDeviceName() + " has successfully been updated.", LocalDateTime.now());
                                                device.setDeviceName(result);

                                                UpdateSmokeSensorThread updateSmokeSensorThread = updateSmokeSensorThreadFactory.create((SmokeSensor) device);
                                                Executor executor = Executors.newSingleThreadExecutor();
                                                executor.execute(updateSmokeSensorThread);

                                                logger.info("Smoke sensor name changed");

                                                FileUtil.serializeChange(change);

                                                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                                                pause.setOnFinished(event -> {
                                                    clearFilters();
                                                });
                                                pause.play();

                                            }
                                            else{
                                                throw new EmptyFieldException();
                                            }
                                        }catch( EmptyFieldException ex){
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Error");
                                            alert.setHeaderText("Text field is empty");
                                            alert.setContentText("Please fill out the empty text field to update the name");
                                            logger.error("Empty field while changing Smoke Sensor name", ex);
                                            alert.showAndWait();
                                        }

                                    }
                                    catch(Exception ignored){}
                                });


                                break;
                            }
                            case("Motion Sensor"):{

                                Platform.runLater(()->{
                                    TextInputDialog dialog= new TextInputDialog();
                                    dialog.setTitle("Rename Motion Sensor");
                                    dialog.setHeaderText("Enter The new name for the Motion Sensor: ");
                                    dialog.setContentText("New Name: ");

                                    String result;
                                    try{
                                        result=dialog.showAndWait().get();
                                        try{
                                            if(!result.isEmpty()){
                                                Change change=new Change(device.getDeviceName(), device.getDeviceType().getDeviceType(), "Motion Sensor: " + device.getDeviceName() + " has successfully been updated.", LocalDateTime.now());
                                                device.setDeviceName(result);
                                                UpdateMotionSensorThread updateMotionSensorThread = updateMotionSensorThreadFactory.create((MotionSensor) device);
                                                Executor executor = Executors.newSingleThreadExecutor();
                                                executor.execute(updateMotionSensorThread);

                                                FileUtil.serializeChange(change);

                                                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                                                pause.setOnFinished(event -> {
                                                    clearFilters();
                                                });
                                                pause.play();
                                            }
                                            else{
                                                throw new EmptyFieldException();
                                            }
                                        }catch( EmptyFieldException ex){
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Error");
                                            alert.setHeaderText("Text field is empty");
                                            alert.setContentText("Please fill out the empty text field to update the name");
                                            alert.showAndWait();
                                        }
                                    }
                                    catch(Exception ignored){}
                                });

                                break;
                            }
                        }
                    }

            } else{
                throw new NoPrivilegeException();
            }
        } catch(NoPrivilegeException ex){
            Platform.runLater(()->{
                logger.error("No privileges to change device's name");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No privileges for this account");
                alert.setContentText("Please log in as an administrator");
                alert.showAndWait();
            });
        }
    }
    public void searchByName(){
        List<Device> deviceList=findAllDevices();

        String deviceName=deviceNameTextField.getText();

        List<Device> filteredDevices=deviceList.parallelStream()
                .filter(c->c.getDeviceName().contains(deviceName))
                .collect(Collectors.toList());

        ObservableList<Device> observableDeviceList=FXCollections.observableArrayList(filteredDevices);

        deviceTableView.setItems(observableDeviceList);
    }
    public Optional<Device> getClickedDevice(){

        TableView.TableViewSelectionModel<Device> selectionModel = deviceTableView.getSelectionModel();

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
    public void deviceControl(){
        try{
            deviceControlHelper();
            clearFilters();
        } catch(NoPrivilegeException ex){
            Platform.runLater(()->{
                logger.error("No privileges to access change device status");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error setting status");
                alert.setHeaderText("No privileges for this account");
                alert.setContentText("Please log in as an administrator");
                alert.showAndWait();
            });

        }


    }
    private void deviceControlHelper() throws NoPrivilegeException {
        if(AuthenticationUtil.currentUser.getAdministrator()){
            Optional<Device> selectedDeviceOptional = getClickedDevice();
            if(selectedDeviceOptional.isEmpty()){

                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Failed to retrieve the device");
                    alert.setContentText("Please click the device and try again.");
                    logger.info("Device wasn't selected");
                    alert.showAndWait();
                });

            }
            else {
                Device selectedDevice=selectedDeviceOptional.get();
                Change change= new Change(selectedDevice.getDeviceName(), selectedDevice.getDeviceType().getDeviceType(), "Device's ("+selectedDevice.getDeviceName()+ ") status has been changed.", LocalDateTime.now());

                if(selectedDevice.getDeviceStatus()){
                    selectedDevice.setDeviceStatus(false);
                }
                else{
                    selectedDevice.setDeviceStatus(true);
                }
                switch(selectedDevice.getDeviceType().getDeviceType()){
                    case "Camera":{
                        UpdateCameraThread updateCameraThread = updateCameraThreadFactory.create((Camera)selectedDevice);
                        Executor executor = Executors.newSingleThreadExecutor();
                        executor.execute(updateCameraThread);
                        logger.info("Camera status changed");
                        break;
                    }
                    case "CO2 Sensor":{
                        UpdateCO2SensorThread updateCO2SensorThread = updateCO2SensorThreadFactory.create((CO2Sensor)selectedDevice);
                        Executor executor = Executors.newSingleThreadExecutor();
                        executor.execute(updateCO2SensorThread);
                        logger.info("CO2 Sensor status changed");
                        break;
                    }
                    case "Glass Break Sensor":{
                        UpdateGlassBreakSensorThread updateGlassBreakSensorThread = updateGlassBreakSensorThreadFactory.create((GlassBreakSensor) selectedDevice);
                        Executor executor = Executors.newSingleThreadExecutor();
                        executor.execute(updateGlassBreakSensorThread);
                        logger.info("Glass Break Sensor status changed");
                        break;
                    }
                    case "Heat Sensor":{
                        UpdateHeatSensorThread updateHeatSensorThread = updateHeatSensorThreadFactory.create((HeatSensor) selectedDevice);
                        Executor executor = Executors.newSingleThreadExecutor();
                        executor.execute(updateHeatSensorThread);
                        logger.info("Heat Sensor status changed");
                        break;
                    }
                    case "Motion Sensor":{
                        UpdateMotionSensorThread updateMotionSensorThread = updateMotionSensorThreadFactory.create((MotionSensor) selectedDevice);
                        Executor executor = Executors.newSingleThreadExecutor();
                        executor.execute(updateMotionSensorThread);
                        logger.info("Motion Sensor status changed");
                        break;
                    }
                    case "Smoke Sensor":{
                        UpdateSmokeSensorThread updateSmokeSensorThread = updateSmokeSensorThreadFactory.create((SmokeSensor) selectedDevice);
                        Executor executor = Executors.newSingleThreadExecutor();
                        executor.execute(updateSmokeSensorThread);
                        logger.info("Smoke Sensor status changed");
                        break;
                    }
                }
                try{
                    FileUtil.serializeChange(change);
                }
                catch(Exception ex){
                    Platform.runLater(()->{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Failed to save Change to file");
                        alert.setContentText("Please try again");
                        logger.info("Change failed to save to file.");
                        alert.showAndWait();
                    });
                }
            }
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> {
                clearFilters();
            });
            pause.play();
        }
        else{
            throw new NoPrivilegeException();
        }
    }

}
