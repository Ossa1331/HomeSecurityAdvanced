package com.example.homesecurity.util;

import com.example.homesecurity.entity.*;
import com.example.homesecurity.controller.ChooseLocationController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private static final String CHANGE_LOG_SERIALIZATION_FILE_NAME = "dat/serialized-log.ser";

    public static void serializeChange(Change change) {
        File file = new File(CHANGE_LOG_SERIALIZATION_FILE_NAME);
        boolean append = file.exists() && file.length() > 0;

        try (FileOutputStream fileOut = new FileOutputStream(file, true);
             ObjectOutputStream out = append ? new AppendableObjectOutputStream(fileOut) : new ObjectOutputStream(fileOut)) {
            out.writeObject(change);
            out.flush();
        } catch (Exception ex) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                logger.error("Error writing in change log",ex);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to write in the change log");
                alert.setContentText("Please restart your application");
                alert.showAndWait();
            });
        }
    }

    public static List<Change> deserializeChanges() {
        List<Change> changes = new ArrayList<>();
        Long currentLocationId = ChooseLocationController.currentLocation.getId();

        try (FileInputStream fileIn = new FileInputStream(CHANGE_LOG_SERIALIZATION_FILE_NAME);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            while (true) {
                try {
                    Change change = (Change) in.readObject();
                    if (Objects.equals(change.getCurrentLocationChange(), currentLocationId)) {
                        changes.add(change);
                    }
                }
                catch (EOFException ex) {
                    logger.info("End of file reached");
                    break;
                }
                catch (StreamCorruptedException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    logger.error("Error reading from change log", ex);
                    alert.setTitle("Error");
                    alert.setHeaderText("Failed to read from the change log");
                    alert.setContentText("Please restart your application");
                    alert.showAndWait();
                    break;
                }
            }
        } catch (Exception ex) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                logger.error("Error writing in change log",ex);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to write in the change log");
                alert.setContentText("Please restart your application");
                alert.showAndWait();
            });

        }

        return changes;
    }


    public static void clearFile() throws IOException {
        Long currentLocationId = ChooseLocationController.currentLocation.getId();
        List<Change> remainingChanges = new ArrayList<>();

        try (FileInputStream fileIn = new FileInputStream(CHANGE_LOG_SERIALIZATION_FILE_NAME);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            while (true) {
                try {
                    Change change = (Change) in.readObject();
                    if (!Objects.equals(change.getCurrentLocationChange(), currentLocationId)) {
                        remainingChanges.add(change);
                    }
                } catch (EOFException e) {
                    logger.info("End of file reached");
                    break;
                } catch (IOException | ClassNotFoundException ex) {
                    logger.error("Unable to clear file", ex);
                    break;
                }
            }
        }
        try (FileOutputStream fileOut = new FileOutputStream(CHANGE_LOG_SERIALIZATION_FILE_NAME);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            for (Change change : remainingChanges) {
                out.writeObject(change);
            }
        }
    }
}
