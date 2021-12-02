module com.appointments.appointment_keeper {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;

    opens com.appointments.appointment_keeper to javafx.fxml;
    opens com.appointments.appointment_keeper.controller to javafx.fxml;
    opens com.appointments.appointment_keeper.model to javafx.fxml;
    exports com.appointments.appointment_keeper;
    exports com.appointments.appointment_keeper.controller;
    exports com.appointments.appointment_keeper.model;
}
