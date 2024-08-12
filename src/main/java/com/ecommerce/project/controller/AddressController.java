package com.ecommerce.project.controller;

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
    public ResponseEntity<AddressRequestDTO> createAddress(@Valid
                                                           @RequestBody AddressRequestDTO addressRequestDTO) {
        User user = authUtil.loggedInUser();
        AddressRequestDTO savedAddressDTO = addressService.createAddress(addressRequestDTO, user);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressRequestDTO>> getAllAddresses() {
        List<AddressRequestDTO> addressList = addressService.getAddresses();
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressRequestDTO> getAddressById(@PathVariable Long addressId) {
        AddressRequestDTO addressRequestDTO = addressService.getAddressById(addressId);
        return new ResponseEntity<>(addressRequestDTO, HttpStatus.OK);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressRequestDTO>> getAllUserAddresses() {
        User user = authUtil.loggedInUser();
        List<AddressRequestDTO> addressList = addressService.getUserAddresses(user);
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressRequestDTO> updateAddressById(
            @PathVariable Long addressId,
            @RequestBody AddressRequestDTO addressRequestDTO) {

        AddressRequestDTO updatedAddress = addressService.updateAddress(addressId, addressRequestDTO);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddressById(@PathVariable Long addressId) {

        String status = addressService.deleteAddressById(addressId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}

