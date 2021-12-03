package com.appointments.appointment_keeper.controller;

import com.appointments.appointment_keeper.model.Customer;
import com.appointments.appointment_keeper.util.Message;
import com.appointments.appointment_keeper.util.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 */
public class ModifyCustomerController implements Initializable {

    private ObservableList<String> countryOptions = FXCollections.observableArrayList();
    private ObservableList<String> provinceOptions = FXCollections.observableArrayList();
    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    private HashMap<Integer, String> countryNames = new HashMap<>();
    private HashMap<Integer, String> provinceNames = new HashMap<>();
    private HashMap<String, Integer> countryIDs = new HashMap<>();
    private HashMap<String, Integer> provinceIDs = new HashMap<>();
    private HashMap<Integer, Integer> provinceToCountryId = new HashMap<>();
    
    @FXML
    private TextField customerIdField;
    @FXML
    private TextField nameField;
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
    @FXML
    private TableColumn<Customer, Integer> customerIDCol;
    @FXML
    private TableColumn<Customer, String> nameCol;
    @FXML
    private TableColumn<Customer, String> addressCol;
    @FXML
    private TableColumn<Customer, String> phoneCol;
    @FXML
    private TableColumn<Customer, String> postalCodeCol;
    @FXML
    private TableColumn<Customer, String> countryCol;
    @FXML
    private TableColumn<Customer, String> provinceCol;
    @FXML
    private TableView<Customer> customersTable;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeCustomerTableCols();
        
