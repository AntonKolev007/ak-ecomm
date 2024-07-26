package com.ecommerce.project.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class CartItemsRequestDTOTest {

    @Test
    public void testDefaultConstructor() {
        CartItemsRequestDTO dto = new CartItemsRequestDTO();
        assertNull(dto.getCartItemId());
        assertNull(dto.getCart());
        assertNull(dto.getProduct());
        assertNull(dto.getQuantity());
        assertNull(dto.getDiscount());
        assertNull(dto.getProductPrice());
    }

    @Test
    public void testParameterizedConstructor() {
        CartRequestDTO cart = new CartRequestDTO();
        ProductRequestDTO product = new ProductRequestDTO();
        CartItemsRequestDTO dto = new CartItemsRequestDTO(1L, cart, product, 5, 10.0, 150.0);

        assertEquals(1L, dto.getCartItemId());
        assertEquals(cart, dto.getCart());
        assertEquals(product, dto.getProduct());
        assertEquals(5, dto.getQuantity());
        assertEquals(10.0, dto.getDiscount());
        assertEquals(150.0, dto.getProductPrice());
    }

    @Test
    public void testGettersAndSetters() {
        CartItemsRequestDTO dto = new CartItemsRequestDTO();
        CartRequestDTO cart = new CartRequestDTO();
        ProductRequestDTO product = new ProductRequestDTO();

        dto.setCartItemId(1L);
        assertEquals(1L, dto.getCartItemId());

        dto.setCart(cart);
        assertEquals(cart, dto.getCart());

        dto.setProduct(product);
        assertEquals(product, dto.getProduct());

        dto.setQuantity(10);
        assertEquals(10, dto.getQuantity());

        dto.setDiscount(5.0);
        assertEquals(5.0, dto.getDiscount());

        dto.setProductPrice(200.0);
        assertEquals(200.0, dto.getProductPrice());
    }

    @Test
    public void testSerialization() throws JsonProcessingException {
        CartRequestDTO cart = new CartRequestDTO();
        ProductRequestDTO product = new ProductRequestDTO();
        CartItemsRequestDTO dto = new CartItemsRequestDTO(1L, cart, product, 5, 10.0, 150.0);
        ObjectMapper mapper = new ObjectMapper();
        
        String json = mapper.writeValueAsString(dto);
        assertTrue(json.contains("\"cartItemId\":1"), "JSON Serialization failed to include the cartItemId");
        assertTrue(json.contains("\"quantity\":5"), "JSON Serialization failed to include the quantity");
    }

    @Test
    public void testDeserialization() throws JsonProcessingException {
        String json = "{\"cartItemId\":1,\"cart\":{},\"product\":{},\"quantity\":5,\"discount\":10.0,\"productPrice\":150.0}";
        ObjectMapper mapper = new ObjectMapper();
        
        CartItemsRequestDTO dto = mapper.readValue(json, CartItemsRequestDTO.class);
        assertEquals(1L, dto.getCartItemId());
        assertNotNull(dto.getCart());
        assertNotNull(dto.getProduct());
        assertEquals(5, dto.getQuantity());
        assertEquals(10.0, dto.getDiscount());
        assertEquals(150.0, dto.getProductPrice());
    }

    @Test
    public void testSerializable() throws IOException, ClassNotFoundException {
        CartRequestDTO cart = new CartRequestDTO();
        ProductRequestDTO product = new ProductRequestDTO();
        CartItemsRequestDTO dto = new CartItemsRequestDTO(1L, cart, product, 5, 10.0, 150.0);

        // Serialize to a byte array
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(dto);

        // Deserialize from the byte array
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        CartItemsRequestDTO deserializedDTO = (CartItemsRequestDTO) in.readObject();

        assertEquals(dto.getCartItemId(), deserializedDTO.getCartItemId());
        assertNotNull(deserializedDTO.getCart());
        assertNotNull(deserializedDTO.getProduct());
        assertEquals(dto.getQuantity(), deserializedDTO.getQuantity());
        assertEquals(dto.getDiscount(), deserializedDTO.getDiscount());
        assertEquals(dto.getProductPrice(), deserializedDTO.getProductPrice());
    }
}
