package com.appointments.appointment_keeper.util;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Message {
    public static void displayError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        
        alert.showAndWait();
    }
    
    /** 
     * Display a confirmation window.
     * @param message Text to display in the window.
     * @return The button the user clicked, OK or CANCEL.
     */
    public static Optional<ButtonType> displayConfirmation(String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText(message);
        
        Optional<ButtonType> result = alert.showAndWait();
        
        return result; 
    }
    
    /** 
     * Display and information window.
     * @param message Text to display in the window. 
     */
    public static void displayInformation(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(message);
        
        alert.showAndWait();
    }
}
