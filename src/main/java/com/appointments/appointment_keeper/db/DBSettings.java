package com.appointments.appointment_keeper.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class DBSettings {
    private static final String PROPS_FILE_NAME = "db.properties";
    private static String user;
    private static String password;
    private static String url;
    
    public static HashMap getAll() {
        HashMap<String, String> settings = new HashMap<>();
        settings.put("user", user);
        settings.put("password", password);
        settings.put("url", url);
        
        return settings;
    }
     
    public static void set() throws IOException{
        Properties settings = new Properties();
        FileInputStream in = new FileInputStream(PROPS_FILE_NAME);
        
        settings.load(in);
        
        user = settings.getProperty("user");
        password = settings.getProperty("password");
        url = settings.getProperty("url");
        
        in.close();
        
    }
        
    public static void updateFile(String user, String password, String url) throws FileNotFoundException, IOException {
        Properties settings = new Properties();
        FileOutputStream out = new FileOutputStream(PROPS_FILE_NAME);
        
        settings.setProperty("url", url);
        settings.setProperty("user", user);
        settings.setProperty("password", password);
        
        settings.store(out, "Database Settings");
        out.close();
    }
    
    public static boolean propsFileExists() {
        File file = new File(PROPS_FILE_NAME);
        
        return file.isFile();
    }
    
    public static boolean deleteFile() {
        File settingsFile = new File(PROPS_FILE_NAME); 
        
        return settingsFile.delete();
    }
    
}