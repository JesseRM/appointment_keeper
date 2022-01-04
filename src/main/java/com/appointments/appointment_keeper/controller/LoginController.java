package com.appointments.appointment_keeper.controller;

import com.appointments.appointment_keeper.model.DBConnection;
import com.appointments.appointment_keeper.model.User;
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
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.PickResult;

/**
 * FXML Controller class
 *
 * @author Jesse
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
    private void loginUser(Event event) throws SQLException, IOException {
        if (!DBConnection.isValid()) DBConnection.connect();
        
        String query = "SELECT user_id FROM users WHERE name= ? AND password= ?";
        PreparedStatement statement = DBConnection.getCurrentConnection().prepareStatement(query);
        
        statement.setString(1, username.getText());
        statement.setString(2, userPassword.getText());
        
        ResultSet queryResult = statement.executeQuery();
        
        if (queryResult.next()) {
            Integer currentUserID = queryResult.getInt("user_id");
            
            User.setId(currentUserID);
            
            logUserLogin("SUCCEEDED", username.getText());
            
            displayHome(event);
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
    private void checkEnterKeyPress(KeyEvent event) throws SQLException, IOException {
        if (event.getCode() == KeyCode.ENTER) {
            loginUser(event);
        }
    }
}