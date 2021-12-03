/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.appointments.appointment_keeper.model;

/**
 *
 * @author Jesse
 */
public class Customer {
    private Integer id;
    private String name;
    private String address;
    private String phoneNumber;
    private String postalCode;
    private Integer provinceId;
    private String provinceName;
    private String countryName;
    
    public Customer(Integer id, String name, String address, String phoneNumber, String postalCode, Integer provinceId, 
            String provinceName, String countryName) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.postalCode = postalCode;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.countryName = countryName;
    }
    
    /** 
     * Get the customer ID.
     * @return The customer ID. 
     */
    public Integer getId() {
        return id;
    }
    
    /** 
     * Get the customer name.
     * @return The customer name. 
     */
    public String getName() {
        return name;
    }
    
    /** 
     * Get the customer address.
     * @return The customer address. 
     */
    public String getAddress() {
        return address;
    }
    
    /** 
     * Get the customer phone number.
     * @return The customer phone number. 
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /** 
     * Get the customer postal code.
     * @return The customer postal code. 
     */
    public String getPostalCode() {
        return postalCode;
    }
    
    /** 
     * Get the customer division ID.
     * @return The customer division ID. 
     */
    public Integer getProvinceId() {
        return provinceId;
    }
    
    /** 
     * Get the customer country name.
     * @return The customer country name. 
     */
    public String getCountryName() {
        return countryName;
    }
    
    /** 
     * Get the customer division name.
     * @return The customer division name. 
     */
    public String getProvinceName() {
        return provinceName;
    }
}
