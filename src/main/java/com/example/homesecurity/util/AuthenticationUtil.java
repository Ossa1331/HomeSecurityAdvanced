package com.example.homesecurity.util;

import com.example.homesecurity.entity.User;
import com.example.homesecurity.exception.UserAlreadyExistsException;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationUtil {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationUtil.class);
    private static final String FILE_PATH = "dat/user_credentials.txt";

    public static User currentUser;

    private static Map<String, User> userCredentials=loadCredentialsFromFile();

    public AuthenticationUtil() {

    }

    public static Map<String, User> loadCredentialsFromFile() {
        Map<String, User> credentials = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                Integer id=Integer.parseInt(parts[0]);
                String username = parts[1];
                boolean isAdmin = Boolean.parseBoolean(parts[2]);
                String hashedPassword = parts[3];

                credentials.put(username, new User(id,username, hashedPassword, isAdmin));
            }
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Unable to load credentials from the file");
            alert.setContentText("Please delete the file and restart the application");
            logger.error("Credentials file corrupted", ex);
            alert.showAndWait();
        }
        return credentials;
    }

    private static void saveCredentialsToFile(User newUser){

        try (FileWriter writer = new FileWriter(FILE_PATH,true)) {
               writer.write(newUser.getId()+ ":" + newUser.getUsername() + ":" + newUser.getAdministrator() + ":" + newUser.getPassword() + "\n");
        }
        catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Unable to save credentials to the file");
            alert.setContentText("Please delete the file and restart the application");
            logger.error("Unable to save credentials to file", ex);
            alert.showAndWait();
        }
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashedBytes) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Unable to hash password");
            alert.setContentText("Please reenter your password");
            logger.error("Non-existent algorithm", ex);
            alert.showAndWait();
            return null;
        }
    }

    public static boolean authenticate(String username, String password) {
        userCredentials = loadCredentialsFromFile();
        String hashedPassword = hashPassword(password);
        User user = userCredentials.get(username);
        currentUser=user;

        return user != null && hashedPassword != null && hashedPassword.equals(user.getPassword());
    }

    public static void addUser(String username, String password, boolean isAdmin) throws UserAlreadyExistsException{

        Integer id=0;

        Map<String, User> credentials=loadCredentialsFromFile();

        if(!credentials.containsKey(username)) {

            if (userCredentials == null) {
                userCredentials = new HashMap<>();
            }
            if (!userCredentials.isEmpty()) {
                for (String key : userCredentials.keySet()) {
                    User user = userCredentials.get(key);
                    if (id < user.getId()) {
                        id = user.getId();
                    }
                }
                id++;
            }
            String hashedPassword = hashPassword(password);
            User newUser = new User(id, username, hashedPassword, isAdmin);

            if (userCredentials.containsKey(username)) {
                userCredentials.replace(username, newUser);
            } else {
                userCredentials.put(username, newUser);
            }

            saveCredentialsToFile(newUser);
        }
        else{
            throw new UserAlreadyExistsException("User already exists");
        }
    }

    public static boolean isAdmin(String username) {
        User user = userCredentials.get(username);
        return user != null && user.getAdministrator();
    }
}
