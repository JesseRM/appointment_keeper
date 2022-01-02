package com.appointments.appointment_keeper.controller;

import com.appointments.appointment_keeper.model.User;
import com.appointments.appointment_keeper.model.Appointment;
import com.appointments.appointment_keeper.util.Message;
import com.appointments.appointment_keeper.model.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;   
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 */
public class HomeController implements Initializable {

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private HashMap<Integer, String> contactIdToName = new HashMap<>();
    private HashMap<Integer, String> customerIdToName = new HashMap<>();
    private HashMap<Integer, String> userIdToName = new HashMap<>();
    
    @FXML
    private ToggleGroup viewType;
    @FXML
    private RadioButton weekRadio;
    @FXML
    private RadioButton monthRadio;
    @FXML
    private RadioButton allRadio;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TableColumn<Appointment, Integer> appIdCol;
    @FXML
    private TableColumn<Appointment, String> statusCol;
    @FXML
    private TableColumn<Appointment, Integer> customerIdCol;
    @FXML
    private TableColumn<Appointment, String> titleCol;
    @FXML
    private TableColumn<Appointment, String> descriptionCol;
    @FXML
    private TableColumn<Appointment, String> locationCol;
    @FXML
    private TableColumn<Appointment, String> typeCol;
    @FXML
    private TableColumn<Appointment, String> dateCol;
    @FXML
    private TableColumn<Appointment, String> startCol;
    @FXML
    private TableColumn<Appointment, String> endCol;
    @FXML
    private TableView<Appointment> appointmentsTable;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datePicker.setValue(LocalDate.now());
        initializeAppointmentTableCols();
        
        try {
            populateAppointments();
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (!User.getLoggedIn()) {
            Appointment appIn15Min = appointmentWithin15Min(appointments);
        
            if (appIn15Min == null) {
                Message.displayInformation("You do not have any upcoming appointment.");
            } else {
                String message = "You have an upcoming appointment ID:" + appIn15Min.getId() + " Start: " + appIn15Min.getDate()
                                    + " " + appIn15Min.getStartTime();
                
                Message.displayConfirmation(message); 
            }
            
            User.setLoggedIn(true);
        }
    }    
    
