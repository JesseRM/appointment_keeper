package com.appointments.appointment_keeper.controller;

import com.appointments.appointment_keeper.model.DBConnection;
import com.appointments.appointment_keeper.util.Authenticate;
import com.appointments.appointment_keeper.util.Message;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 */
public class NewUserController implements Initializable {

    @FXML
    private PasswordField password1;
    @FXML
    private TextField password2;
    @FXML
    private TextField username;
    @FXML
    private PasswordField confirmPass1;
    @FXML
    private TextField confirmPass2;
    @FXML
    private CheckBox clearTextCheckBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       password1.textProperty().bindBidirectional(password2.textProperty());
       confirmPass1.textProperty().bindBidirectional(confirmPass2.textProperty());
    }    
    
    /** 
     * Display the Login screen. 
     * @param event Mouse click event
     */
    @FXML
    private void displayLogin(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    /** 
     * Create a new user in the database. 
     * @param event Mouse click event
     */
    @FXML
    private void createNewUser(MouseEvent event) throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException, IOException {
        if (usernameIsValid() && passwordIsValid()) {
            String password = clearTextCheckBox.isSelected() ? password2.getText() : password1.getText();
            String salt = Authenticate.createSalt();
            String hash = Authenticate.hash(password, salt);
            
            String query = "INSERT INTO users (name, password_hash, salt) VALUES (?, ?, ?)";
            PreparedStatement statement = DBConnection.getCurrentConnection().prepareStatement(query);

            statement.setString(1, username.getText().toLowerCase());
            statement.setString(2, hash);
            statement.setString(3, salt);
            
            statement.executeUpdate();
            
            displayLogin(event);
        }
    }
    
    /** 
     * Check if passwords should be displayed in clear text. 
     * @param event Action event
     */
    @FXML
    private void showClearTextPass(ActionEvent event) {
        if (clearTextCheckBox.isSelected()) {
            password2.toFront();
            confirmPass2.toFront();
        } else {
            password1.toFront();
            confirmPass1.toFront();
        }
    }
    
    /** 
     * Check if username meets constraints. 
     */
    private boolean usernameIsValid() throws SQLException {
        String name = username.getText();
        
        if (name.isEmpty() || !name.matches("[a-zA-Z0-9]*")) {
            Message.displayError("Username can only contain letters and numbers.");
            return false;
        }
        
        String query = "SELECT name FROM users WHERE name= ?";
        PreparedStatement statement = DBConnection.getCurrentConnection().prepareStatement(query);

        statement.setString(1, name.toLowerCase());
        ResultSet queryResult = statement.executeQuery();
        
        if (queryResult.next()) {
            Message.displayError("Username already exists, please try a different one.");
            return false;
        }
        
        return true;
    }
    
    /** 
     * Check if password meets constraints. 
     */
    private boolean passwordIsValid() {
        String password = clearTextCheckBox.isSelected() ? password2.getText() : password1.getText();
        String confirmPass = clearTextCheckBox.isSelected() ? confirmPass2.getText() : confirmPass1.getText();
        
        if (password.isEmpty() || confirmPass.isEmpty()) {
            Message.displayError("Please enter and confirm your password.");
            return false;
        }
        
        if (!password.equals(confirmPass)) {
            Message.displayError("Passwords do not match, please try again.");
            return false;
        }
        
        return true;
    }
    
}
