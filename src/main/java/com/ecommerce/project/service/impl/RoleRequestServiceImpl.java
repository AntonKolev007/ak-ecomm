package com.ecommerce.project.service.impl;

import com.ecommerce.project.payload.request.RoleRequestDTO;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.util.RoleRequestMapper;
import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.RoleRequest;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.RoleRepository;
import com.ecommerce.project.repositories.RoleRequestRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.service.RoleRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleRequestServiceImpl implements RoleRequestService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleRequestRepository roleRequestRepository;

    public RoleRequestServiceImpl(UserRepository userRepository, RoleRepository roleRepository, RoleRequestRepository roleRequestRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleRequestRepository = roleRequestRepository;
    }

    @Override
    public void requestSellerRole(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setUserId(userId);
        roleRequest.setRequestedRole(AppRole.ROLE_SELLER);
        roleRequest.setStatus("PENDING");
        roleRequest.setCreatedAt(LocalDateTime.now());
        roleRequestRepository.save(roleRequest);
    }

    @Override
    public List<RoleRequestDTO> getAllPendingRoleRequests() {
        return roleRequestRepository.findByStatus("PENDING")
                .stream()
                .map(RoleRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleRequestDTO> getRoleRequestsByUserId(Long userId) {
        return roleRequestRepository.findByUserId(userId)
                .stream()
                .map(RoleRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void approveRoleRequest(Long requestId) {
        RoleRequest roleRequest = roleRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("RoleRequest", "id", requestId));
        roleRequest.setStatus("APPROVED");
        roleRequestRepository.save(roleRequest);

        User user = userRepository.findById(roleRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", roleRequest.getUserId()));

        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", AppRole.ROLE_SELLER.name()));

        user.getRoles().add(sellerRole);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void rejectRoleRequest(Long requestId) {
        RoleRequest roleRequest = roleRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("RoleRequest", "id", requestId));
        roleRequest.setStatus("REJECTED");
        roleRequestRepository.save(roleRequest);
    }
}