        try {
            cacheCountryInfo();
            cacheProvinceInfo();
            populateCustomerTable();
        } catch (SQLException ex) {
            Logger.getLogger(ModifyCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    /** 
     * Update customer in the DB.
     * @param event Mouse click event.
     */
    @FXML
    private void saveModification(MouseEvent event) throws SQLException, IOException {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        
        if (selectedCustomer == null) {
            Message.displayError("Please select a customer.");
            
            return;
        }
        
        if (isEmptyField()) return;
        
        Integer customerId = selectedCustomer.getId();
        String customerName = nameField.getText();
        String address = addressField.getText();
        String phoneNumber = phoneNumberField.getText();
        String postalCode = postalCodeField.getText();
        Integer provinceId = provinceIDs.get(provinceComboBox.getSelectionModel().getSelectedItem());
        
        String query = "UPDATE customers SET "
                + "name= ?,"
                + "address= ?, "
                + "phone_number= ?, "
                + "postal_code= ?, "
                + "province_id= ?"
                + "WHERE customer_id= ?";
        
        PreparedStatement statement = DBConnection.getCurrentConnection().prepareStatement(query);
        
        statement.setString(1, customerName);
        statement.setString(2, address);
        statement.setString(3, phoneNumber);
        statement.setString(4, postalCode);
        statement.setInt(5, provinceId);
        statement.setInt(6, customerId);
        
        statement.executeUpdate();
        
        exitToMainMenu(event);
    }
    
    /** 
     * User clicks on cancel button.
     * @param event Mouse click event
     */
    @FXML
    private void cancelModifyCustomer(MouseEvent event) throws IOException {
        exitToMainMenu(event);
    }
    
    /** 
     * Delete user from the DB. 
     * @param event Mouse click event.
     */
    @FXML
    private void deleteCustomer(MouseEvent event) throws SQLException {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        
        if (selectedCustomer == null) return;
        
        boolean deleteConfirmed = confirmCustomerDelete(selectedCustomer.getId());
        
        if (!deleteConfirmed) return;
        
        String query = "DELETE FROM customers WHERE customer_id= ?";
        PreparedStatement statement = DBConnection.getCurrentConnection().prepareStatement(query);
        statement.setInt(1, selectedCustomer.getId());
        
        statement.executeUpdate();
        
        populateCustomerTable();
        
        customerDeletedMessage(selectedCustomer.getId());
    }
    
    /** 
     * Populate the customer table. 
     */
    private void populateCustomerTable() throws SQLException {
        String query = "SELECT customer_id, name, address, postal_code, phone_number, province_id FROM customers";
        PreparedStatement statement = DBConnection.getCurrentConnection().prepareStatement(query);
        ResultSet queryResult = statement.executeQuery();
        
        customers.clear();
        
        while (queryResult.next()) {
            Integer customerId = queryResult.getInt("customer_id");
            String customerName = queryResult.getString("name");
            String address = queryResult.getString("address");
            String postalCode = queryResult.getString("postal_code");
            String phone = queryResult.getString("phone_number");
            Integer provinceId = queryResult.getInt("province_id");
            String provinceName = provinceNames.get(provinceId);
            String countryName = countryNames.get(provinceToCountryId.get(provinceId));
            
            Customer customer = new Customer(customerId, customerName, address, phone, postalCode, provinceId,
            provinceName, countryName);
            
            customers.add(customer);
        }
        
        customersTable.setItems(customers);
    }
    
    /** 
     * Cache country info to avoid DB queries. 
     */
    private void cacheCountryInfo() throws SQLException {
        String query = "SELECT country_id, name FROM countries";
        PreparedStatement statement = DBConnection.getCurrentConnection().prepareStatement(query);
        
        ResultSet queryResult = statement.executeQuery();
        
        while (queryResult.next()) {
            Integer countryId = queryResult.getInt("country_id");
            String countryName = queryResult.getString("name");
            
            countryIDs.put(countryName, countryId);
            countryNames.put(countryId, countryName);
            countryOptions.add(countryName);
        }
    }
    
    /** 
     * Cache province info to avoid DB queries. 
     */
    private void cacheProvinceInfo() throws SQLException {
        String query = "SELECT province_id, name, country_id FROM provinces";
        PreparedStatement statement = DBConnection.getCurrentConnection().prepareStatement(query);
        ResultSet queryResult = statement.executeQuery();
        
        while (queryResult.next()) {
            Integer provinceId = queryResult.getInt("province_id");
            Integer countryId = queryResult.getInt("country_id");
            String provinceName = queryResult.getString("name");
            
            provinceIDs.put(provinceName, provinceId);
            provinceNames.put(provinceId, provinceName);
            provinceToCountryId.put(provinceId, countryId);
            provinceOptions.add(provinceName);
        }
    }
    
    /** 
     * Initialize customer table columns to use with Customer model. 
     */
    private void initializeCustomerTableCols() {
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("provinceName"));
    }
    
    /** 
     * Populate customer fields based on selected customer. 
     * @param event Mouse click event.
     */
    @FXML
    private void populateCustomerFields(MouseEvent event) {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        
        if (selectedCustomer == null) return;
        
        customerIdField.setText(String.valueOf(selectedCustomer.getId()));
        nameField.setText(selectedCustomer.getName());
        addressField.setText(selectedCustomer.getAddress());
        phoneNumberField.setText(selectedCustomer.getPhoneNumber());
        postalCodeField.setText(selectedCustomer.getPostalCode());
        countryComboBox.setItems(countryOptions);
        provinceComboBox.setItems(provinceOptions);
        countryComboBox.getSelectionModel().select(selectedCustomer.getCountryName());
        provinceComboBox.getSelectionModel().select(selectedCustomer.getProvinceName());
    }
    
    /** 
     * Update division option based on selected country. 
     * @param event User interaction with country combobox.
     */
    @FXML
    private void updateProvinceOptions(ActionEvent event) {
        String selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();
        Integer selectedCountryId = countryIDs.get(selectedCountry);
        
        provinceOptions.clear();
        
        provinceToCountryId.forEach((key, value) -> {
            if (selectedCountryId.equals(value)) {
                provinceOptions.add(provinceNames.get(key));
            }
        });
    }
    
    /** 
     * Open confirmation window for user deletion. 
     * @param customerId The selected customer ID.
     */
    private boolean confirmCustomerDelete(Integer customerId) {
        String message = "Delete customer with ID: " + customerId + "?";
        
        Optional<ButtonType> result = Message.displayConfirmation(message);
        
        return result.get() == ButtonType.OK;
    }
    
    /** 
     * Open window confirming selected user has been deleted from DB. 
     * @param customerId The deleted customer ID.
     */
    private void customerDeletedMessage(Integer customerId) {
        String message = "Customer with ID: " + customerId + " has been deleted.";
        
        Message.displayInformation(message);
    }
    
    /** 
     * Check if there are empty fields.
     * @return True if there are empty fields.
     */
    private boolean isEmptyField() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String postalCode = postalCodeField.getText().trim();
        String country = countryComboBox.getSelectionModel().getSelectedItem();
        String province = provinceComboBox.getSelectionModel().getSelectedItem();
        
        if (name.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || postalCode.isEmpty()) {
            Message.displayError("You must enter Name, Address, Phone Number and Postal Code values.");
            
            return true;
        }
        
        if (country == null || province == null) {
            Message.displayError("You must select a Country and Province.");
            
            return true;
        }
        
        return false;
    }
    
    private void exitToMainMenu(MouseEvent event) throws IOException {
       Parent root = FXMLLoader.load(getClass().getResource("/fxml/Home.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show(); 
    }   
    
}
