package com.ecommerce.project.controller;

import com.ecommerce.project.payload.RoleRequestDTO;
import com.ecommerce.project.security.response.MessageResponse;
import com.ecommerce.project.security.services.UserDetailsImpl;
import com.ecommerce.project.service.RoleRequestService;
import com.ecommerce.project.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    private final UserService userService;
    private final RoleRequestService roleRequestService;

    public UserProfileController(UserService userService, RoleRequestService roleRequestService) {
        this.userService = userService;
        this.roleRequestService = roleRequestService;
    }

    @PostMapping("/request-seller")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> requestSellerRole(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            roleRequestService.requestSellerRole(userDetails.getId());
            return ResponseEntity.ok(new MessageResponse("Seller role request sent successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> viewProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            return ResponseEntity.ok(userService.getCurrentUser((Authentication) userDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/role-requests")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> getRoleRequests(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<RoleRequestDTO> roleRequests = roleRequestService.getRoleRequestsByUserId(userDetails.getId());
            return ResponseEntity.ok(roleRequests);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}