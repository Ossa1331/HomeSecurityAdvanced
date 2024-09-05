package com.example.homesecurity.controller;

import com.example.homesecurity.entity.*;
import com.example.homesecurity.enums.DeviceType;
import com.example.homesecurity.factory.*;
import com.example.homesecurity.service.*;
import com.example.homesecurity.thread.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Duration;
import com.example.homesecurity.util.MultiObjectWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Controller
public class MeasurableController {

    Logger logger= LoggerFactory.getLogger(MeasurableController.class);

    private static final Executor executor= Executors.newSingleThreadExecutor();

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

    @FXML
    private ComboBox<String> deviceTypeComboBox;
    @FXML
    private TableView<MultiObjectWrapper> deviceTableView;
    @FXML
    private TableColumn<MultiObjectWrapper,String> deviceNameTableColumn;
    @FXML
    private TableColumn<MultiObjectWrapper, String> deviceTypeTableColumn;
    @FXML
    private TableColumn<MultiObjectWrapper, String> deviceResultTableColumn;

    public void initialize(){
        List<String> typeList=new ArrayList<>();
        typeList.add("Camera");
        typeList.add("CO2 Sensor");
        typeList.add("Glass Break Sensor");
        typeList.add("Heat Sensor");
        typeList.add("Motion Sensor");
        typeList.add("Smoke Sensor");

        ObservableList<String> observableTypeList= FXCollections.observableList(typeList);

        deviceTypeComboBox.setItems(observableTypeList);
        deviceTypeComboBox.setValue(observableTypeList.getFirst());

        List<MultiObjectWrapper> allDevices = getAllDevices();


        deviceNameTableColumn.setCellValueFactory(cellData -> {
            Object object = cellData.getValue().object();
            return switch (object) {
                case Camera camera -> Bindings.createStringBinding(camera::getDeviceName);
                case CO2Sensor co2Sensor -> Bindings.createStringBinding(co2Sensor::getDeviceName);
                case GlassBreakSensor glassBreakSensor ->
                        Bindings.createStringBinding((glassBreakSensor)::getDeviceName);
                case HeatSensor heatSensor -> Bindings.createStringBinding(heatSensor::getDeviceName);
                case MotionSensor motionSensor ->
                        Bindings.createStringBinding(motionSensor::getDeviceName);
                case SmokeSensor smokeSensor -> Bindings.createStringBinding(smokeSensor::getDeviceName);
                default -> throw new IllegalStateException("Unexpected value: " + object);
            };
        });
        deviceTypeTableColumn.setCellValueFactory(cellData -> {
            Object object = cellData.getValue().object();
            return switch (object) {
                case Camera camera -> Bindings.createStringBinding(()->camera.getDeviceType().getDeviceType());
                case CO2Sensor co2Sensor -> Bindings.createStringBinding(()->co2Sensor.getDeviceType().getDeviceType());
                case GlassBreakSensor glassBreakSensor ->
                        Bindings.createStringBinding(()->glassBreakSensor.getDeviceType().getDeviceType());
                case HeatSensor heatSensor -> Bindings.createStringBinding(()->heatSensor.getDeviceType().getDeviceType());
                case MotionSensor motionSensor ->
                        Bindings.createStringBinding(()->motionSensor.getDeviceType().getDeviceType());
                case SmokeSensor smokeSensor -> Bindings.createStringBinding(()->smokeSensor.getDeviceType().getDeviceType());
                default -> throw new IllegalStateException("Unexpected value: " + object);
            };
        });
        deviceResultTableColumn.setCellValueFactory(cellData -> {
            Object object = cellData.getValue().object();
            switch (object) {
                case Camera camera -> {
                    if (camera.getDeviceStatus()) {
                        return Bindings.createStringBinding(() -> camera.getHumanIdentified().toString());
                    } else {
                        return Bindings.createStringBinding(() -> "This device is currently disabled");
                    }
                }
                case CO2Sensor co2Sensor -> {
                    Float temp=co2Sensor.getCurrentCO2();
                    String formattedTemp=String.format("%.2f", temp);

                    if (co2Sensor.getDeviceStatus()) {
                        return Bindings.createStringBinding(() -> formattedTemp + " micromol");
                    } else {
                        return Bindings.createStringBinding(() -> "This device is currently disabled");
                    }
                }
                case GlassBreakSensor glassBreakSensor -> {
                    if (glassBreakSensor.getDeviceStatus()) {
                        return Bindings.createStringBinding(() -> glassBreakSensor.getGlassBreak().toString());
                    } else {
                        return Bindings.createStringBinding(() -> "This device is currently disabled");
                    }
                }
                case HeatSensor heatSensor -> {
                    if (heatSensor.getDeviceStatus()) {
                        Double temp=heatSensor.getTemperatureInC();
                        String formattedTemp=String.format("%.2f", temp);
                        return Bindings.createStringBinding(() -> formattedTemp + "C");
                    } else {
                        return Bindings.createStringBinding(() -> "This device is currently disabled");
                    }
                }
                case MotionSensor motionSensor -> {
                    if (motionSensor.getDeviceStatus()) {
                        return Bindings.createStringBinding(() -> motionSensor.isAnomalyDetected().toString());
                    } else {
                        return Bindings.createStringBinding(() -> "This device is currently disabled");
                    }
                }
                case SmokeSensor smokeSensor -> {
                    Double temp=smokeSensor.getCurrentObscuration();
                    String formattedTemp=String.format("%.2f", temp);

                    if ((smokeSensor.getDeviceStatus())) {
                        return Bindings.createStringBinding(() -> formattedTemp + "%");

                    } else {
                        return Bindings.createStringBinding(() -> "This device is currently disabled");
                    }
                }
                default -> throw new IllegalStateException("Unexpected value: " + object);
            }
        });

        deviceTableView.setItems(FXCollections.observableList(allDevices));

        logger.info("MeasurableController has been initialized");

        startMeasuring();

    }

