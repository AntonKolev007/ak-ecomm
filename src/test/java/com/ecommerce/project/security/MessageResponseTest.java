package com.ecommerce.project.security;

import com.ecommerce.project.security.response.MessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class MessageResponseTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testConstructor() {
        MessageResponse messageResponse = new MessageResponse("Test message");
        assertEquals("Test message", messageResponse.getMessage());
    }

    @Test
    public void testGettersAndSetters() {
        MessageResponse messageResponse = new MessageResponse("Initial message");
        messageResponse.setMessage("Updated message");

        assertEquals("Updated message", messageResponse.getMessage());
    }

    @Test
    public void testJsonSerialization() throws Exception {
        MessageResponse messageResponse = new MessageResponse("Test message");

        String json = objectMapper.writeValueAsString(messageResponse);

        assertTrue(json.contains("\"message\":\"Test message\""));
    }

//    @Test
//    public void testJsonDeserialization() throws Exception {
//        String json = "{\"message\":\"Test message\"}";
//
//        MessageResponse messageResponse = objectMapper.readValue(json, MessageResponse.class);
//
//        assertEquals("Test message", messageResponse.getMessage());
//    }

//    @Test
//    public void testJavaSerialization() throws IOException, ClassNotFoundException {
//        MessageResponse messageResponse = new MessageResponse("Test message");
//
//        // Serialize to a byte array
//        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
//        ObjectOutputStream out = new ObjectOutputStream(byteOut);
//        out.writeObject(messageResponse);
//
//        // Deserialize from the byte array
//        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
//        ObjectInputStream in = new ObjectInputStream(byteIn);
//        MessageResponse deserializedMessageResponse = (MessageResponse) in.readObject();
//
//        assertEquals("Test message", deserializedMessageResponse.getMessage());
//    }
}
