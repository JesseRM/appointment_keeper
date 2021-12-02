/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.appointments.appointment_keeper.model;

import java.time.LocalDateTime;

/**
 *
 * @author Jesse
 */
public class Appointment {
    private Integer id;
    private Integer userId;
    private Integer customerId;
    private String title;
    private String description;
    private String type;
    private String location;
    private String status;
    private String customerName;
    private String userName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    public Appointment(Integer id, Integer userId, Integer customerId, String title, String description, String type, String location, 
            String status, String customerName, String userName, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.userId = userId;
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.location = location;
        this.status = status;
        this.customerName = customerName;
        this.userName = userName;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    /** 
     * Get the appointment ID.
     * @return The appointment ID. 
     */
    public Integer getId() {
        return id;
    }
    
    /** 
     * Get the user ID.
     * @return The user ID. 
     */
    public Integer getUserId() {
        return userId;
    }
    
    /** 
     * Get the customer ID.
     * @return The customer ID. 
     */
    public Integer getCustomerId() {
        return customerId;
    }
    
    /** 
     * Get the appointment title.
     * @return The appointment title. 
     */
    public String getTitle() {
        return title;
    }
    
    /** 
     * Get the appointment description.
     * @return The appointment description. 
     */
    public String getDescription() {
        return description;
    }
    
    /** 
     * Get the appointment type.
     * @return The appointment type. 
     */
    public String getType() {
        return type;
    }
    
    /** 
     * Get the appointment location.
     * @return The appointment location. 
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * Get the appointment status.
     * @return The appointment status.
     */
    public String getStatus() {
        return status;
    }
    
    /** 
     * Get the customer name.
     * @return The customer name. 
     */
    public String getCustomerName() {
        return customerName;
    }
    
    /** 
     * Get the user name.
     * @return The user name. 
     */
    public String getUserName() {
        return userName;
    }
    
    /** 
     * Get the appointment date.
     * @return The appointment date. 
     */
    public String getDate() {
        return startTime.toLocalDate().toString();
    }
    
    /** 
     * Get the appointment start time.
     * @return The appointment start time. 
     */
    public String getStartTime() {
        return  startTime.toLocalTime().toString();
    }
    
    /** 
     * Get the appointment start local date time.
     * @return The appointment start dateTime. 
     */
    public LocalDateTime getStart() {
        return startTime;
    }
    
    /** 
     * Get the appointment end time.
     * @return The appointment end time. 
     */
    public String getEndTime() {
        return endTime.toLocalTime().toString();
    }
    
    /** 
     * Get the appointment end local date time.
     * @return The appointment ID. 
     */
    public LocalDateTime getEnd() {
        return endTime;
    }
}
