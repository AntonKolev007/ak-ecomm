package com.ecommerce.project.service.impl;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.request.UpdatePasswordRequest;
import com.ecommerce.project.payload.request.UpdateUsernameRequest;
import com.ecommerce.project.repositories.RoleRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.security.request.SignupRequest;
import com.ecommerce.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUserName(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", AppRole.ROLE_USER.name()));
        user.setRoles(Collections.singleton(userRole));

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Override
    public void updateUsername(UpdateUsernameRequest updateUsernameRequest, Long userId) {
        if (userRepository.existsByUserName(updateUsernameRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setUserName(updateUsernameRequest.getUsername());
        userRepository.save(user);
    }

    @Override
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void promoteUserToRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Role newRole = roleRepository.findByRoleName(AppRole.valueOf(role))
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", role));

        Set<Role> roles = new HashSet<>(user.getRoles());
        roles.add(newRole);
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public void demoteUserFromRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Role roleToRemove = roleRepository.findByRoleName(AppRole.valueOf(role))
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", role));

        Set<Role> roles = new HashSet<>(user.getRoles());
        if (!roles.contains(roleToRemove)) {
            throw new RuntimeException("Error: User does not have the role " + role);
        }

        roles.remove(roleToRemove);
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    public String getCurrentUsername(Authentication authentication) {
        return authentication.getName();
    }

    @Override
    public User getCurrentUser(Authentication authentication) {
        return userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", authentication.getName()));
    }
}