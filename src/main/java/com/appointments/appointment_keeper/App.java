package com.appointments.appointment_keeper;

import com.appointments.appointment_keeper.db.DBConnection;
import com.appointments.appointment_keeper.db.DBSettings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        String resource = "/fxml/Login.fxml";
        
        if (!DBSettings.propsFileExists()) {
            resource = "/fxml/DBSettings.fxml";
        }
        
        Parent root = FXMLLoader.load(getClass().getResource(resource));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
        DBConnection.closeConnection();
    }

}