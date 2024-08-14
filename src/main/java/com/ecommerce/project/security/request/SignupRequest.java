package com.ecommerce.project.security.request;

import java.util.Set;

import jakarta.validation.constraints.*;

public class SignupRequest {
    @NotBlank(message = "Username cannot be blank!")
    @Size(min = 3, max = 20, message="{Size.signupRequest.username}")
    private String username;

    @NotBlank (message = "Email cannot be blank!")
    @Size(max = 50, message = "Email should be no longer than 50 characters!")
    @Email
    private String email;

    private Set<String> role;

    @NotBlank (message = "Password cannot be blank!")
    @Size(min = 6, max = 40, message = "{Size.signupRequest.password}")
    private String password;

    public SignupRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRole() {
        return this.role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }



}