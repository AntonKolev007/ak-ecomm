package com.ecommerce.project.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class AddressRequestDTO implements Serializable {
    private Long addressId;
    @NotBlank(message = "{address.street.notBlank}")
    @Size(min = 5, message = "{address.street.size}")
    private String street;
    @NotBlank(message = "{address.buildingName.notBlank}")
    @Size(min = 5, message = "{Size.handleValidationErrors.buildingName}")
    private String buildingName;

    @NotBlank(message = "{address.city.notBlank}")
    @Size(min = 4, message = "{address.city.size}")
    private String city;

    @NotBlank(message = "{address.state.notBlank}")
    @Size(min = 2, message = "{address.state.size}")
    private String state;

    @NotBlank(message = "{address.country.notBlank}")
    @Size(min = 2, message = "{address.country.size}")
    private String country;

    @NotBlank(message = "{address.zipCode.notBlank}")
    @Size(min = 6, message = "{address.zipCode.size}")
    private String zipCode;

    public AddressRequestDTO() {
    }

    public AddressRequestDTO(Long addressId, String street, String buildingName, String city, String state, String country, String zipCode) {
        this.addressId = addressId;
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
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

}
