package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 characters long!")
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building name must be at least 5 characters!")
    private String buildingName;
    @NotBlank
    @Size(min = 4, message = "City name must be at least 4 characters!")
    private String city;
    @NotBlank
    @Size(min = 2, message = "State name must be at least 2 characters!")
    private String state;
    @NotBlank
    @Size(min = 2, message = "Country name must be at least 2 characters!")
    private String country;
    @NotBlank
    @Size(min = 6, message = "Postal code must be at least 6 characters!")
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "user_d")
    private User user;

    public Address(String street,
                   String buildingName,
                   String city,
                   String state,
                   String country,
                   String zipCode,
                   User user) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
        this.user = user;
    }

    public Address() {
    }

    public Address(Long addressId,
                   String street,
                   String buildingName,
                   String city,
                   String state,
                   String country,
                   String zipCode,
                   User user) {
        this.addressId = addressId;
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
        this.user = user;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", street='" + street + '\'' +
                ", buildingName='" + buildingName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
