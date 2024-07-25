package com.ecommerce.project.service;

import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressRequestDTO;

import java.util.List;

public interface AddressService {

    AddressRequestDTO createAddress(AddressRequestDTO addressRequestDTO, User user);

    List<AddressRequestDTO> getAddresses();

    AddressRequestDTO getAddressById(Long addressId);

    List<AddressRequestDTO> getUserAddresses(User user);

    AddressRequestDTO updateAddress(Long addressId, AddressRequestDTO addressRequestDTO);

    String deleteAddressById(Long addressId);
}




