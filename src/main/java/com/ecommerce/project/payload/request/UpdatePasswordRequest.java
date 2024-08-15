package com.ecommerce.project.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdatePasswordRequest {

    @NotBlank(message = "Password cannot be blank!")
    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters.")
    private String password;

    public UpdatePasswordRequest() {}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
