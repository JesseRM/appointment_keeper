package com.appointments.appointment_keeper.controller;

import com.appointments.appointment_keeper.db.DBConnection;
import com.appointments.appointment_keeper.db.DBSettings;
import com.appointments.appointment_keeper.util.Message;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DBSettingsController implements Initializable {

    @FXML
    private TextField user;
    @FXML
    private TextField url;
    @FXML
    private TextField password;
    @FXML
    private Button cancelBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       if (DBConnection.getCurrentConnection() == null) {
           cancelBtn.setDisable(true);
       }
    }    

    @FXML
    private void cancelDBSettings(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void saveDBSettings(MouseEvent event) throws IOException {
        if (isValid()) {
            DBSettings.updateFile(user.getText(), password.getText(), url.getText());
            exitToLoginScreen(event);
        } else {
            Message.displayError("Please enter a value for all fields.");
        }
    }
    
    private boolean isValid() {
        boolean userIsEmpty = user.getText().trim().isEmpty();
        boolean passwordIsEmpty = password.getText().trim().isEmpty();
        boolean urlIsEmpty = url.getText().trim().isEmpty();
        
        return !(userIsEmpty || passwordIsEmpty || urlIsEmpty);
    }
    
    private void exitToLoginScreen(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
}
