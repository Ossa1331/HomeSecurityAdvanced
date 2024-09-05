package com.example.homesecurity.controller;

import com.example.homesecurity.entity.Event;
import com.example.homesecurity.service.EventService;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@Controller
public class EventLogController {

    @Autowired
    private EventService eventService;

    @FXML
    private TableView<Event> eventsTableView;

    @FXML
    private TableColumn<Event, String> eventDeviceNameTableColumn;
    @FXML
    private TableColumn<Event, String> eventDeviceTypeTableColumn;
    @FXML
    private TableColumn<Event, String> eventTimeAddedTableColumn;
    @FXML
    private TableColumn<Event, String> eventMessageTableColumn;

    List<Event> allEvents= new ArrayList<>();

    private static final Logger logger= LoggerFactory.getLogger(AllDevicesController.class);

    ObservableList<Event> allEventsObservableList;

    public void initialize(){
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        eventDeviceNameTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getDeviceName()));

        eventDeviceTypeTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getDeviceType()));

        eventTimeAddedTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getCurrentEventTriggered().format(formatter)));

        eventMessageTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getMessage()));

        allEvents= eventService.findAllEvents();

        allEventsObservableList= FXCollections.observableList(allEvents);

        eventsTableView.setItems(allEventsObservableList);

        logger.info("EventLogController initialized");
    }
    public void dismissAll(){
        allEventsObservableList.removeAll();
        eventService.deleteAllEvents();
        eventsTableView.refresh();

        logger.info("All events dismissed from database");
    }
}