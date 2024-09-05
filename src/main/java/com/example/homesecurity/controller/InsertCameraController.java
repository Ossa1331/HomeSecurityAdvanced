package com.example.homesecurity.controller;

import com.example.homesecurity.entity.Camera;
import com.example.homesecurity.entity.Change;
import com.example.homesecurity.enums.DeviceType;
import com.example.homesecurity.enums.Locations;
import com.example.homesecurity.exception.EmptyFieldException;
import com.example.homesecurity.exception.DuplicateSerialNumberException;
import com.example.homesecurity.factory.SaveCameraThreadFactory;
import com.example.homesecurity.service.CameraService;
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
import com.example.homesecurity.thread.SaveCameraThread;
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
public class InsertCameraController {
    private static final Logger logger= LoggerFactory.getLogger(InsertCameraController.class);

    @Autowired
    private SaveCameraThreadFactory saveCameraThreadFactory;

    @Autowired
    private CameraService cameraService;

    @FXML
    private TextField cameraNameTextField;
    @FXML
    private TextField cameraModelTextField;
    @FXML
    private TextField cameraManufacturerTextField;
    @FXML
    private TextField cameraSerialNumberTextField;
    @FXML
    private DatePicker cameraManufacturingDateDatePicker;
    @FXML
    private ComboBox<String> cameraLocationComboBox;

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

        ObservableList<String> observableLocationList= FXCollections.observableList(locationList);

        cameraLocationComboBox.setItems(observableLocationList);
        cameraLocationComboBox.setValue(observableLocationList.getFirst());

        cameraManufacturingDateDatePicker.setEditable(false);

        logger.info("InsertCameraController has been initialized");
    }

    public void saveNewCamera() {

        List<Camera> allCameras=cameraService.findAllCameras();

        String cameraName = cameraNameTextField.getText();
        String cameraModel = cameraModelTextField.getText();
        String cameraManufacturer = cameraManufacturerTextField.getText();
        String cameraSerialNumber = cameraSerialNumberTextField.getText();
        LocalDate cameraManufacturingDate=cameraManufacturingDateDatePicker.getValue();

            try {
                saveNewCameraHelper(cameraName, cameraModel, cameraManufacturer, cameraSerialNumber, cameraManufacturingDate, allCameras);

                logger.info("Camera: " + cameraName + " has successfully been saved to database");

            } catch (EmptyFieldException ex) {
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error saving object to database.");
                    alert.setContentText("Please fill all the fields");
                    logger.error("Some fields were left empty");
                    alert.showAndWait();
                });
            }
            catch(DuplicateSerialNumberException ex){
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error saving object to database.");
                    alert.setContentText("Equal serial numbers found. please try again.");
                    logger.error("Equal serial numbers found while saving camera");
                    alert.showAndWait();
                });
            }
        }
    private void saveNewCameraHelper(String cameraName, String cameraModel, String cameraManufacturer, String cameraSerialNumber, LocalDate cameraManufacturingDate, List<Camera> allCameras) throws EmptyFieldException {

        if(!cameraSerialNumber.isEmpty()){
            for(Camera camera: allCameras){
                if(camera.getSerialNumber().equals(cameraSerialNumber)){
                    throw new DuplicateSerialNumberException("Camera duplicate serial numbers found");
                }
            }
        }

        if (!cameraName.isEmpty() && !cameraModel.isEmpty() && !cameraManufacturer.isEmpty()
                && !cameraSerialNumber.isEmpty() && cameraManufacturingDate !=null) {

            Platform.runLater(()->{
                Locations deviceLocation = Locations.BACKYARD;
                switch (cameraLocationComboBox.getValue()) {
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

                Camera camera = createCamera(cameraName, cameraModel, cameraManufacturer, cameraSerialNumber, cameraManufacturingDate, deviceLocation);

                Change change = new Change(camera.getDeviceName(), camera.getDeviceType().getDeviceType(),
                        "Camera: " + camera.getDeviceName() + " has been successfully saved", LocalDateTime.now());
                FileUtil.serializeChange(change);

                SaveCameraThread saveCameraThread = saveCameraThreadFactory.create(camera);
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(saveCameraThread);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information");
                alert.setHeaderText("Successful operation");
                alert.setContentText("Camera: " + camera.getDeviceName() + " has been successfully saved");
                alert.showAndWait();
            });

        }
        else {
            throw new EmptyFieldException();
        }
    }

    public Camera createCamera(String cameraName, String cameraModel, String cameraManufacturer, String cameraSerialNumber, LocalDate cameraManufacturingDate, Locations deviceLocation) {
        Camera camera = (Camera) Camera.builder()
                .deviceManufacturer(cameraManufacturer)
                .deviceType(DeviceType.CAMERA)
                .deviceModel(cameraModel)
                .deviceStatus(false)
                .deviceSerialNumber(cameraSerialNumber)
                .location(deviceLocation)
                .currentCameraTime(LocalDateTime.now())
                .humanIdentified(false)
                .deviceManufacturingDate(cameraManufacturingDate)
                .deviceName(cameraName)
                .build();

        camera.setAddress(ChooseLocationController.currentLocation);
        return camera;
    }

}