    private List<MultiObjectWrapper> getAllDevices() {
        List<Camera> allCameras=cameraService.findAllCameras();
        List<CO2Sensor> allCO2Sensors=co2SensorService.findAllCO2Sensors();
        List<GlassBreakSensor> allGlassBreakSensors=glassBreakSensorService.findAllGlassBreakSensors();
        List<HeatSensor>allHeatSensors= heatSensorService.findAllHeatSensors();
        List<MotionSensor> allMotionSensors=motionSensorService.findAllMotionSensors();
        List<SmokeSensor> allSmokeSensors=smokeSensorService.findAllSmokeSensors();
        List<MultiObjectWrapper> allDevices=new ArrayList<>();

        for(Camera camera:allCameras){
            MultiObjectWrapper wrapper=new MultiObjectWrapper(camera);
            allDevices.add(wrapper);
        }
        for(CO2Sensor co2Sensor:allCO2Sensors){
            MultiObjectWrapper wrapper=new MultiObjectWrapper(co2Sensor);
            allDevices.add(wrapper);
        }
        for(GlassBreakSensor glassBreakSensor:allGlassBreakSensors){
            MultiObjectWrapper wrapper=new MultiObjectWrapper(glassBreakSensor);
            allDevices.add(wrapper);
        }
        for(HeatSensor heatSensor:allHeatSensors){
            MultiObjectWrapper wrapper=new MultiObjectWrapper(heatSensor);
            allDevices.add(wrapper);
        }
        for(MotionSensor motionSensor:allMotionSensors){
            MultiObjectWrapper wrapper=new MultiObjectWrapper(motionSensor);
            allDevices.add(wrapper);
        }
        for(SmokeSensor smokeSensor:allSmokeSensors){
            MultiObjectWrapper wrapper=new MultiObjectWrapper(smokeSensor);
            allDevices.add(wrapper);
        }

        logger.info("Successfully returned all devices.");
        return allDevices;
    }

