package com.ecommerce.project.util;

import com.ecommerce.project.payload.RoleRequestDTO;
import com.ecommerce.project.model.RoleRequest;

public class RoleRequestMapper {

    public static RoleRequestDTO toDto(RoleRequest roleRequest) {
        RoleRequestDTO dto = new RoleRequestDTO();
        dto.setId(roleRequest.getId());
        dto.setUserId(roleRequest.getUserId());
        dto.setRequestedRole(roleRequest.getRequestedRole());
        dto.setStatus(roleRequest.getStatus());
        dto.setCreatedAt(roleRequest.getCreatedAt());
        return dto;
    }

    public static RoleRequest toEntity(RoleRequestDTO dto) {
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setId(dto.getId());
        roleRequest.setUserId(dto.getUserId());
        roleRequest.setRequestedRole(dto.getRequestedRole());
        roleRequest.setStatus(dto.getStatus());
        roleRequest.setCreatedAt(dto.getCreatedAt());
        return roleRequest;
    }
}