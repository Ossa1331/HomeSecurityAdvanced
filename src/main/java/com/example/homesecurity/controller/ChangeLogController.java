package com.example.homesecurity.controller;

import com.example.homesecurity.entity.Change;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.example.homesecurity.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Controller
public class ChangeLogController {

    @FXML
    private TableView<Change> changeTableView;
    @FXML
    private TableColumn<Change, String> deviceNameChangeTableColumn;
    @FXML
    private TableColumn<Change, String> deviceTypeChangeTableColumn;
    @FXML
    private TableColumn<Change, String> messageChangeTableColumn;
    @FXML
    private TableColumn<Change, String> changeTimeAddedTableColumn;

    private static final Logger logger = LoggerFactory.getLogger(ChangeLogController.class);


    public void initialize(){

        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        deviceNameChangeTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getDeviceName()));
        deviceTypeChangeTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getDeviceType()));
        changeTimeAddedTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getTimeAdded().format(formatter)));
        messageChangeTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getMessage()));

        List<Change>allChanges= FileUtil.deserializeChanges();
        ObservableList<Change> allChangesObservableList= FXCollections.observableArrayList(allChanges);

        changeTableView.setItems(allChangesObservableList);

        logger.info("ChangeLogController initialized");

    }

    public void dismissAll(){

        Platform.runLater(()->{

            List<Change>allChanges= FileUtil.deserializeChanges();
            ObservableList<Change> allChangesObservableList= FXCollections.observableArrayList(allChanges);

            allChangesObservableList.removeAll();
            changeTableView.refresh();
            try {
                FileUtil.clearFile();
            } catch (IOException ex) {
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    logger.error("Error clearing the change log",ex);
                    alert.setTitle("Error");
                    alert.setHeaderText("Failed to clear the change log");
                    alert.setContentText("Please restart your application");
                    alert.showAndWait();
                });
            }
            initialize();
        });

    }
}
