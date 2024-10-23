package com.hikesenseserver.hikesenseserver.models;

public class CustomerAddress {
    
    private String address1;
    private String address2;
    private String city;
    private String postnumber;
    private String country;

    public CustomerAddress() {
    }

    public CustomerAddress(String address1, String address2, String city, String postnumber, String country) {
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.postnumber = postnumber;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostnumber() {
        return postnumber;
    }

    public void setPostnumber(String postnumber) {
        this.postnumber = postnumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    
}
