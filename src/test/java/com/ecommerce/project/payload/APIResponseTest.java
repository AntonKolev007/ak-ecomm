package com.ecommerce.project.payload;

import com.ecommerce.project.payload.response.APIResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class APIResponseTest {

    @Test
    public void testDefaultConstructor() {
        APIResponse response = new APIResponse();
        assertNull(response.getMessage());
        assertFalse(response.isStatus());
    }

    @Test
    public void testParameterizedConstructor() {
        APIResponse response = new APIResponse("Success", true);
        assertEquals("Success", response.getMessage());
        assertTrue(response.isStatus());
    }

    @Test
    public void testGettersAndSetters() {
        APIResponse response = new APIResponse();

        response.setMessage("Error");
        assertEquals("Error", response.getMessage());

        response.setStatus(false);
        assertFalse(response.isStatus());

        response.setStatus(true);
        assertTrue(response.isStatus());
    }

    @Test
    public void testSerialization() throws JsonProcessingException {
        APIResponse response = new APIResponse("Success", true);
        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(response);
        assertTrue(json.contains("\"message\":\"Success\""), "JSON Serialization failed to include the message");
        assertTrue(json.contains("\"status\":true"), "JSON Serialization failed to include the status");
    }

    @Test
    public void testDeserialization() throws JsonProcessingException {
        String json = "{\"message\":\"Success\",\"status\":true}";
        ObjectMapper mapper = new ObjectMapper();

        APIResponse response = mapper.readValue(json, APIResponse.class);
        assertEquals("Success", response.getMessage());
        assertTrue(response.isStatus());
    }

    @Test
    public void testSerializable() throws IOException, ClassNotFoundException {
        APIResponse response = new APIResponse("Success", true);

        // Serialize to a byte array
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(response);

        // Deserialize from the byte array
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        APIResponse deserializedResponse = (APIResponse) in.readObject();

        assertEquals(response.getMessage(), deserializedResponse.getMessage());
        assertEquals(response.isStatus(), deserializedResponse.isStatus());
    }
}
