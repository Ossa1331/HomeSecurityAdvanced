package com.example.homesecurity.entity;

import jakarta.persistence.*;

@Entity
@Table(name="address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="street")
    private String street;
    @Column(name="housenumber")
    private Integer houseNumber;
    @Column(name="city")
    private String city;
    @Column(name="postalcode")
    private Integer postalCode;
    @Column(name="ownerusername")
    private String ownerUsername;

    public Address(){}

    public Address(String street, Integer houseNumber, String city, Integer postalCode,String ownerUsername){
        this.street=street;
        this.houseNumber=houseNumber;
        this.city=city;
        this.ownerUsername=ownerUsername;
        this.postalCode=postalCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }
}
