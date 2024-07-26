package com.ecommerce.project.security;

import com.ecommerce.project.security.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class LoginRequestTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGettersAndSetters() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("testpassword");

        assertEquals("testuser", loginRequest.getUsername());
        assertEquals("testpassword", loginRequest.getPassword());
    }

    @Test
    public void testJsonSerialization() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("testpassword");

        String json = objectMapper.writeValueAsString(loginRequest);
        assertTrue(json.contains("\"username\":\"testuser\""));
        assertTrue(json.contains("\"password\":\"testpassword\""));
    }

    @Test
    public void testJsonDeserialization() throws Exception {
        String json = "{\"username\":\"testuser\",\"password\":\"testpassword\"}";

        LoginRequest loginRequest = objectMapper.readValue(json, LoginRequest.class);

        assertEquals("testuser", loginRequest.getUsername());
        assertEquals("testpassword", loginRequest.getPassword());
    }

//    @Test
//    public void testJavaSerialization() throws IOException, ClassNotFoundException {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setUsername("testuser");
//        loginRequest.setPassword("testpassword");
//
//        // Serialize to a byte array
//        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
//        ObjectOutputStream out = new ObjectOutputStream(byteOut);
//        out.writeObject(loginRequest);
//
//        // Deserialize from the byte array
//        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
//        ObjectInputStream in = new ObjectInputStream(byteIn);
//        LoginRequest deserializedLoginRequest = (LoginRequest) in.readObject();
//
//        assertEquals("testuser", deserializedLoginRequest.getUsername());
//        assertEquals("testpassword", deserializedLoginRequest.getPassword());
//    }
}
