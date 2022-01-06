package com.appointments.appointment_keeper.controller;

import com.appointments.appointment_keeper.util.Message;
import com.appointments.appointment_keeper.model.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;


/**
 * FXML Controller class
 */
public class AddAppointmentController implements Initializable {

    private ObservableList<String> userOptions = FXCollections.observableArrayList();
    private HashMap<String, Integer> userOptionToId = new HashMap<>();
    private ObservableList<String> customerOptions = FXCollections.observableArrayList();
    private HashMap<String, Integer> customerOptionToId = new HashMap<>();
    private ObservableList<String> statusOptions = FXCollections.observableArrayList();
    private ObservableList<String> hourOptions = FXCollections.observableArrayList();
    private ObservableList<String> minOptions = FXCollections.observableArrayList();
    private ObservableList<String> periodOptions = FXCollections.observableArrayList();
    
    private ZoneId currentZoneId = ZoneId.systemDefault();
    private LocalDateTime startTimeLocal;
    private LocalDateTime endTimeLocal;
    private LocalDateTime startTimeUTC;
    private LocalDateTime endTimeUTC;
    
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField titleField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField locationField;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private ComboBox<String> startTimeHr;
    @FXML
    private ComboBox<String> startTimeMin;
    @FXML
    private ComboBox<String> startTimePeriod;
    @FXML
    private ComboBox<String> endTimeHr;
    @FXML
    private ComboBox<String> endTimeMin;
    @FXML
    private ComboBox<String> endTimePeriod;
    @FXML
    private ComboBox<String> userComboBox;
    @FXML
    private ComboBox<String> customerComboBox;
    @FXML
    private TextField typeField;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            populateUsers();
            populateCustomers();
            populateStatuses();
        } catch (SQLException ex) {
            Logger.getLogger(AddAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        setDatePicker();
        populateHoursAndMins();
    }    
    
    /** 
     * Populates the user combobox. 
     */
    private void populateUsers() throws SQLException {
        String query = "SELECT user_id, name FROM users;";
        PreparedStatement statement = DBConnection.getCurrentConnection().prepareStatement(query);
        ResultSet queryResult = statement.executeQuery();
        
        while (queryResult.next()) {
            Integer userId = queryResult.getInt("user_id");
            String userName = queryResult.getString("name");
            String option = userName + " (ID: " + userId + ")";
            
            userOptions.add(option);
            userOptionToId.put(option, userId);
        }
        
        userComboBox.setItems(userOptions);
    }
    
    /** 
     * Populates the customers combobox. 
     */
    private void populateCustomers() throws SQLException {
        String query = "SELECT customer_id, name FROM customers;";
        PreparedStatement statement = DBConnection.getCurrentConnection().prepareStatement(query);
        ResultSet queryResult = statement.executeQuery();
        
        while (queryResult.next()) {
            Integer customerId = queryResult.getInt("customer_id");
            String customerName = queryResult.getString("name");
            String option = customerName + " (ID: " + customerId + ")";
            
            customerOptions.add(option);
            customerOptionToId.put(option, customerId);
        }
        
        customerComboBox.setItems(customerOptions);
    }
    
    /** 
     * Populates the contacts combobox. 
     */
    private void populateStatuses() throws SQLException {
        statusOptions.addAll("PENDING", "COMPLETE", "NO SHOW");
        
        statusComboBox.setItems(statusOptions);
    }
    
    /** 
     * Sets the date picker to the current date.
     */
    private void setDatePicker() {
        LocalDate currentDate = LocalDate.now();
        
        datePicker.setValue(currentDate);
        
        datePicker.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isBefore(currentDate));
            }
        });
    }
    
    /** 
     * Populates the available hours and minutes comboboxes. 
     */
    private void populateHoursAndMins() {
        hourOptions.addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        minOptions.addAll("00", "15", "30", "45");
        periodOptions.addAll("AM", "PM");
        
        startTimeHr.setItems(hourOptions);
        startTimeMin.setItems(minOptions);
        startTimePeriod.setItems(periodOptions);
        
        endTimeHr.setItems(hourOptions);
        endTimeMin.setItems(minOptions);
        endTimePeriod.setItems(periodOptions);
    }
    
    /** Saves a new appointment into the DB 
     * @param event Mouse click event.
     */
    @FXML
    private void saveAppointment(MouseEvent event) throws SQLException, IOException {
        if (isEmptyField()) return;
        
        setEndAndStartTimes();
        
        if (!validTimes()) return;
        
        if (!DBConnection.isValid()) DBConnection.connect();
        
        String query = "INSERT INTO appointments (title, description, location, type, start_time, end_time, customer_id, user_id, status)"
                        + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = DBConnection.getCurrentConnection().prepareStatement(query);
         
        statement.setString(1, titleField.getText());
        statement.setString(2, descriptionField.getText());
        statement.setString(3, locationField.getText());
        statement.setString(4, typeField.getText());
        statement.setObject(5, startTimeUTC);
        statement.setObject(6, endTimeUTC);
        statement.setInt(7, customerOptionToId.get(customerComboBox.getSelectionModel().getSelectedItem()));
        statement.setInt(8, userOptionToId.get(userComboBox.getSelectionModel().getSelectedItem()));
        statement.setString(9, statusComboBox.getSelectionModel().getSelectedItem());
        
        statement.executeUpdate();
        
        exitToMainMenu(event);
                
    }
    
    /** Check to see if there are any empty fields. 
     * @return False if no empty fields
     */
    private boolean isEmptyField() {
        String user = userComboBox.getSelectionModel().getSelectedItem();
        String customer = customerComboBox.getSelectionModel().getSelectedItem();
        String contact = statusComboBox.getSelectionModel().getSelectedItem();
        String startHr = startTimeHr.getSelectionModel().getSelectedItem();
        String startMin = startTimeMin.getSelectionModel().getSelectedItem();
        String startPeriod = startTimePeriod.getSelectionModel().getSelectedItem();
        String endHr = endTimeHr.getSelectionModel().getSelectedItem();
        String endMin = endTimeMin.getSelectionModel().getSelectedItem();
        String endPeriod = endTimePeriod.getSelectionModel().getSelectedItem();
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        String type = typeField.getText().trim();
        String location = locationField.getText().trim();
        LocalDate date = datePicker.getValue();
        
        if (user == null || customer == null || contact == null) {
            Message.displayError("Please select User, Customer and Status.");
            
            return true;
        }
        
        if (title.isEmpty() || description.isEmpty() || type.isEmpty() || location.isEmpty()) {
            Message.displayError("Please enter values for Title, Description, Type and Location fields.");
            
            return true;
        }
        
        if (date == null) {
            Message.displayError("Please selecte a date.");
            
            return true;
        }
        
        if (startHr == null || startMin == null || startPeriod == null || endHr == null || endMin == null || endPeriod == null) {
            Message.displayError("Please select values for all time fields");
            
            return true;
        }
        
        return false;
    }
    
    /** 
     * Format the user selected date and time to be saved into the DB. 
     */
    private void setEndAndStartTimes() {
        LocalDate selectedDate = datePicker.getValue();
        String selectedStartHr = startTimeHr.getValue();
        String selectedStartMin = startTimeMin.getValue();
        String selectedStartPeriod = startTimePeriod.getValue();
        String selectedEndHr = endTimeHr.getValue();
        String selectedEndMin = endTimeMin.getValue();
        String selectedEndPeriod = endTimePeriod.getValue();
        
        String completeStartTime = selectedDate + " " + selectedStartHr + ":" + selectedStartMin + " " + selectedStartPeriod;
        String completeEndTime = selectedDate + " " + selectedEndHr + ":" + selectedEndMin + " " + selectedEndPeriod;
        
        startTimeLocal = LocalDateTime.parse(completeStartTime, DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));
        endTimeLocal = LocalDateTime.parse(completeEndTime, DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));
        
        startTimeUTC = startTimeLocal.atZone(currentZoneId).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        endTimeUTC = endTimeLocal.atZone(currentZoneId).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
    }
    
    /** Ensure user selected end and start times are valid. 
     * @return True if times are valid
     */
    private boolean validTimes() throws SQLException {
        if (sameStartAndEndTime()) {
            Message.displayError("Start and end times cannot be the same.");
            
            return false;
        }
        
        if (endBeforeStartTime()) {
            Message.displayError("End time cannot be before start time.");
            
            return false;
        }
        
        if (outsideBusinessHours()) {
            Message.displayError("Times must be between 8:00 AM and 10:00 PM EST.");
            
            return false;
        }
        
        if (conflictingAppointment()) {
            Message.displayError("The selected times conflict with another appointment.");
            
            return false;
        }
        
        return true;
    }
    
    /** Check to see if user selected end and start time are within business hours. 
     * @return False if outside business hours
     */
    private boolean outsideBusinessHours() {
        LocalTime startBizZone = startTimeLocal
                                    .atZone(currentZoneId)
                                    .withZoneSameInstant(BusinessHours.getBIZ_HR_ZONE_ID())
                                    .toLocalTime();
        LocalTime endBizZone = endTimeLocal
                                    .atZone(currentZoneId)
                                    .withZoneSameInstant(BusinessHours.getBIZ_HR_ZONE_ID())
                                    .toLocalTime();
        
        if (startBizZone.isBefore(BusinessHours.getOpen()) || 
                startBizZone.isAfter(BusinessHours.getClose().minusMinutes(1))) return true;
        if (endBizZone.isBefore(BusinessHours.getOpen()) || 
                endBizZone.isAfter(BusinessHours.getClose())) return true;
        
        return false;
    }
    
    /** Check the DB for conflicting appointment. 
     * @return False if a conflicting appointment is found
     */
    private boolean conflictingAppointment() throws SQLException {
        Integer customerId = customerOptionToId.get(customerComboBox.getSelectionModel().getSelectedItem());
        
        String query = "SELECT * FROM appointments WHERE (" 
                + "? >= start_time AND ? < end_time "
		+ "OR ? > start_time AND ? <= end_time "
		+ "OR ? < start_time AND ? > end_time)"
                + " AND customer_id= ?";
        
        PreparedStatement statement = DBConnection.getCurrentConnection().prepareStatement(query);
        statement.setObject(1, startTimeUTC);
        statement.setObject(2, startTimeUTC);
        statement.setObject(3, endTimeUTC);
        statement.setObject(4, endTimeUTC);
        statement.setObject(5, startTimeUTC);
        statement.setObject(6, endTimeUTC);
        statement.setInt(7, customerId);
        
        ResultSet queryResult = statement.executeQuery();
        
        //If there is a row in the set then there is a conflict
        if (queryResult.next()) {
            return true;
        }
        
        return false;
    }
    
    /** Check if user selected start and end times are the same. 
     * @return True if start and end times are the same
     */
    private boolean sameStartAndEndTime() {
        return startTimeLocal.equals(endTimeLocal);
    }
    
    /** Check if end is before start time 
     * @return True if end is before start time
     */
    private boolean endBeforeStartTime() {
        return endTimeLocal.isBefore(startTimeLocal);
    }
    
    /** User clicks on cancel button.
     * @param event Mouse click event
     */
    @FXML
    private void cancelAddAppointment(MouseEvent event) throws IOException {
        exitToMainMenu(event);
    }
    
    /** Exit to main menu view. 
     * @param event Mouse click event
     */
    public void exitToMainMenu(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Home.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
}
