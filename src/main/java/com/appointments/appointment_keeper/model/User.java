package com.appointments.appointment_keeper.model;

public class User {
    private static Integer id;
    private static String username;
    private static boolean loggedIn = false;
    
    /** 
     * Get the logged in user ID.
     * @return The logged in user ID. 
     */
    public static Integer getId() {
        return id;
    }
    
    /** 
     * Get the status of whether a user is currently logged in.
     * @return True if a user is currently logged in. 
     */
    public static boolean getLoggedIn() {
        return loggedIn;
    }
    
    /** 
     * Get the logged in username.
     * @return The logged in user ID. 
     */
    public static String getUsername() {
        return username;
    }
    
    /** 
     * Set the ID of the currently logged in user.
     * @param currentUser The logged in user ID.
     */
    public static void setId(Integer currentUser) {
        id = currentUser;
    }
    
    /** 
     * Set the username of the currently logged in user.
     * @param name The logged in username.
     */
    public static void setUsername(String name) {
        username = name;
    }
    
    /** 
     * Set the logged in status.
     * @param loggedInStatus The status of a logged in user 
     */
    public static void setLoggedIn(boolean loggedInStatus) {
        loggedIn = loggedInStatus;
    }
}
