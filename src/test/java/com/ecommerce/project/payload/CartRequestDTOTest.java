package com.ecommerce.project.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CartRequestDTOTest {

    @Test
    public void testDefaultConstructor() {
        CartRequestDTO dto = new CartRequestDTO();
        assertNull(dto.getCartId());
        assertEquals(0.0, dto.getTotalPrice());
        assertNotNull(dto.getProducts());
        assertTrue(dto.getProducts().isEmpty());
    }

    @Test
    public void testParameterizedConstructor() {
        List<ProductRequestDTO> products = new ArrayList<>();
        products.add(new ProductRequestDTO());
        CartRequestDTO dto = new CartRequestDTO(1L, 100.0, products);

        assertEquals(1L, dto.getCartId());
        assertEquals(100.0, dto.getTotalPrice());
        assertEquals(products, dto.getProducts());
    }

    @Test
    public void testGettersAndSetters() {
        CartRequestDTO dto = new CartRequestDTO();
        List<ProductRequestDTO> products = new ArrayList<>();
        products.add(new ProductRequestDTO());

        dto.setCartId(1L);
        assertEquals(1L, dto.getCartId());

        dto.setTotalPrice(200.0);
        assertEquals(200.0, dto.getTotalPrice());

        dto.setProducts(products);
        assertEquals(products, dto.getProducts());
        assertEquals(1, dto.getProducts().size());
    }

    @Test
    public void testAddToProducts() {
        CartRequestDTO dto = new CartRequestDTO();
        ProductRequestDTO product = new ProductRequestDTO();
        dto.getProducts().add(product);

        assertNotNull(dto.getProducts());
        assertEquals(1, dto.getProducts().size());
        assertEquals(product, dto.getProducts().get(0));
    }

    @Test
    public void testRemoveFromProducts() {
        CartRequestDTO dto = new CartRequestDTO();
        ProductRequestDTO product = new ProductRequestDTO();
        dto.getProducts().add(product);
        dto.getProducts().remove(product);

        assertNotNull(dto.getProducts());
        assertTrue(dto.getProducts().isEmpty());
    }

    @Test
    public void testClearProducts() {
        CartRequestDTO dto = new CartRequestDTO();
        ProductRequestDTO product = new ProductRequestDTO();
        dto.getProducts().add(product);
        dto.getProducts().clear();

        assertNotNull(dto.getProducts());
        assertTrue(dto.getProducts().isEmpty());
    }

    @Test
    public void testSerialization() throws JsonProcessingException {
        List<ProductRequestDTO> products = new ArrayList<>();
        products.add(new ProductRequestDTO());
        CartRequestDTO dto = new CartRequestDTO(1L, 100.0, products);
        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(dto);
        assertTrue(json.contains("\"cartId\":1"), "JSON Serialization failed to include the cartId");
        assertTrue(json.contains("\"totalPrice\":100.0"), "JSON Serialization failed to include the totalPrice");
    }

    @Test
    public void testDeserialization() throws JsonProcessingException {
        String json = "{\"cartId\":1,\"totalPrice\":100.0,\"products\":[{}]}";
        ObjectMapper mapper = new ObjectMapper();

        CartRequestDTO dto = mapper.readValue(json, CartRequestDTO.class);
        assertEquals(1L, dto.getCartId());
        assertEquals(100.0, dto.getTotalPrice());
        assertNotNull(dto.getProducts());
        assertEquals(1, dto.getProducts().size());
    }

    @Test
    public void testSerializable() throws IOException, ClassNotFoundException {
        List<ProductRequestDTO> products = new ArrayList<>();
        products.add(new ProductRequestDTO());
        CartRequestDTO dto = new CartRequestDTO(1L, 100.0, products);

        // Serialize to a byte array
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(dto);

        // Deserialize from the byte array
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        CartRequestDTO deserializedDTO = (CartRequestDTO) in.readObject();

        assertEquals(dto.getCartId(), deserializedDTO.getCartId());
        assertEquals(dto.getTotalPrice(), deserializedDTO.getTotalPrice());
        assertNotNull(deserializedDTO.getProducts());
        assertEquals(dto.getProducts().size(), deserializedDTO.getProducts().size());
    }
}
