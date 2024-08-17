package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.repositories.RoleRepository;
import com.ecommerce.project.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role roleUser;

    @BeforeEach
    void setUp() {
        roleUser = new Role();
        roleUser.setRoleName(AppRole.ROLE_USER);
    }

    @Test
    void testFindByRoleName_RoleExists() {
        when(roleRepository.findByRoleName(AppRole.ROLE_USER)).thenReturn(Optional.of(roleUser));

        Role foundRole = roleService.findByRoleName(AppRole.ROLE_USER);

        assertNotNull(foundRole);
        assertEquals(AppRole.ROLE_USER, foundRole.getRoleName());

        verify(roleRepository, times(1)).findByRoleName(AppRole.ROLE_USER);
    }

    @Test
    void testFindByRoleName_RoleNotFound() {
        when(roleRepository.findByRoleName(AppRole.ROLE_ADMIN)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            roleService.findByRoleName(AppRole.ROLE_ADMIN);
        });

        assertEquals("Role not found with name: ROLE_ADMIN.", exception.getMessage());

        verify(roleRepository, times(1)).findByRoleName(AppRole.ROLE_ADMIN);
    }
}
