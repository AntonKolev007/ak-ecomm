package com.ecommerce.project.controller;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressRequestDTO;
import com.ecommerce.project.service.AddressService;
import com.ecommerce.project.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    private final AuthUtil authUtil;
    private final AddressService addressService;

    public AddressController(AuthUtil authUtil, AddressService addressService) {
        this.authUtil = authUtil;
        this.addressService = addressService;
    }

    @PostMapping("/addresses")
    public ResponseEntity<AddressRequestDTO> createAddress(@Valid @RequestBody AddressRequestDTO addressRequestDTO) {
        User user = authUtil.loggedInUser();
        try {
            AddressRequestDTO savedAddressDTO = addressService.createAddress(addressRequestDTO, user);
            return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressRequestDTO>> getAllAddresses() {
        try {
            List<AddressRequestDTO> addressList = addressService.getAddresses();
            return new ResponseEntity<>(addressList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressRequestDTO> getAddressById(@PathVariable Long addressId) {
        try {
            AddressRequestDTO addressRequestDTO = addressService.getAddressById(addressId);
            return new ResponseEntity<>(addressRequestDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressRequestDTO>> getAllUserAddresses() {
        User user = authUtil.loggedInUser();
        try {
            List<AddressRequestDTO> addressList = addressService.getUserAddresses(user);
            return new ResponseEntity<>(addressList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressRequestDTO> updateAddressById(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequestDTO addressRequestDTO) {
        try {
            AddressRequestDTO updatedAddress = addressService.updateAddress(addressId, addressRequestDTO);
            return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddressById(@PathVariable Long addressId) {
        try {
            String status = addressService.deleteAddressById(addressId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}