package com.ecommerce.project.payload;

import com.ecommerce.project.payload.request.ProductRequestDTO;
import com.ecommerce.project.payload.response.ProductResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductResponseDTOTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDefaultConstructor() {
        ProductResponseDTO dto = new ProductResponseDTO();
        assertTrue(dto.getContent().isEmpty(), "content should be null");
        assertNull(dto.getPageNumber(), "pageNumber should be null");
        assertNull(dto.getPageSize(), "pageSize should be null");
        assertNull(dto.getTotalElements(), "totalElements should be null");
        assertNull(dto.getTotalPages(), "totalPages should be null");
        assertFalse(dto.isLastPage(), "lastPage should be false");
    }

    @Test
    public void testParameterizedConstructor() {
        List<ProductRequestDTO> content = new ArrayList<>();
        content.add(new ProductRequestDTO(1L, "Product1", "image1", "description1", 10, 100.0, 10.0, 90.0));

        ProductResponseDTO dto = new ProductResponseDTO(content, 1, 10, 100L, 10, true);

        assertEquals(content, dto.getContent(), "content should match");
        assertEquals(1, dto.getPageNumber(), "pageNumber should be 1");
        assertEquals(10, dto.getPageSize(), "pageSize should be 10");
        assertEquals(100L, dto.getTotalElements(), "totalElements should be 100L");
        assertEquals(10, dto.getTotalPages(), "totalPages should be 10");
        assertTrue(dto.isLastPage(), "lastPage should be true");
    }

    @Test
    public void testGettersAndSetters() {
        List<ProductRequestDTO> content = new ArrayList<>();
        content.add(new ProductRequestDTO(1L, "Product1", "image1", "description1", 10, 100.0, 10.0, 90.0));

        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setContent(content);
        assertEquals(content, dto.getContent(), "content should match");

        dto.setPageNumber(1);
        assertEquals(1, dto.getPageNumber(), "pageNumber should be 1");

        dto.setPageSize(10);
        assertEquals(10, dto.getPageSize(), "pageSize should be 10");

        dto.setTotalElements(100L);
        assertEquals(100L, dto.getTotalElements(), "totalElements should be 100L");

        dto.setTotalPages(10);
        assertEquals(10, dto.getTotalPages(), "totalPages should be 10");

        dto.setLastPage(true);
        assertTrue(dto.isLastPage(), "lastPage should be true");
    }

    @Test
    public void testJsonSerialization() throws JsonProcessingException {
        List<ProductRequestDTO> content = new ArrayList<>();
        content.add(new ProductRequestDTO(1L, "Product1", "image1", "description1", 10, 100.0, 10.0, 90.0));
        ProductResponseDTO dto = new ProductResponseDTO(content, 1, 10, 100L, 10, true);

        String json = mapper.writeValueAsString(dto);

        assertTrue(json.contains("\"content\":[{\"productId\":1,\"productName\":\"Product1\",\"image\":\"image1\",\"description\":\"description1\",\"quantity\":10,\"price\":100.0,\"discount\":10.0,\"specialPrice\":90.0}]"), "JSON should contain the content");
        assertTrue(json.contains("\"pageNumber\":1"), "JSON should contain the pageNumber");
        assertTrue(json.contains("\"pageSize\":10"), "JSON should contain the pageSize");
        assertTrue(json.contains("\"totalElements\":100"), "JSON should contain the totalElements");
        assertTrue(json.contains("\"totalPages\":10"), "JSON should contain the totalPages");
        assertTrue(json.contains("\"lastPage\":true"), "JSON should contain the lastPage");
    }

    @Test
    public void testJsonDeserialization() throws JsonProcessingException {
        String json = "{\"content\":[{\"productId\":1,\"productName\":\"Product1\",\"image\":\"image1\",\"description\":\"description1\",\"quantity\":10,\"price\":100.0,\"discount\":10.0,\"specialPrice\":90.0}],\"pageNumber\":1,\"pageSize\":10,\"totalElements\":100,\"totalPages\":10,\"lastPage\":true}";

        ProductResponseDTO dto = mapper.readValue(json, ProductResponseDTO.class);

        assertNotNull(dto.getContent(), "content should not be null");
        assertEquals(1L, dto.getContent().get(0).getProductId(), "productId should be 1");
        assertEquals("Product1", dto.getContent().get(0).getProductName(), "productName should be 'Product1'");
        assertEquals("image1", dto.getContent().get(0).getImage(), "image should be 'image1'");
        assertEquals("description1", dto.getContent().get(0).getDescription(), "description should be 'description1'");
        assertEquals(10, dto.getContent().get(0).getQuantity(), "quantity should be 10");
        assertEquals(100.0, dto.getContent().get(0).getPrice(), 0.01, "price should be 100.0");
        assertEquals(10.0, dto.getContent().get(0).getDiscount(), 0.01, "discount should be 10.0");
        assertEquals(90.0, dto.getContent().get(0).getSpecialPrice(), 0.01, "specialPrice should be 90.0");

        assertEquals(1, dto.getPageNumber(), "pageNumber should be 1");
        assertEquals(10, dto.getPageSize(), "pageSize should be 10");
        assertEquals(100L, dto.getTotalElements(), "totalElements should be 100L");
        assertEquals(10, dto.getTotalPages(), "totalPages should be 10");
        assertTrue(dto.isLastPage(), "lastPage should be true");
    }

    @Test
    public void testJavaSerialization() throws IOException, ClassNotFoundException {
        List<ProductRequestDTO> content = new ArrayList<>();
        content.add(new ProductRequestDTO(1L, "Product1", "image1", "description1", 10, 100.0, 10.0, 90.0));
        ProductResponseDTO dto = new ProductResponseDTO(content, 1, 10, 100L, 10, true);

        // Serialize to a byte array
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(dto);

        // Deserialize from the byte array
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        ProductResponseDTO deserializedDTO = (ProductResponseDTO) in.readObject();

        assertEquals(dto.getContent(), deserializedDTO.getContent(), "content should match");
        assertEquals(dto.getPageNumber(), deserializedDTO.getPageNumber(), "pageNumber should match");
        assertEquals(dto.getPageSize(), deserializedDTO.getPageSize(), "pageSize should match");
        assertEquals(dto.getTotalElements(), deserializedDTO.getTotalElements(), "totalElements should match");
        assertEquals(dto.getTotalPages(), deserializedDTO.getTotalPages(), "totalPages should match");
        assertEquals(dto.isLastPage(), deserializedDTO.isLastPage(), "lastPage should match");
    }
}
