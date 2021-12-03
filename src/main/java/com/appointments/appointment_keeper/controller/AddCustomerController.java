package com.appointments.appointment_keeper.controller;

import com.appointments.appointment_keeper.util.DBConnection;
import com.appointments.appointment_keeper.util.Message;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 */
public class AddCustomerController implements Initializable {
    private ObservableList<String> countryOptions = FXCollections.observableArrayList();
    private ObservableList<String> provinceOptions = FXCollections.observableArrayList();
    private HashMap<String, Integer> countryIDs = new HashMap<>();
    private HashMap<String, Integer> provinceIDs = new HashMap<>();
    
    @FXML
    private TextField customerNameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private ComboBox<String> provinceComboBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            populateCountries();
        } catch (SQLException ex) {
            Logger.getLogger(AddCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }    
    
    /** 
     * Save new customer to DB.
     * @param event Mouse click event
     */
    @FXML
    private void saveNewCustomer(MouseEvent event) throws SQLException, IOException {
        if (isEmptyField()) return;
        
        String currentProvince = provinceComboBox.getSelectionModel().getSelectedItem();
        Integer province_id = provinceIDs.get(currentProvince);
        
        
        String query = "INSERT INTO customers (name, address, phone_number, postal_code, province_id) VALUES ("
                + "?, ?, ?, ?, ?)";
                
        PreparedStatement statement = DBConnection.getCurrentConnection().prepareStatement(query);
        statement.setString(1, customerNameField.getText());
        statement.setString(2, addressField.getText());
        statement.setString(3, phoneNumberField.getText());
        statement.setString(4, postalCodeField.getText());
        statement.setInt(5, province_id);
        
        statement.executeUpdate();
        
        exitToMainMenu(event);
    }
    
    /** 
     * User clicks on cancel button.
     * @param event Mouse click event
     */
    @FXML
    private void cancelAddCustomer(MouseEvent event) throws IOException {
        exitToMainMenu(event);
    }
    
    /** 
     * Populate province combobox.
     * @param event Action event
     */
    @FXML
    private void populateProvinces(ActionEvent event) throws SQLException {
        String selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();
        Integer selectedCountryID = countryIDs.get(selectedCountry);
        
        String query = "SELECT province_id, name FROM provinces WHERE country_id= ?";
        PreparedStatement statement = DBConnection.getCurrentConnection().prepareStatement(query);
        statement.setInt(1, selectedCountryID);
        
        ResultSet queryResult = statement.executeQuery();
        
        provinceOptions.clear();
        provinceComboBox.getItems().clear();
        
        while (queryResult.next()) {
            provinceOptions.add(queryResult.getString("name"));
            //cache division IDs
            provinceIDs.put(queryResult.getString("name"), queryResult.getInt("province_id"));
        }
        
        provinceComboBox.setItems(provinceOptions);
        
    }
    
    /** 
     * Populate countries combobox.
     */
    private void populateCountries() throws SQLException {
        String query = "SELECT country_id, name FROM countries;";
        PreparedStatement statement = DBConnection.getCurrentConnection().prepareStatement(query);
        
        ResultSet queryResult = statement.executeQuery();
        
        while (queryResult.next()) {
            countryOptions.add(queryResult.getString("name"));
            //cache country IDs
            countryIDs.put(queryResult.getString("name"), queryResult.getInt("country_id"));
        }
        
        countryComboBox.setItems(countryOptions);
    }
    
    /** 
     * Display main menu view.
     * @param event Mouse click event
     */
    private void exitToMainMenu(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Home.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    /** 
     * Check if there are empty fields.
     * @return True if there are empty fields
     */
    private boolean isEmptyField() {
        String name = customerNameField.getText().trim();
        String address = addressField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String postalCode = postalCodeField.getText().trim();
        String country = countryComboBox.getSelectionModel().getSelectedItem();
        String division = provinceComboBox.getSelectionModel().getSelectedItem();
        
        if (name.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || postalCode.isEmpty()) {
            Message.displayError("You must enter Name, Address, Phone Number and Postal Code values.");
            
            return true;
        }
        
        if (country == null || division == null) {
            Message.displayError("You must select a Country and Province.");
            
            return true;
        }
        
        return false;
    }
    
}
