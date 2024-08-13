package com.ecommerce.project.payload;

import com.ecommerce.project.model.AppRole;

import java.time.LocalDateTime;

public class RoleRequestDTO {
    private Long id;
    private Long userId;
    private AppRole requestedRole;
    private String status;
    private LocalDateTime createdAt;

    public RoleRequestDTO() {
    }

    public RoleRequestDTO(Long id, Long userId, AppRole requestedRole, String status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.requestedRole = requestedRole;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AppRole getRequestedRole() {
        return requestedRole;
    }

    public void setRequestedRole(AppRole requestedRole) {
        this.requestedRole = requestedRole;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}