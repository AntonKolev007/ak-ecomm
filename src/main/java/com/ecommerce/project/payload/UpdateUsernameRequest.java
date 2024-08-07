package com.ecommerce.project.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateUsernameRequest {
    
    @NotBlank(message = "Username cannot be blank!")
    @Size(min = 3, max = 20, message = "User name must be between 3 and 20 characters.")
    private String username;

    public UpdateUsernameRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
