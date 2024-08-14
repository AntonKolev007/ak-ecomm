package com.ecommerce.project.controller;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressRequestDTO;
import com.ecommerce.project.service.AddressService;
import com.ecommerce.project.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AddressController {

    private final AuthUtil authUtil;
    private final AddressService addressService;
    private final MessageSource messageSource;

    public AddressController(AuthUtil authUtil, AddressService addressService, MessageSource messageSource) {
        this.authUtil = authUtil;
        this.addressService = addressService;
        this.messageSource = messageSource;
    }

    @PostMapping("/addresses")
    public ResponseEntity<Object> createAddress(@Valid @RequestBody AddressRequestDTO addressRequestDTO, BindingResult bindingResult, Locale locale) {
        if (!isAuthenticated()) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized: No active session."), HttpStatus.UNAUTHORIZED);
        }
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult, locale);
        }
        try {
            User user = authUtil.loggedInUser();
            AddressRequestDTO savedAddressDTO = addressService.createAddress(addressRequestDTO, user);
            return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/addresses")
    public ResponseEntity<Object> getAllAddresses() {
        if (!isAuthenticated()) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized: No active session."), HttpStatus.UNAUTHORIZED);
        }
        try {
            List<AddressRequestDTO> addressList = addressService.getAddresses();
            return new ResponseEntity<>(addressList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<Object> getAddressById(@PathVariable Long addressId) {
        if (!isAuthenticated()) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized: No active session."), HttpStatus.UNAUTHORIZED);
        }
        try {
            AddressRequestDTO addressRequestDTO = addressService.getAddressById(addressId);
            return new ResponseEntity<>(addressRequestDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Resource not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<Object> getAllUserAddresses() {
        if (!isAuthenticated()) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized: No active session."), HttpStatus.UNAUTHORIZED);
        }
        try {
            User user = authUtil.loggedInUser();
            List<AddressRequestDTO> addressList = addressService.getUserAddresses(user);
            return new ResponseEntity<>(addressList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<Object> updateAddressById(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequestDTO addressRequestDTO, BindingResult bindingResult, Locale locale) {
        if (!isAuthenticated()) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized: No active session."), HttpStatus.UNAUTHORIZED);
        }
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult, locale);
        }
        try {
            AddressRequestDTO updatedAddress = addressService.updateAddress(addressId, addressRequestDTO);
            return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Resource not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<Object> deleteAddressById(@PathVariable Long addressId) {
        if (!isAuthenticated()) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized: No active session."), HttpStatus.UNAUTHORIZED);
        }
        try {
            String status = addressService.deleteAddressById(addressId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Resource not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Object> handleValidationErrors(BindingResult bindingResult, Locale locale) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            String localizedErrorMessage = messageSource.getMessage(error, locale);
            errors.put(error.getField(), localizedErrorMessage);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName());
    }

    // Inner class to represent an error response
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}