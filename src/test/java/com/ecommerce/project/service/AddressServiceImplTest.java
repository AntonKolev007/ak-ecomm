package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressRequestDTO;
import com.ecommerce.project.repositories.AddressRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.service.impl.AddressServiceImpl;
import com.ecommerce.project.util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceImplTest {

    @Mock
    private AuthUtil authUtil;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    private User user;
    private Address address;
    private AddressRequestDTO addressRequestDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setUserName("testUser");
        user.setEmail("test@example.com");

        address = new Address();
        address.setAddressId(1L);
        address.setCity("Test City");
        address.setZipCode("12345");
        address.setState("Test State");
        address.setCountry("Test Country");
        address.setStreet("Test Street");
        address.setBuildingName("Test Building");
        address.setUser(user);

        addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setCity("Test City");
        addressRequestDTO.setZipCode("12345");
        addressRequestDTO.setState("Test State");
        addressRequestDTO.setCountry("Test Country");
        addressRequestDTO.setStreet("Test Street");
        addressRequestDTO.setBuildingName("Test Building");

        user.setAddresses(new ArrayList<>(List.of(address)));
    }

    @Test
    void testCreateAddress() {
        when(modelMapper.map(addressRequestDTO, Address.class)).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(address);
        when(modelMapper.map(address, AddressRequestDTO.class)).thenReturn(addressRequestDTO);

        AddressRequestDTO result = addressService.createAddress(addressRequestDTO, user);

        assertNotNull(result);
        assertEquals(addressRequestDTO.getCity(), result.getCity());
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void testGetAddresses() {
        when(addressRepository.findAll()).thenReturn(List.of(address));
        when(modelMapper.map(address, AddressRequestDTO.class)).thenReturn(addressRequestDTO);

        List<AddressRequestDTO> result = addressService.getAddresses();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    void testGetAddressById() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(modelMapper.map(address, AddressRequestDTO.class)).thenReturn(addressRequestDTO);

        AddressRequestDTO result = addressService.getAddressById(1L);

        assertNotNull(result);
        assertEquals(addressRequestDTO.getCity(), result.getCity());
        verify(addressRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAddressById_NotFound() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.getAddressById(1L));
        verify(addressRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserAddresses() {
        when(modelMapper.map(address, AddressRequestDTO.class)).thenReturn(addressRequestDTO);

        List<AddressRequestDTO> result = addressService.getUserAddresses(user);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testUpdateAddress() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(address)).thenReturn(address);
        when(modelMapper.map(address, AddressRequestDTO.class)).thenReturn(addressRequestDTO);

        AddressRequestDTO result = addressService.updateAddress(1L, addressRequestDTO);

        assertNotNull(result);
        assertEquals(addressRequestDTO.getCity(), result.getCity());
        verify(addressRepository, times(1)).findById(1L);
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void testUpdateAddress_NotFound() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.updateAddress(1L, addressRequestDTO));
        verify(addressRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteAddressById() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));

        String result = addressService.deleteAddressById(1L);

        assertNotNull(result);
        assertTrue(result.contains("successfully removed"));
        verify(addressRepository, times(1)).findById(1L);
        verify(addressRepository, times(1)).delete(address);
    }

    @Test
    void testDeleteAddressById_NotFound() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.deleteAddressById(1L));
        verify(addressRepository, times(1)).findById(1L);
    }
}