    private void startMeasuring() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(20), event -> {

            List<MultiObjectWrapper> allDevices=getAllDevices();

            for(MultiObjectWrapper object:allDevices){
                switch (object.object()) {
                    case Camera camera -> {
                        if (camera.getDeviceStatus()) {

                            Camera tempCamera=((Camera) object.object());
                            tempCamera.setHumanIdentified(tempCamera.measure());

                            Event tempEvent=new Event(LocalDateTime.now(),tempCamera.getDeviceName(),tempCamera.getDeviceType().getDeviceType());
                            tempEvent.setAddress(ChooseLocationController.currentLocation);
                            tempEvent.checkTriggerEvent(tempCamera);

                            UpdateCameraThread updateCameraThread=updateCameraThreadFactory.create(tempCamera, tempEvent);
                            executor.execute(updateCameraThread);

                            logger.info("Camera: " + tempCamera.getDeviceName() + " measured successfully");

                        }
                    }
                    case CO2Sensor co2Sensor -> {
                        if (co2Sensor.getDeviceStatus()) {

                            CO2Sensor tempCO2Sensor=((CO2Sensor) object.object());
                            tempCO2Sensor.setCurrentCO2(tempCO2Sensor.measure());

                            Event tempEvent=new Event(LocalDateTime.now(),tempCO2Sensor.getDeviceName(),tempCO2Sensor.getDeviceType().getDeviceType());
                            tempEvent.setAddress(ChooseLocationController.currentLocation);
                            tempEvent.checkTriggerEvent(tempCO2Sensor);

                            UpdateCO2SensorThread updateCO2SensorThread=updateCO2SensorThreadFactory.create(tempCO2Sensor, tempEvent);
                            executor.execute(updateCO2SensorThread);

                            logger.info("CO2 Sensor: " + tempCO2Sensor.getDeviceName() + " measured successfully");

                        }
                    }
                    case GlassBreakSensor glassBreakSensor -> {
                        if (glassBreakSensor.getDeviceStatus()) {

                            GlassBreakSensor tempGlassBreakSensor=((GlassBreakSensor) object.object());
                            tempGlassBreakSensor.setGlassBreak(tempGlassBreakSensor.measure());

                            Event tempEvent=new Event(LocalDateTime.now(),tempGlassBreakSensor.getDeviceName(),tempGlassBreakSensor.getDeviceType().getDeviceType());
                            tempEvent.setAddress(ChooseLocationController.currentLocation);
                            tempEvent.checkTriggerEvent(tempGlassBreakSensor);

                            UpdateGlassBreakSensorThread updateGlassBreakSensorThread=updateGlassBreakSensorThreadFactory.create(tempGlassBreakSensor, tempEvent);
                            executor.execute(updateGlassBreakSensorThread);

                            logger.info("Glass Break Sensor: " + tempGlassBreakSensor.getDeviceName() + " measured successfully");

                        }
                    }
                    case HeatSensor heatSensor -> {
                        if (heatSensor.getDeviceStatus()) {

                            HeatSensor tempHeatSensor=((HeatSensor) object.object());
                            tempHeatSensor.setTemperatureInC(tempHeatSensor.measure());

                            Event tempEvent=new Event(LocalDateTime.now(),tempHeatSensor.getDeviceName(),tempHeatSensor.getDeviceType().getDeviceType());
                            tempEvent.setAddress(ChooseLocationController.currentLocation);
                            tempEvent.checkTriggerEvent(tempHeatSensor);

                            UpdateHeatSensorThread updateHeatSensorThread=updateHeatSensorThreadFactory.create(tempHeatSensor,tempEvent);
                            executor.execute(updateHeatSensorThread);

                            logger.info("Heat Sensor: " + tempHeatSensor+ " measured successfully");
                        }
                    }
                    case MotionSensor motionSensor -> {
                        if (motionSensor.getDeviceStatus()) {

                            MotionSensor tempMotionSensor=((MotionSensor) object.object());
                            tempMotionSensor.setAnomalyDetected(tempMotionSensor.measure());

                            Event tempEvent=new Event(LocalDateTime.now(),tempMotionSensor.getDeviceName(),tempMotionSensor.getDeviceType().getDeviceType());
                            tempEvent.setAddress(ChooseLocationController.currentLocation);
                            tempEvent.checkTriggerEvent(tempMotionSensor);

                            UpdateMotionSensorThread updateMotionSensorThread=updateMotionSensorThreadFactory.create(tempMotionSensor, tempEvent);
                            executor.execute(updateMotionSensorThread);

                            logger.info("Motion Sensor: " + tempMotionSensor + " measured successfully");
                        }
                    }
                    case null, default -> {
                        if (((SmokeSensor) object.object()).getDeviceStatus()) {

                            SmokeSensor tempSmokeSensor=((SmokeSensor) object.object());
                            tempSmokeSensor.setCurrentObscuration(tempSmokeSensor.measure());

                            Event tempEvent=new Event(LocalDateTime.now(),tempSmokeSensor.getDeviceName(),tempSmokeSensor.getDeviceType().getDeviceType());
                            tempEvent.setAddress(ChooseLocationController.currentLocation);
                            tempEvent.checkTriggerEvent(tempSmokeSensor);

                            UpdateSmokeSensorThread updateSmokeSensorThread=updateSmokeSensorThreadFactory.create(tempSmokeSensor, tempEvent);
                            executor.execute(updateSmokeSensorThread);

                            logger.info("Smoke Sensor: " + tempSmokeSensor.getDeviceName() + " measured successfully");

                        }
                    }
                }
            }
            deviceTableView.setItems(FXCollections.observableList(allDevices));
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);

