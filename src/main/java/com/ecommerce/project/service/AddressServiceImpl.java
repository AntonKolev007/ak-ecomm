package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressRequestDTO;
import com.ecommerce.project.repositories.AddressRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AuthUtil authUtil;
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public AddressServiceImpl(AuthUtil authUtil, AddressRepository addressRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.authUtil = authUtil;
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public AddressRequestDTO createAddress(AddressRequestDTO addressRequestDTO, User user) {
        Address address = modelMapper.map(addressRequestDTO, Address.class);

        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);
        Address saveAddress = addressRepository.save(address);

        return modelMapper.map(saveAddress, AddressRequestDTO.class);

    }

    @Override
    public List<AddressRequestDTO> getAddresses() {
        List<Address> addresses = addressRepository.findAll();
        List<AddressRequestDTO> addressRequestDTOs = addresses.stream()
                .map(address -> modelMapper.map(address, AddressRequestDTO.class))
                .toList();
        return addressRequestDTOs;
    }

    @Override
    public AddressRequestDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        return modelMapper.map(address, AddressRequestDTO.class);
    }

    @Override
    public List<AddressRequestDTO> getUserAddresses(User user) {
        List<Address> addresses = user.getAddresses();
        List<AddressRequestDTO> addressRequestDTOs = addresses.stream()
                .map(address -> modelMapper.map(address, AddressRequestDTO.class))
                .toList();
        return addressRequestDTOs;
    }

    @Override
    public AddressRequestDTO updateAddress(Long addressId, AddressRequestDTO addressRequestDTO) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        address.setCity(addressRequestDTO.getCity());
        address.setZipCode(addressRequestDTO.getZipCode());
        address.setState(addressRequestDTO.getState());
        address.setCountry(addressRequestDTO.getCountry());
        address.setStreet(addressRequestDTO.getStreet());
        address.setBuildingName(addressRequestDTO.getBuildingName());

        Address updatedAddress = addressRepository.save(address);

        User user = address.getUser();
        user.getAddresses().removeIf(address1 -> address1.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);

        return modelMapper.map(updatedAddress, AddressRequestDTO.class);
    }

    @Override
    public String deleteAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        User user = address.getUser();
        user.getAddresses().removeIf(address1 -> address1.getAddressId().equals(addressId));
        userRepository.save(user);

        addressRepository.delete(address);

        return "Address with id " + addressId + " located in " + address.getCountry() + " was successfully removed!";
    }
}
