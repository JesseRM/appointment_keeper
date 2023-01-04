package com.appointments.appointment_keeper.util;

import com.appointments.appointment_keeper.db.DBConnection;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Jesse
 */
public class Authenticate {
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    
    public static boolean user(String username, String password) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        String salt = getStoredSalt(username);
        String hash = hash(password, salt);
        
        if (salt == null) return false;
        
        String query = "SELECT password_hash FROM users WHERE name= ?";
        PreparedStatement statement = DBConnection.getCurrentConnection().prepareStatement(query);
        
        statement.setString(1, username);
        
        ResultSet queryResult = statement.executeQuery();
        
        if (queryResult.next()) {
            String storedHash = queryResult.getString("password_hash");
            
            if (storedHash.equals(hash)) return true;
        }
        
        return false;
    }
    
    public static String getStoredSalt(String username) throws SQLException {
        String query = "SELECT salt FROM users WHERE name= ?";
        PreparedStatement statement = DBConnection.getCurrentConnection().prepareStatement(query);
        
        statement.setString(1, username);
        
        ResultSet queryResult = statement.executeQuery();
        
        String salt = null;
        if (queryResult.next()) {
            salt = queryResult.getString("salt");
        }
        
        return salt;
    }
    
    public static String hash(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
 
        byte[] encBytes = skf.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(encBytes);
    }
    
    public static String createSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        
        return Base64.getEncoder().encodeToString(salt);
    }
}