        Platform.runLater(timeline::play);
    }

    public void applyFilter(){
        String deviceType= deviceTypeComboBox.getValue();

        switch(deviceType){
            case("Camera"):{
                List<Camera> allCameras= cameraService.findAllCameras();
                List<Camera> filteredCameras=allCameras.parallelStream()
                        .filter(c-> c.getDeviceType().equals(DeviceType.CAMERA))
                        .toList();

                List<MultiObjectWrapper> filteredCamerasWrapper=new ArrayList<>();

                for(Camera camera:filteredCameras){
                    MultiObjectWrapper filteredCamera=new MultiObjectWrapper(camera);
                    filteredCamerasWrapper.add(filteredCamera);
                }
                ObservableList<MultiObjectWrapper> observableCameraList=FXCollections.observableArrayList(filteredCamerasWrapper);

                deviceTableView.setItems(observableCameraList);

                logger.info("Camera filter applied in MeasurableController");
                break;
            }
            case("CO2 Sensor"):{
                List<CO2Sensor> allSensors= co2SensorService.findAllCO2Sensors();
                List<CO2Sensor> filteredSensors=allSensors.parallelStream()
                        .filter(c-> c.getDeviceType().equals(DeviceType.CO2_SENSOR))
                        .toList();

                List<MultiObjectWrapper> filteredSensorsWrapper=new ArrayList<>();

                for(CO2Sensor camera:filteredSensors){
                    MultiObjectWrapper filteredCamera=new MultiObjectWrapper(camera);
                    filteredSensorsWrapper.add(filteredCamera);
                }
                ObservableList<MultiObjectWrapper> observableCameraList=FXCollections.observableArrayList(filteredSensorsWrapper);

                deviceTableView.setItems(observableCameraList);

                logger.info("CO2Sensor filter applied in MeasurableController");
                break;
            }
            case("Glass Break Sensor"):{
                List<GlassBreakSensor> allSensors= glassBreakSensorService.findAllGlassBreakSensors();
                List<GlassBreakSensor> filteredSensors=allSensors.parallelStream()
                        .filter(c-> c.getDeviceType().equals(DeviceType.GLASS_BREAK_SENSOR))
                        .toList();

                List<MultiObjectWrapper> filteredSensorsWrapper=new ArrayList<>();

                for(GlassBreakSensor sensor:filteredSensors){
                    MultiObjectWrapper filteredCamera=new MultiObjectWrapper(sensor);
                    filteredSensorsWrapper.add(filteredCamera);
                }
                ObservableList<MultiObjectWrapper> observableCameraList=FXCollections.observableArrayList(filteredSensorsWrapper);

                deviceTableView.setItems(observableCameraList);

                logger.info("Glass Break Sensor filter applied in MeasurableController");
                break;
            }
            case("Heat Sensor"):{
                List<HeatSensor> allSensors= heatSensorService.findAllHeatSensors();
                List<HeatSensor> filteredSensors=allSensors.parallelStream()
                        .filter(c-> c.getDeviceType().equals(DeviceType.HEAT_SENSOR))
                        .toList();

                List<MultiObjectWrapper> filteredSensorsWrapper=new ArrayList<>();

                for(HeatSensor sensor:filteredSensors){
                    MultiObjectWrapper filteredCamera=new MultiObjectWrapper(sensor);
                    filteredSensorsWrapper.add(filteredCamera);
                }
                ObservableList<MultiObjectWrapper> observableCameraList=FXCollections.observableArrayList(filteredSensorsWrapper);

                deviceTableView.setItems(observableCameraList);

                logger.info("Heat Sensor filter applied in MeasurableController");
                break;
            }
            case("Motion Sensor"):{
                List<MotionSensor> allSensors= motionSensorService.findAllMotionSensors();
                List<MotionSensor> filteredSensors=allSensors.parallelStream()
                        .filter(c-> c.getDeviceType().equals(DeviceType.MOTION_SENSOR))
                        .toList();

                List<MultiObjectWrapper> filteredSensorsWrapper=new ArrayList<>();

                for(MotionSensor sensor:filteredSensors){
                    MultiObjectWrapper filteredCamera=new MultiObjectWrapper(sensor);
                    filteredSensorsWrapper.add(filteredCamera);
                }
                ObservableList<MultiObjectWrapper> observableCameraList=FXCollections.observableArrayList(filteredSensorsWrapper);

                deviceTableView.setItems(observableCameraList);

                logger.info("Motion Sensor filter applied in MeasurableController");
                break;
            }
            case("Smoke Sensor"):{
                List<SmokeSensor> allSensors= smokeSensorService.findAllSmokeSensors();
                List<SmokeSensor> filteredSensors=allSensors.parallelStream()
                        .filter(c-> c.getDeviceType().equals(DeviceType.SMOKE_SENSOR))
                        .toList();

                List<MultiObjectWrapper> filteredSensorsWrapper=new ArrayList<>();

                for(SmokeSensor sensor:filteredSensors){
                    MultiObjectWrapper filteredCamera=new MultiObjectWrapper(sensor);
                    filteredSensorsWrapper.add(filteredCamera);
                }
                ObservableList<MultiObjectWrapper> observableCameraList=FXCollections.observableArrayList(filteredSensorsWrapper);

                deviceTableView.setItems(observableCameraList);

                logger.info("Smoke Sensor filter applied in MeasurableController");
                break;
            }
        }
    }


}
