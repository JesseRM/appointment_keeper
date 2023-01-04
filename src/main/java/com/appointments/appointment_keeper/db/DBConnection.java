package com.appointments.appointment_keeper.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class DBConnection {
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static Connection conn = null;

    public static void setCredentials(HashMap<String, String> settings) {
        URL = settings.get("url");
        USER = settings.get("user");
        PASSWORD = settings.get("password"); 
    }
        
    /** 
      * Set up the DB driver and establish a connection. 
      */
    public static void connect() throws SQLException {
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
        
    }

    /** 
      * Close the DB connection.
      */
    public static void closeConnection() {
        try {
          if (DBConnection.getCurrentConnection() != null) {
             conn.close();   
          }  
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }   
    }

    /** 
      * Get the current connection object.
      * @return The current connection object. 
      */
    public static Connection getCurrentConnection() {
        return conn;
    } 
    
    /**
     * Check if DB connection is still valid. 
     */
    public static boolean isValid() throws SQLException {
        return conn.isValid(0);
    }
    
}
