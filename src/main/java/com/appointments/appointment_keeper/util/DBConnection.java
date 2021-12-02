package com.appointments.appointment_keeper.util;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URI = "jdbc:postgresql://ec2-34-198-189-252.compute-1.amazonaws.com:5432/d1ar619r4jri83";
    private static final String USER = "coseaxkdgygapa";
    private static final String PASSWORD = "88dd93415af5c1655a4ee50f002a3b54f5d6da77b1df72057400948215c3fc4b";
    private static Connection conn = null;

    /** 
      * Set up the DB driver and establish a connection. 
      */
    public static void connect() {
        try {
            conn = DriverManager.getConnection(URI, USER, PASSWORD);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** 
      * Close the DB connection.
      */
    public static void closeConnection() {
        try {
          conn.close();  
        }
        catch (SQLException e) {
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
}
