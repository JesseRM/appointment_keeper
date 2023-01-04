package com.appointments.appointment_keeper.controller;

import com.appointments.appointment_keeper.db.DBConnection;
import com.appointments.appointment_keeper.db.DBSettings;
import com.appointments.appointment_keeper.model.User;
import com.appointments.appointment_keeper.util.Authenticate;
import com.appointments.appointment_keeper.util.Message;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.sql.ResultSet;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 */
public class LoginController implements Initializable {
    private String userLocation;
    
    @FXML
    private TextField username;
    @FXML
    private TextField userPassword;
    @FXML
    private Label errorMessage;
    @FXML
    private Label userLocationLabel;
    
    public LoginController() {
        userLocation = ZoneId.systemDefault().toString();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userLocationLabel.setText(userLocation);
        
        try {
            DBSettings.set();
            DBConnection.setCredentials(DBSettings.getAll());
            DBConnection.connect();
        } catch (IOException | SQLException e) {
            String message = "Error connecting to database. Would you like to delete settings file and "
                    + "re-enter your credentials after app restart?";
            
            Optional<ButtonType> userInput = Message.displayConfirmation(message);
        
            if (userInput.get() == ButtonType.OK) {
                if (DBSettings.deleteFile()) {
                    message = "Settings file deleted.";
                    Message.displayInformation(message);
                } else {
                    message = "Could not delete settings file.";
                    Message.displayError(message);
                }
            }
            
            message = "Application will now close.";
            Message.displayInformation(message);
            
            Platform.exit();
        }
    }    
    
    /** 
     * Clear username and password fields.
     */
    @FXML
    private void resetFields(MouseEvent event) {
        username.clear();
        userPassword.clear();
        errorMessage.setText("");
    }
    
    /** 
     * Query the DB for valid username and password. 
     * @param event Mouse click event
     */
    @FXML
    private void loginUser(Event event) throws SQLException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (!DBConnection.isValid()) DBConnection.connect();
        
        if (username.getText().trim().isEmpty() || userPassword.getText().isEmpty()) {
            displayLoginError();
            
            return;
        }
        
        if (Authenticate.user(username.getText().toLowerCase(), userPassword.getText())) {
            String query = "SELECT name, user_id FROM users WHERE name= ?";
            PreparedStatement statement = DBConnection.getCurrentConnection().prepareStatement(query);

            statement.setString(1, username.getText().toLowerCase());
            ResultSet queryResult = statement.executeQuery();
            
            if (queryResult.next()) {
                Integer currentUserID = queryResult.getInt("user_id");
                String name = queryResult.getString("name");
                
                User.setId(currentUserID);
                User.setUsername(name);

                logUserLogin("SUCCEEDED", username.getText());

                displayHome(event);
            } else {
                Message.displayError("Something went wrong, please try again.");
                
            }
        } else {
            logUserLogin("FAILED", username.getText());
            displayLoginError(); 
        }
        
    }
    
    /** 
     * Display error if invalid username or password. 
     */
    private void displayLoginError() {
        errorMessage.setText("Invalid Username or Password");
    }
    
    /** 
     * Display main menu view. 
     * @param event Mouse click event
     */
    private void displayHome(Event event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Home.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    /** 
     * Log user login attempts to external file. 
     * @param status Login succeeded or failed
     * @param userName Entered username
     */
    private void logUserLogin(String status, String userName) throws IOException {
        String entry = "Login " + status + " for username: " + userName + " on " + LocalDateTime.now();
        String filename = "login_activity.txt";
        
        FileWriter fWriter = new FileWriter(filename, true);
        PrintWriter outputFile = new PrintWriter(fWriter);
        outputFile.println(entry); 
        outputFile.close();
    }
    
    /**
     * If user has pressed 'Enter' key, attempt login.
     * @param event
     */
    @FXML
    private void checkEnterKeyPress(KeyEvent event) throws SQLException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (event.getCode() == KeyCode.ENTER) {
            loginUser(event);
        }
    }

    @FXML
    private void displayNewUser(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/NewUser.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
}