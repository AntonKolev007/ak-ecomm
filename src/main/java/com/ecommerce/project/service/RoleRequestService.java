package com.ecommerce.project.service;

import com.ecommerce.project.payload.RoleRequestDTO;

import java.util.List;

public interface RoleRequestService {
    void requestSellerRole(Long userId);
    List<RoleRequestDTO> getAllPendingRoleRequests();
    List<RoleRequestDTO> getRoleRequestsByUserId(Long userId);
    void approveRoleRequest(Long requestId);
    void rejectRoleRequest(Long requestId);
}
