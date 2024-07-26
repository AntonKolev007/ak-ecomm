package com.ecommerce.project.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class ProductRequestDTOTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDefaultConstructor() {
        ProductRequestDTO dto = new ProductRequestDTO();
        assertNull(dto.getProductId(), "productId should be null");
        assertNull(dto.getProductName(), "productName should be null");
        assertNull(dto.getImage(), "image should be null");
        assertNull(dto.getDescription(), "description should be null");
        assertNull(dto.getQuantity(), "quantity should be null");
        assertEquals(0.0, dto.getPrice(), 0.01, "price should be 0.0");
        assertEquals(0.0, dto.getDiscount(), 0.01, "discount should be 0.0");
        assertEquals(0.0, dto.getSpecialPrice(), 0.01, "specialPrice should be 0.0");
    }

    @Test
    public void testParameterizedConstructor() {
        ProductRequestDTO dto = new ProductRequestDTO(1L, "Product1", "image1", "description1", 10, 100.0, 10.0, 90.0);

        assertEquals(1L, dto.getProductId(), "productId should be 1");
        assertEquals("Product1", dto.getProductName(), "productName should be 'Product1'");
        assertEquals("image1", dto.getImage(), "image should be 'image1'");
        assertEquals("description1", dto.getDescription(), "description should be 'description1'");
        assertEquals(10, dto.getQuantity(), "quantity should be 10");
        assertEquals(100.0, dto.getPrice(), 0.01, "price should be 100.0");
        assertEquals(10.0, dto.getDiscount(), 0.01, "discount should be 10.0");
        assertEquals(90.0, dto.getSpecialPrice(), 0.01, "specialPrice should be 90.0");
    }

    @Test
    public void testGettersAndSetters() {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setProductId(1L);
        assertEquals(1L, dto.getProductId(), "productId should be 1");

        dto.setProductName("Product1");
        assertEquals("Product1", dto.getProductName(), "productName should be 'Product1'");

        dto.setImage("image1");
        assertEquals("image1", dto.getImage(), "image should be 'image1'");

        dto.setDescription("description1");
        assertEquals("description1", dto.getDescription(), "description should be 'description1'");

        dto.setQuantity(10);
        assertEquals(10, dto.getQuantity(), "quantity should be 10");

        dto.setPrice(100.0);
        assertEquals(100.0, dto.getPrice(), 0.01, "price should be 100.0");

        dto.setDiscount(10.0);
        assertEquals(10.0, dto.getDiscount(), 0.01, "discount should be 10.0");

        dto.setSpecialPrice(90.0);
        assertEquals(90.0, dto.getSpecialPrice(), 0.01, "specialPrice should be 90.0");
    }

    @Test
    public void testJsonSerialization() throws JsonProcessingException {
        ProductRequestDTO dto = new ProductRequestDTO(1L, "Product1", "image1", "description1", 10, 100.0, 10.0, 90.0);

        String json = mapper.writeValueAsString(dto);

        assertTrue(json.contains("\"productId\":1"), "JSON should contain the productId");
        assertTrue(json.contains("\"productName\":\"Product1\""), "JSON should contain the productName");
        assertTrue(json.contains("\"image\":\"image1\""), "JSON should contain the image");
        assertTrue(json.contains("\"description\":\"description1\""), "JSON should contain the description");
        assertTrue(json.contains("\"quantity\":10"), "JSON should contain the quantity");
        assertTrue(json.contains("\"price\":100.0"), "JSON should contain the price");
        assertTrue(json.contains("\"discount\":10.0"), "JSON should contain the discount");
        assertTrue(json.contains("\"specialPrice\":90.0"), "JSON should contain the specialPrice");
    }

    @Test
    public void testJsonDeserialization() throws JsonProcessingException {
        String json = "{\"productId\":1,\"productName\":\"Product1\",\"image\":\"image1\",\"description\":\"description1\",\"quantity\":10,\"price\":100.0,\"discount\":10.0,\"specialPrice\":90.0}";

        ProductRequestDTO dto = mapper.readValue(json, ProductRequestDTO.class);

        assertEquals(1L, dto.getProductId(), "productId should be 1");
        assertEquals("Product1", dto.getProductName(), "productName should be 'Product1'");
        assertEquals("image1", dto.getImage(), "image should be 'image1'");
        assertEquals("description1", dto.getDescription(), "description should be 'description1'");
        assertEquals(10, dto.getQuantity(), "quantity should be 10");
        assertEquals(100.0, dto.getPrice(), 0.01, "price should be 100.0");
        assertEquals(10.0, dto.getDiscount(), 0.01, "discount should be 10.0");
        assertEquals(90.0, dto.getSpecialPrice(), 0.01, "specialPrice should be 90.0");
    }

    @Test
    public void testJavaSerialization() throws IOException, ClassNotFoundException {
        ProductRequestDTO dto = new ProductRequestDTO(1L, "Product1", "image1", "description1", 10, 100.0, 10.0, 90.0);

        // Serialize to a byte array
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(dto);

        // Deserialize from the byte array
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        ProductRequestDTO deserializedDTO = (ProductRequestDTO) in.readObject();

        assertEquals(dto.getProductId(), deserializedDTO.getProductId(), "productId should match");
        assertEquals(dto.getProductName(), deserializedDTO.getProductName(), "productName should match");
        assertEquals(dto.getImage(), deserializedDTO.getImage(), "image should match");
        assertEquals(dto.getDescription(), deserializedDTO.getDescription(), "description should match");
        assertEquals(dto.getQuantity(), deserializedDTO.getQuantity(), "quantity should match");
        assertEquals(dto.getPrice(), deserializedDTO.getPrice(), 0.01, "price should match");
        assertEquals(dto.getDiscount(), deserializedDTO.getDiscount(), 0.01, "discount should match");
        assertEquals(dto.getSpecialPrice(), deserializedDTO.getSpecialPrice(), 0.01, "specialPrice should match");
    }
}
