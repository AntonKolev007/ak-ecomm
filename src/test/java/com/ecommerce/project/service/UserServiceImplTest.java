package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.request.UpdatePasswordRequest;
import com.ecommerce.project.payload.request.UpdateUsernameRequest;
import com.ecommerce.project.repositories.RoleRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.security.request.SignupRequest;
import com.ecommerce.project.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleName(AppRole.ROLE_USER);

        user = new User();
        user.setUserId(1L);
        user.setUserName("testuser");
        user.setEmail("test@example.com");
        user.setRoles(new HashSet<>(Collections.singleton(role)));
    }

    @Test
    void testExistsByUsername() {
        when(userRepository.existsByUserName(anyString())).thenReturn(true);

        boolean exists = userService.existsByUsername("testuser");

        assertTrue(exists);
        verify(userRepository, times(1)).existsByUserName("testuser");
    }

    @Test
    void testExistsByEmail() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        boolean exists = userService.existsByEmail("test@example.com");

        assertTrue(exists);
        verify(userRepository, times(1)).existsByEmail("test@example.com");
    }

    @Test
    void testSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUserName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegisterUser_Success() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newuser");
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("password");

        when(userRepository.existsByUserName(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");
        when(roleRepository.findByRoleName(AppRole.ROLE_USER)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.registerUser(signupRequest);

        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUserName());
        verify(userRepository, times(1)).existsByUserName("newuser");
        verify(userRepository, times(1)).existsByEmail("newuser@example.com");
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_UsernameAlreadyTaken() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("testuser");
        signupRequest.setEmail("newuser@example.com");

        when(userRepository.existsByUserName(anyString())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(signupRequest);
        });

        assertEquals("Error: Username is already taken!", exception.getMessage());
    }

    @Test
    void testRegisterUser_EmailAlreadyInUse() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newuser");
        signupRequest.setEmail("test@example.com");

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(signupRequest);
        });

        assertEquals("Error: Email is already in use!", exception.getMessage());
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUserName());
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("User not found with id: 1.", exception.getMessage());
    }

    @Test
    void testUpdateUsername_Success() {
        UpdateUsernameRequest updateUsernameRequest = new UpdateUsernameRequest();
        updateUsernameRequest.setUsername("newusername");

        when(userRepository.existsByUserName(anyString())).thenReturn(false);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.updateUsername(updateUsernameRequest, 1L);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUsername_UsernameAlreadyTaken() {
        UpdateUsernameRequest updateUsernameRequest = new UpdateUsernameRequest();
        updateUsernameRequest.setUsername("testuser");

        when(userRepository.existsByUserName(anyString())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUsername(updateUsernameRequest, 1L);
        });

        assertEquals("Error: Username is already taken!", exception.getMessage());
    }

    @Test
    void testUpdatePassword_Success() {
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.setPassword("newpassword");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");

        userService.updatePassword(updatePasswordRequest, 1L);

        verify(userRepository, times(1)).save(user);
        assertEquals("encodedpassword", user.getPassword());
    }

    @Test
    void testUpdatePassword_UserNotFound() {
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.setPassword("newpassword");

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updatePassword(updatePasswordRequest, 1L);
        });

        assertEquals("User not found with id: 1.", exception.getMessage());
    }

    @Test
    void testPromoteUserToRole_Success() {
        Role adminRole = new Role();
        adminRole.setRoleName(AppRole.ROLE_ADMIN);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(roleRepository.findByRoleName(AppRole.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));

        userService.promoteUserToRole(1L, "ROLE_ADMIN");

        verify(userRepository, times(1)).save(user);
        assertTrue(user.getRoles().contains(adminRole));
    }

    @Test
    void testPromoteUserToRole_RoleNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(roleRepository.findByRoleName(any(AppRole.class))).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.promoteUserToRole(1L, "ROLE_ADMIN");
        });

        assertEquals("Role not found with name: ROLE_ADMIN.", exception.getMessage());
    }

    @Test
    void testDemoteUserFromRole_Success() {
        Role adminRole = new Role();
        adminRole.setRoleName(AppRole.ROLE_ADMIN);
        user.getRoles().add(adminRole);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(roleRepository.findByRoleName(AppRole.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));

        userService.demoteUserFromRole(1L, "ROLE_ADMIN");

        verify(userRepository, times(1)).save(user);
        assertFalse(user.getRoles().contains(adminRole));
    }

    @Test
    void testDemoteUserFromRole_RoleNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(roleRepository.findByRoleName(any(AppRole.class))).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.demoteUserFromRole(1L, "ROLE_ADMIN");
        });

        assertEquals("Role not found with name: ROLE_ADMIN.", exception.getMessage());
    }

    @Test
    void testDemoteUserFromRole_UserDoesNotHaveRole() {
        Role adminRole = new Role();
        adminRole.setRoleName(AppRole.ROLE_ADMIN);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(roleRepository.findByRoleName(AppRole.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.demoteUserFromRole(1L, "ROLE_ADMIN");
        });

        assertEquals("Error: User does not have the role ROLE_ADMIN", exception.getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(anyLong())).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("User not found with id: 1.", exception.getMessage());
    }

    @Test
    void testGetCurrentUsername() {
        when(authentication.getName()).thenReturn("testuser");

        String username = userService.getCurrentUsername(authentication);

        assertEquals("testuser", username);
    }

    @Test
    void testGetCurrentUser_Success() {
        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(user));

        User currentUser = userService.getCurrentUser(authentication);

        assertNotNull(currentUser);
        assertEquals("testuser", currentUser.getUserName());
    }

    @Test
    void testGetCurrentUser_UserNotFound() {
        when(authentication.getName()).thenReturn("unknownuser");
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getCurrentUser(authentication);
        });

        assertEquals("User not found with username: unknownuser.", exception.getMessage());
    }
}