    /** 
     * Log out the current user.
     * @param event Mouse click event.
     */
    @FXML
    private void logout(MouseEvent event) throws IOException {
        User.setLoggedIn(false);
        
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    /** 
     * Display the add customer view.
     * @param event Mouse click event.
     */
    @FXML
    private void addCustomer(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/AddCustomer.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    /** 
     * Display the modify customer view.
     * @param event Mouse click event.
     */
    @FXML
    private void modifyCustomer(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ModifyCustomer.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    /** 
     * Display the make appointment view.
     * @param event Mouse click event.
     */
    @FXML
    private void makeAppointment(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/AddAppointment.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    /** 
     * Display the modify appointment view.
     * @param event Mouse click event.
     */
    @FXML
    private void modifyAppointment(MouseEvent event) throws IOException {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        
        if (selectedAppointment == null) {
            Message.displayError("Please select an appointment to modify.");
            
            return;
        }
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ModifyAppointment.fxml"));
        ModifyAppointmentController modifyAppointmentController = new ModifyAppointmentController(selectedAppointment);
        
        loader.setController(modifyAppointmentController);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    /** 
     * Display the reports view.
     * @param event Mouse click event.
     */
    @FXML
    private void reports(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Reports.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    /** 
     * Populate the appointments table. 
     */
    private void populateAppointments() throws SQLException {
        if (!DBConnection.isValid()) DBConnection.connect();
        
        String query = "SELECT a.appointment_id, a.user_id, a.customer_id, a.title, a.description, a.type, a.location, a.status, " +
                        "a.start_time, a.end_time, u.name AS user_name, c.name AS customer_name " +
                        "FROM appointments AS a " +
                        "INNER JOIN users AS u " +
                        "ON a.user_id=u.user_id " +
                        "INNER JOIN customers AS c " +
                        "ON a.customer_id=c.customer_id";
        
        PreparedStatement statement = DBConnection.getCurrentConnection().prepareStatement(query);
        
        ResultSet queryResult = statement.executeQuery();
        
        while (queryResult.next()) {
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ZoneId userZoneId = ZoneId.systemDefault();
            ZoneId UtcZoneId = ZoneId.of("UTC");
            
            Integer appId = queryResult.getInt("appointment_id");
            Integer userId = queryResult.getInt("user_id");
            Integer customerId = queryResult.getInt("customer_id");
            String title = queryResult.getString("title");
            String description = queryResult.getString("description");
            String type = queryResult.getString("type");
            String location = queryResult.getString("location");
            String status = queryResult.getString("status");
            String customerName = queryResult.getString("customer_name");
            String userName = queryResult.getString("user_name");
            LocalDateTime startUTC = LocalDateTime.parse(queryResult.getString("start_time").substring(0, 19), dateTimeFormat);
            LocalDateTime endUTC = LocalDateTime.parse(queryResult.getString("end_time").substring(0, 19), dateTimeFormat);
            LocalDateTime startLocal = startUTC.atZone(UtcZoneId).withZoneSameInstant(userZoneId).toLocalDateTime();
            LocalDateTime endLocal = endUTC.atZone(UtcZoneId).withZoneSameInstant(userZoneId).toLocalDateTime();
            
            Appointment appointment = new Appointment(appId, userId, customerId, title, description, type, 
                    location, status, customerName, userName, startLocal, endLocal);
            
            appointments.add(appointment);
        }
        
        appointmentsTable.setItems(appointments);
    }
    
    /** 
     * Initialize appointment table columns for use with Appointment model. 
     */
    private void initializeAppointmentTableCols() {
        appIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    }
    
    /** 
     * Filter appointments displayed in table by week. 
     */
    @FXML
    private void filterAppointmentsByWeek(ActionEvent event) {
        final Integer dayInWeek = 7;
        
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate selectedDate = LocalDate.parse(datePicker.getValue().toString(), dateFormat);
        Integer dayOfWeek = selectedDate.getDayOfWeek().getValue();
        LocalDate startDate = selectedDate.minusDays(dayOfWeek);
        LocalDate endDate = selectedDate.plusDays(dayInWeek - dayOfWeek);
        
        FilteredList<Appointment> filteredApps = new FilteredList<>(appointments);
        
        filteredApps.setPredicate(appointment -> {
            LocalDate appointmentDate = LocalDate.parse(appointment.getDate(), dateFormat);
            
            return appointmentDate.isAfter(startDate) && appointmentDate.isBefore(endDate);
        });
        
        appointmentsTable.setItems(filteredApps);
    }
    
    /** 
     * Filter appointments displayed in table by month.  
     */
    @FXML
    private void filterAppointmentsByMonth(ActionEvent event) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate selectedDate = LocalDate.parse(datePicker.getValue().toString(), dateFormat);
        Integer year = selectedDate.getYear();
        Integer monthInt = selectedDate.getMonthValue();
        String monthStr = monthInt < 10 ? ("0" + monthInt):("" + monthInt);
        Integer monthLastDay = selectedDate.lengthOfMonth();
        String startOfMonth = year + "-" + monthStr + "-01";
        String endOfMonth = year + "-" + monthStr + "-" + monthLastDay;
        LocalDate startDate = LocalDate.parse(startOfMonth, dateFormat);
        LocalDate endDate = LocalDate.parse(endOfMonth, dateFormat).plusDays(1);
        
        FilteredList<Appointment> filteredApps = new FilteredList<>(appointments);
        
        filteredApps.setPredicate(appointment -> {
            LocalDate appointmentDate = LocalDate.parse(appointment.getDate(), dateFormat);
            
            return appointmentDate.isAfter(startDate.minusDays(1)) && appointmentDate.isBefore(endDate);
        });
        
        appointmentsTable.setItems(filteredApps);
    }
    
    /** 
     * Filter appointments displayed in table by all. 
     */
    @FXML
    private void displayAllAppointments(ActionEvent event) {
        appointmentsTable.setItems(appointments);
    }
    
    /** 
     * Display appointments based on selected filter. 
     */
    @FXML
    private void displayAppointments(ActionEvent event) {
        if (weekRadio.isSelected()) {
            filterAppointmentsByWeek(event);
        }
        
        if (monthRadio.isSelected()) {
            filterAppointmentsByMonth(event);
        }
        
        if (allRadio.isSelected()) {
            displayAllAppointments(event);
        }
    }
    
    /** 
     * Check if the logged in user has an appointment within 15 min.
     */
    @FXML
    private Appointment appointmentWithin15Min(ObservableList<Appointment> appointments) {
        LocalDateTime now = LocalDateTime.now().minusMinutes(1);
        LocalDateTime nowPlus15 = now.plusMinutes(16);
        
        for (Appointment appointment : appointments) {
            if (appointment.getUserId().equals(User.getId()) && appointment.getStart().isAfter(now) && appointment.getStart().isBefore(nowPlus15)) {
                    return appointment;
            }
        }
            
        return null;   
    } 
    
}
