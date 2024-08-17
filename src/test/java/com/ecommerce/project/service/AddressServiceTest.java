package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.request.AddressRequestDTO;
import com.ecommerce.project.repositories.AddressRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.service.impl.AddressServiceImpl;
import com.ecommerce.project.util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AddressServiceTest {

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private AuthUtil authUtil;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAddress() {
        User user = new User();
        user.setAddresses(new ArrayList<>());
        Address address = new Address();
        AddressRequestDTO addressRequestDTO = new AddressRequestDTO();

        when(modelMapper.map(addressRequestDTO, Address.class)).thenReturn(address);
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(modelMapper.map(address, AddressRequestDTO.class)).thenReturn(addressRequestDTO);

        AddressRequestDTO result = addressService.createAddress(addressRequestDTO, user);

        assertNotNull(result);
        assertEquals(addressRequestDTO, result);
        assertTrue(user.getAddresses().contains(address));
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    public void testGetAddresses() {
        List<Address> addresses = List.of(new Address());
        List<AddressRequestDTO> addressRequestDTOs = List.of(new AddressRequestDTO());

        when(addressRepository.findAll()).thenReturn(addresses);
        when(modelMapper.map(any(Address.class), eq(AddressRequestDTO.class))).thenReturn(addressRequestDTOs.get(0));

        List<AddressRequestDTO> result = addressService.getAddresses();

        assertNotNull(result);
        assertEquals(addressRequestDTOs.size(), result.size());
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    public void testGetAddressById() {
        Long addressId = 1L;
        Address address = new Address();
        AddressRequestDTO addressRequestDTO = new AddressRequestDTO();

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(modelMapper.map(address, AddressRequestDTO.class)).thenReturn(addressRequestDTO);

        AddressRequestDTO result = addressService.getAddressById(addressId);

        assertNotNull(result);
        assertEquals(addressRequestDTO, result);
        verify(addressRepository, times(1)).findById(addressId);
    }

    @Test
    public void testGetAddressByIdNotFound() {
        Long addressId = 1L;

        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            addressService.getAddressById(addressId);
        });

        verify(addressRepository, times(1)).findById(addressId);
    }

    @Test
    public void testGetUserAddresses() {
        User user = new User();
        user.setAddresses(List.of(new Address()));
        List<AddressRequestDTO> addressRequestDTOs = List.of(new AddressRequestDTO());

        when(modelMapper.map(any(Address.class), eq(AddressRequestDTO.class))).thenReturn(addressRequestDTOs.get(0));

        List<AddressRequestDTO> result = addressService.getUserAddresses(user);

        assertNotNull(result);
        assertEquals(addressRequestDTOs.size(), result.size());
    }

    @Test
    public void testUpdateAddress() {
        Long addressId = 1L;
        Address address = new Address();
        User user = new User();
        user.setAddresses(new ArrayList<>());
        address.setUser(user);

        AddressRequestDTO addressRequestDTO = new AddressRequestDTO();
        Address updatedAddress = new Address();

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(addressRepository.save(address)).thenReturn(updatedAddress);
        when(modelMapper.map(updatedAddress, AddressRequestDTO.class)).thenReturn(addressRequestDTO);

        AddressRequestDTO result = addressService.updateAddress(addressId, addressRequestDTO);

        assertNotNull(result);
        assertEquals(addressRequestDTO, result);
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    public void testUpdateAddressNotFound() {
        Long addressId = 1L;
        AddressRequestDTO addressRequestDTO = new AddressRequestDTO();

        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            addressService.updateAddress(addressId, addressRequestDTO);
        });

        verify(addressRepository, times(1)).findById(addressId);
    }

    @Test
    public void testDeleteAddressById() {
        Long addressId = 1L;
        Address address = new Address();
        User user = new User();
        user.setAddresses(new ArrayList<>());
        address.setUser(user);

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        String result = addressService.deleteAddressById(addressId);

        assertNotNull(result);
        verify(addressRepository, times(1)).delete(address);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteAddressByIdNotFound() {
        Long addressId = 1L;

        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            addressService.deleteAddressById(addressId);
        });

        verify(addressRepository, times(1)).findById(addressId);
    }
}
