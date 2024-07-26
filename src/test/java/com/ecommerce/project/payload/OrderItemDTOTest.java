package com.ecommerce.project.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class OrderItemDTOTest {

    @Test
    public void testDefaultConstructor() {
        OrderItemDTO dto = new OrderItemDTO();
        assertNull(dto.getOrderItemId(), "orderItemId should be null");
        assertNull(dto.getProduct(), "product should be null");
        assertNull(dto.getQuantity(), "quantity should be null");
        assertEquals(0.0, dto.getDiscount(), "discount should be 0.0");
        assertEquals(0.0, dto.getOrderedProductPrice(), "orderedProductPrice should be 0.0");
    }

    @Test
    public void testParameterizedConstructor() {
        ProductRequestDTO product = new ProductRequestDTO();
        OrderItemDTO dto = new OrderItemDTO(1L, product, 2, 10.0, 100.0);

        assertEquals(1L, dto.getOrderItemId(), "orderItemId should be 1");
        assertEquals(product, dto.getProduct(), "product should match");
        assertEquals(2, dto.getQuantity(), "quantity should be 2");
        assertEquals(10.0, dto.getDiscount(), "discount should be 10.0");
        assertEquals(100.0, dto.getOrderedProductPrice(), "orderedProductPrice should be 100.0");
    }

    @Test
    public void testGettersAndSetters() {
        OrderItemDTO dto = new OrderItemDTO();
        ProductRequestDTO product = new ProductRequestDTO();

        dto.setOrderItemId(1L);
        assertEquals(1L, dto.getOrderItemId(), "orderItemId should be 1");

        dto.setProduct(product);
        assertEquals(product, dto.getProduct(), "product should match");

        dto.setQuantity(2);
        assertEquals(2, dto.getQuantity(), "quantity should be 2");

        dto.setDiscount(10.0);
        assertEquals(10.0, dto.getDiscount(), "discount should be 10.0");

        dto.setOrderedProductPrice(100.0);
        assertEquals(100.0, dto.getOrderedProductPrice(), "orderedProductPrice should be 100.0");
    }

    @Test
    public void testJsonSerialization() throws JsonProcessingException {
        ProductRequestDTO product = new ProductRequestDTO();
        OrderItemDTO dto = new OrderItemDTO(1L, product, 2, 10.0, 100.0);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dto);

        assertTrue(json.contains("\"orderItemId\":1"), "JSON should contain the orderItemId");
       // assertTrue(json.contains("\"product\":{}"), "JSON should contain the product");
        assertTrue(json.contains("\"quantity\":2"), "JSON should contain the quantity");
        assertTrue(json.contains("\"discount\":10.0"), "JSON should contain the discount");
        assertTrue(json.contains("\"orderedProductPrice\":100.0"), "JSON should contain the orderedProductPrice");
    }

    @Test
    public void testJsonDeserialization() throws JsonProcessingException {
        String json = "{\"orderItemId\":1,\"product\":{},\"quantity\":2,\"discount\":10.0,\"orderedProductPrice\":100.0}";
        ObjectMapper mapper = new ObjectMapper();

        OrderItemDTO dto = mapper.readValue(json, OrderItemDTO.class);

        assertEquals(1L, dto.getOrderItemId(), "orderItemId should be 1");
        assertNotNull(dto.getProduct(), "product should not be null");
        assertEquals(2, dto.getQuantity(), "quantity should be 2");
        assertEquals(10.0, dto.getDiscount(), "discount should be 10.0");
        assertEquals(100.0, dto.getOrderedProductPrice(), "orderedProductPrice should be 100.0");
    }

    @Test
    public void testJavaSerialization() throws IOException, ClassNotFoundException {
        ProductRequestDTO product = new ProductRequestDTO();
        OrderItemDTO dto = new OrderItemDTO(1L, product, 2, 10.0, 100.0);

        // Serialize to a byte array
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(dto);

        // Deserialize from the byte array
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        OrderItemDTO deserializedDTO = (OrderItemDTO) in.readObject();

        assertEquals(dto.getOrderItemId(), deserializedDTO.getOrderItemId(), "orderItemId should match");
        assertEquals(dto.getProduct(), deserializedDTO.getProduct(), "product should match");
        assertEquals(dto.getQuantity(), deserializedDTO.getQuantity(), "quantity should match");
        assertEquals(dto.getDiscount(), deserializedDTO.getDiscount(), "discount should match");
        assertEquals(dto.getOrderedProductPrice(), deserializedDTO.getOrderedProductPrice(), "orderedProductPrice should match");
    }
}
