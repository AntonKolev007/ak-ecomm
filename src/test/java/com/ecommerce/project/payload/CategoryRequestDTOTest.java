package com.ecommerce.project.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryRequestDTOTest {

    @Test
    public void testDefaultConstructor() {
        CategoryRequestDTO dto = new CategoryRequestDTO();
        assertNull(dto.getCategoryId(), "categoryId should be null");
        assertNull(dto.getCategoryName(), "categoryName should be null");
    }

    @Test
    public void testParameterizedConstructor() {
        CategoryRequestDTO dto = new CategoryRequestDTO(1L, "Electronics");

        assertEquals(1L, dto.getCategoryId(), "categoryId should be 1");
        assertEquals("Electronics", dto.getCategoryName(), "categoryName should be 'Electronics'");
    }

    @Test
    public void testGettersAndSetters() {
        CategoryRequestDTO dto = new CategoryRequestDTO();

        dto.setCategoryId(1L);
        assertEquals(1L, dto.getCategoryId(), "categoryId should be 1");

        dto.setCategoryName("Books");
        assertEquals("Books", dto.getCategoryName(), "categoryName should be 'Books'");
    }

    @Test
    public void testJsonSerialization() throws JsonProcessingException {
        CategoryRequestDTO dto = new CategoryRequestDTO(1L, "Electronics");
        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(dto);
        assertTrue(json.contains("\"categoryId\":1"), "JSON should contain 'categoryId:1'");
        assertTrue(json.contains("\"categoryName\":\"Electronics\""), "JSON should contain 'categoryName:Electronics'");
    }

    @Test
    public void testJsonDeserialization() throws JsonProcessingException {
        String json = "{\"categoryId\":1,\"categoryName\":\"Electronics\"}";
        ObjectMapper mapper = new ObjectMapper();

        CategoryRequestDTO dto = mapper.readValue(json, CategoryRequestDTO.class);
        assertEquals(1L, dto.getCategoryId(), "categoryId should be 1");
        assertEquals("Electronics", dto.getCategoryName(), "categoryName should be 'Electronics'");
    }

    @Test
    public void testJavaSerialization() throws IOException, ClassNotFoundException {
        CategoryRequestDTO dto = new CategoryRequestDTO(1L, "Electronics");

        // Serialize to a byte array
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(dto);

        // Deserialize from the byte array
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        CategoryRequestDTO deserializedDTO = (CategoryRequestDTO) in.readObject();

        assertEquals(dto.getCategoryId(), deserializedDTO.getCategoryId(), "categoryId should match");
        assertEquals(dto.getCategoryName(), deserializedDTO.getCategoryName(), "categoryName should match");
    }
}
