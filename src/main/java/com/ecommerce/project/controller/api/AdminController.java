package com.ecommerce.project.controller.api;

import com.ecommerce.project.payload.request.RoleRequestDTO;
import com.ecommerce.project.payload.response.APIResponse;
import com.ecommerce.project.security.response.MessageResponse;
import com.ecommerce.project.service.RoleRequestService;
import com.ecommerce.project.service.UserService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final RoleRequestService roleRequestService;

    public AdminController(UserService userService, RoleRequestService roleRequestService) {
        this.userService = userService;
        this.roleRequestService = roleRequestService;
    }

    @PostMapping("/promote")
    public ResponseEntity<Object> promoteUser(@RequestParam Long userId, @RequestParam @NotBlank String role) {
        try {
            userService.promoteUserToRole(userId, role);
            return ResponseEntity.ok(new MessageResponse("User promoted successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/demote")
    public ResponseEntity<Object> demoteUser(@RequestParam Long userId, @RequestParam @NotBlank String role) {
        try {
            userService.demoteUserFromRole(userId, role);
            return ResponseEntity.ok(new MessageResponse("User demoted successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteUser(@RequestParam Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/role-requests")
    public ResponseEntity<?> getAllPendingRoleRequests() {
        try {
            List<RoleRequestDTO> roleRequests = roleRequestService.getAllPendingRoleRequests();
            return ResponseEntity.ok(roleRequests);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/role-requests/approve")
    public ResponseEntity<APIResponse> approveRoleRequest(@RequestParam Long requestId) {
        try {
            roleRequestService.approveRoleRequest(requestId);
            return ResponseEntity.ok(new APIResponse("Role request approved successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new APIResponse(e.getMessage(), false));
        }
    }

    @PostMapping("/role-requests/reject")
    public ResponseEntity<APIResponse> rejectRoleRequest(@RequestParam Long requestId) {
        try {
            roleRequestService.rejectRoleRequest(requestId);
            return ResponseEntity.ok(new APIResponse("Role request rejected successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new APIResponse(e.getMessage(), false));
        }
    }
}