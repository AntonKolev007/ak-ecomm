package com.ecommerce.project.payload;

import com.ecommerce.project.payload.request.CategoryRequestDTO;
import com.ecommerce.project.payload.response.CategoryResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryResponseDTOTest {

    @Test
    public void testDefaultConstructor() {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        assertNull(dto.getContent(), "content should be null");
        assertNull(dto.getPageNumber(), "pageNumber should be null");
        assertNull(dto.getPageSize(), "pageSize should be null");
        assertNull(dto.getTotalElements(), "totalElements should be null");
        assertNull(dto.getTotalPages(), "totalPages should be null");
        assertFalse(dto.isLastPages(), "lastPages should be false");
    }

    @Test
    public void testParameterizedConstructor() {
        List<CategoryRequestDTO> content = new ArrayList<>();
        content.add(new CategoryRequestDTO(1L, "Electronics"));
        CategoryResponseDTO dto = new CategoryResponseDTO(content);

        assertEquals(content, dto.getContent(), "content should match");
    }

    @Test
    public void testGettersAndSetters() {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        List<CategoryRequestDTO> content = new ArrayList<>();
        content.add(new CategoryRequestDTO(1L, "Electronics"));

        dto.setContent(content);
        assertEquals(content, dto.getContent(), "content should match");

        dto.setPageNumber(1);
        assertEquals(1, dto.getPageNumber(), "pageNumber should be 1");

        dto.setPageSize(10);
        assertEquals(10, dto.getPageSize(), "pageSize should be 10");

        dto.setTotalElements(100L);
        assertEquals(100L, dto.getTotalElements(), "totalElements should be 100");

        dto.setTotalPages(10);
        assertEquals(10, dto.getTotalPages(), "totalPages should be 10");

        dto.setLastPages(true);
        assertTrue(dto.isLastPages(), "lastPages should be true");
    }

    @Test
    public void testJsonSerialization() throws JsonProcessingException {
        List<CategoryRequestDTO> content = new ArrayList<>();
        content.add(new CategoryRequestDTO(1L, "Electronics"));
        CategoryResponseDTO dto = new CategoryResponseDTO(content);
        dto.setPageNumber(1);
        dto.setPageSize(10);
        dto.setTotalElements(100L);
        dto.setTotalPages(10);
        dto.setLastPages(true);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dto);

        assertTrue(json.contains("\"content\":[{\"categoryId\":1,\"categoryName\":\"Electronics\"}]"), "JSON should contain the content");
        assertTrue(json.contains("\"pageNumber\":1"), "JSON should contain the pageNumber");
        assertTrue(json.contains("\"pageSize\":10"), "JSON should contain the pageSize");
        assertTrue(json.contains("\"totalElements\":100"), "JSON should contain the totalElements");
        assertTrue(json.contains("\"totalPages\":10"), "JSON should contain the totalPages");
        assertTrue(json.contains("\"lastPages\":true"), "JSON should contain the lastPages");
    }

    @Test
    public void testJsonDeserialization() throws JsonProcessingException {
        String json = "{\"content\":[{\"categoryId\":1,\"categoryName\":\"Electronics\"}],\"pageNumber\":1,\"pageSize\":10,\"totalElements\":100,\"totalPages\":10,\"lastPages\":true}";
        ObjectMapper mapper = new ObjectMapper();

        CategoryResponseDTO dto = mapper.readValue(json, CategoryResponseDTO.class);
        assertNotNull(dto.getContent(), "content should not be null");
        assertEquals(1, dto.getContent().get(0).getCategoryId(), "categoryId should be 1");
        assertEquals("Electronics", dto.getContent().get(0).getCategoryName(), "categoryName should be 'Electronics'");
        assertEquals(1, dto.getPageNumber(), "pageNumber should be 1");
        assertEquals(10, dto.getPageSize(), "pageSize should be 10");
        assertEquals(100L, dto.getTotalElements(), "totalElements should be 100");
        assertEquals(10, dto.getTotalPages(), "totalPages should be 10");
        assertTrue(dto.isLastPages(), "lastPages should be true");
    }

    @Test
    public void testJavaSerialization() throws IOException, ClassNotFoundException {
        List<CategoryRequestDTO> content = new ArrayList<>();
        content.add(new CategoryRequestDTO(1L, "Electronics"));
        CategoryResponseDTO dto = new CategoryResponseDTO(content);
        dto.setPageNumber(1);
        dto.setPageSize(10);
        dto.setTotalElements(100L);
        dto.setTotalPages(10);
        dto.setLastPages(true);

        // Serialize to a byte array
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(dto);

        // Deserialize from the byte array
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        CategoryResponseDTO deserializedDTO = (CategoryResponseDTO) in.readObject();

        assertEquals(dto.getContent().size(), deserializedDTO.getContent().size(), "content size should match");
        assertEquals(dto.getPageNumber(), deserializedDTO.getPageNumber(), "pageNumber should match");
        assertEquals(dto.getPageSize(), deserializedDTO.getPageSize(), "pageSize should match");
        assertEquals(dto.getTotalElements(), deserializedDTO.getTotalElements(), "totalElements should match");
        assertEquals(dto.getTotalPages(), deserializedDTO.getTotalPages(), "totalPages should match");
        assertEquals(dto.isLastPages(), deserializedDTO.isLastPages(), "lastPages should match");
    }
}
