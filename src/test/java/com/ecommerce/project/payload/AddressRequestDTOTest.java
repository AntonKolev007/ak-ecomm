package com.ecommerce.project.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddressRequestDTOTest {

    @Test
    public void testDefaultConstructor() {
        AddressRequestDTO dto = new AddressRequestDTO();
        assertNull(dto.getAddressId());
        assertNull(dto.getStreet());
        assertNull(dto.getBuildingName());
        assertNull(dto.getCity());
        assertNull(dto.getState());
        assertNull(dto.getCountry());
        assertNull(dto.getZipCode());
    }

    @Test
    public void testParameterizedConstructor() {
        AddressRequestDTO dto = new AddressRequestDTO(1L, "123 Elm Street", "Building A", "Gotham", "NY", "USA", "10001");
        assertEquals(1L, dto.getAddressId());
        assertEquals("123 Elm Street", dto.getStreet());
        assertEquals("Building A", dto.getBuildingName());
        assertEquals("Gotham", dto.getCity());
        assertEquals("NY", dto.getState());
        assertEquals("USA", dto.getCountry());
        assertEquals("10001", dto.getZipCode());
    }

    @Test
    public void testGettersAndSetters() {
        AddressRequestDTO dto = new AddressRequestDTO();

        dto.setAddressId(1L);
        assertEquals(1L, dto.getAddressId());

        dto.setStreet("123 Elm Street");
        assertEquals("123 Elm Street", dto.getStreet());

        dto.setBuildingName("Building A");
        assertEquals("Building A", dto.getBuildingName());

        dto.setCity("Gotham");
        assertEquals("Gotham", dto.getCity());

        dto.setState("NY");
        assertEquals("NY", dto.getState());

        dto.setCountry("USA");
        assertEquals("USA", dto.getCountry());

        dto.setZipCode("10001");
        assertEquals("10001", dto.getZipCode());
    }

    @Test
    public void testSerialization() throws JsonProcessingException {
        AddressRequestDTO dto = new AddressRequestDTO(1L, "123 Elm Street", "Building A", "Gotham", "NY", "USA", "10001");
        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(dto);
        assertTrue(json.contains("\"street\":\"123 Elm Street\""), "JSON Serialization failed to include the street");
        assertTrue(json.contains("\"addressId\":1"), "JSON Serialization failed to include the addressId");
    }

    @Test
    public void testDeserialization() throws JsonProcessingException {
        String json = "{\"addressId\":1,\"street\":\"123 Elm Street\",\"buildingName\":\"Building A\",\"city\":\"Gotham\",\"state\":\"NY\",\"country\":\"USA\",\"zipCode\":\"10001\"}";
        ObjectMapper mapper = new ObjectMapper();

        AddressRequestDTO dto = mapper.readValue(json, AddressRequestDTO.class);
        assertEquals(1L, dto.getAddressId());
        assertEquals("123 Elm Street", dto.getStreet());
        assertEquals("Building A", dto.getBuildingName());
        assertEquals("Gotham", dto.getCity());
        assertEquals("NY", dto.getState());
        assertEquals("USA", dto.getCountry());
        assertEquals("10001", dto.getZipCode());
    }
}
