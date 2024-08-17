package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.*;
import com.ecommerce.project.payload.request.RoleRequestDTO;
import com.ecommerce.project.repositories.RoleRepository;
import com.ecommerce.project.repositories.RoleRequestRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.service.impl.RoleRequestServiceImpl;
import com.ecommerce.project.util.RoleRequestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleRequestServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleRequestRepository roleRequestRepository;

    @InjectMocks
    private RoleRequestServiceImpl roleRequestService;

    private RoleRequest roleRequest;
    private User user;
    private Role sellerRole;

    @BeforeEach
    void setUp() {
        roleRequest = new RoleRequest();
        roleRequest.setId(1L);  // Adjusted method name to `setId` for the RoleRequest ID
        roleRequest.setUserId(1L);
        roleRequest.setRequestedRole(AppRole.ROLE_SELLER);
        roleRequest.setStatus("PENDING");
        roleRequest.setCreatedAt(LocalDateTime.now());

        user = new User();
        user.setUserId(1L);  // Adjusted method name to `setId` for the User ID
        user.setUserName("testuser");

        sellerRole = new Role();
        sellerRole.setRoleName(AppRole.ROLE_SELLER);
    }

    @Test
    void testRequestSellerRole_UserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRequestRepository.save(any(RoleRequest.class))).thenReturn(roleRequest);

        assertDoesNotThrow(() -> roleRequestService.requestSellerRole(1L));

        verify(userRepository, times(1)).findById(1L);
        verify(roleRequestRepository, times(1)).save(any(RoleRequest.class));
    }

    @Test
    void testRequestSellerRole_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleRequestService.requestSellerRole(1L));

        verify(userRepository, times(1)).findById(1L);
        verify(roleRequestRepository, never()).save(any(RoleRequest.class));
    }

    @Test
    void testGetAllPendingRoleRequests() {
        when(roleRequestRepository.findByStatus("PENDING")).thenReturn(Collections.singletonList(roleRequest));

        List<RoleRequestDTO> result = roleRequestService.getAllPendingRoleRequests();

        assertEquals(1, result.size());
        verify(roleRequestRepository, times(1)).findByStatus("PENDING");
    }

    @Test
    void testGetRoleRequestsByUserId() {
        when(roleRequestRepository.findByUserId(1L)).thenReturn(Collections.singletonList(roleRequest));

        List<RoleRequestDTO> result = roleRequestService.getRoleRequestsByUserId(1L);

        assertEquals(1, result.size());
        verify(roleRequestRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testApproveRoleRequest_RequestExists() {
        when(roleRequestRepository.findById(1L)).thenReturn(Optional.of(roleRequest));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByRoleName(AppRole.ROLE_SELLER)).thenReturn(Optional.of(sellerRole));

        assertDoesNotThrow(() -> roleRequestService.approveRoleRequest(1L));

        assertEquals("APPROVED", roleRequest.getStatus());
        assertTrue(user.getRoles().contains(sellerRole));

        verify(roleRequestRepository, times(1)).findById(1L);
        verify(roleRequestRepository, times(1)).save(roleRequest);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
        verify(roleRepository, times(1)).findByRoleName(AppRole.ROLE_SELLER);
    }

    @Test
    void testApproveRoleRequest_RequestNotFound() {
        when(roleRequestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleRequestService.approveRoleRequest(1L));

        verify(roleRequestRepository, times(1)).findById(1L);
        verify(roleRequestRepository, never()).save(any(RoleRequest.class));
    }

    @Test
    void testRejectRoleRequest_RequestExists() {
        when(roleRequestRepository.findById(1L)).thenReturn(Optional.of(roleRequest));

        assertDoesNotThrow(() -> roleRequestService.rejectRoleRequest(1L));

        assertEquals("REJECTED", roleRequest.getStatus());

        verify(roleRequestRepository, times(1)).findById(1L);
        verify(roleRequestRepository, times(1)).save(roleRequest);
    }

    @Test
    void testRejectRoleRequest_RequestNotFound() {
        when(roleRequestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleRequestService.rejectRoleRequest(1L));

        verify(roleRequestRepository, times(1)).findById(1L);
        verify(roleRequestRepository, never()).save(any(RoleRequest.class));
    }
}
