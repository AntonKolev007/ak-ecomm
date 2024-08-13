package com.ecommerce.project.service;

import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.UpdatePasswordRequest;
import com.ecommerce.project.payload.UpdateUsernameRequest;
import com.ecommerce.project.security.request.SignupRequest;
import org.springframework.security.core.Authentication;

public interface UserService {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User saveUser(User user);
    User registerUser(SignupRequest signUpRequest);
    User getUserById(Long id);
    void updateUsername(UpdateUsernameRequest updateUsernameRequest, Long userId);
    void updatePassword(UpdatePasswordRequest updatePasswordRequest, Long userId);
    void promoteUserToRole(Long userId, String role);
    void demoteUserFromRole(Long userId, String role);
    void deleteUser(Long userId);
    String getCurrentUsername(Authentication authentication);
    User getCurrentUser(Authentication authentication);
}