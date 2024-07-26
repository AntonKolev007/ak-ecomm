package com.ecommerce.project.security;

import com.ecommerce.project.security.request.SignupRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SignupRequestTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGettersAndSetters() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("testuser");
        signupRequest.setEmail("testuser@example.com");
        signupRequest.setPassword("testpassword");
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        signupRequest.setRole(roles);

        assertEquals("testuser", signupRequest.getUsername());
        assertEquals("testuser@example.com", signupRequest.getEmail());
        assertEquals("testpassword", signupRequest.getPassword());
        assertEquals(roles, signupRequest.getRole());
    }

    @Test
    public void testJsonSerialization() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("testuser");
        signupRequest.setEmail("testuser@example.com");
        signupRequest.setPassword("testpassword");
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        signupRequest.setRole(roles);

        String json = objectMapper.writeValueAsString(signupRequest);

        assertTrue(json.contains("\"username\":\"testuser\""));
        assertTrue(json.contains("\"email\":\"testuser@example.com\""));
        assertTrue(json.contains("\"password\":\"testpassword\""));
        assertTrue(json.contains("\"role\":[\"ROLE_USER\"]"));
    }

    @Test
    public void testJsonDeserialization() throws Exception {
        String json = "{\"username\":\"testuser\",\"email\":\"testuser@example.com\",\"password\":\"testpassword\",\"role\":[\"ROLE_USER\"]}";

        SignupRequest signupRequest = objectMapper.readValue(json, SignupRequest.class);

        assertEquals("testuser", signupRequest.getUsername());
        assertEquals("testuser@example.com", signupRequest.getEmail());
        assertEquals("testpassword", signupRequest.getPassword());
        assertTrue(signupRequest.getRole().contains("ROLE_USER"));
    }

//    @Test
//    public void testJavaSerialization() throws IOException, ClassNotFoundException {
//        SignupRequest signupRequest = new SignupRequest();
//        signupRequest.setUsername("testuser");
//        signupRequest.setEmail("testuser@example.com");
//        signupRequest.setPassword("testpassword");
//        Set<String> roles = new HashSet<>();
//        roles.add("ROLE_USER");
//        signupRequest.setRole(roles);
//
//        // Serialize to a byte array
//        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
//        ObjectOutputStream out = new ObjectOutputStream(byteOut);
//        out.writeObject(signupRequest);
//
//        // Deserialize from the byte array
//        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
//        ObjectInputStream in = new ObjectInputStream(byteIn);
//        SignupRequest deserializedSignupRequest = (SignupRequest) in.readObject();
//
//        assertEquals("testuser", deserializedSignupRequest.getUsername());
//        assertEquals("testuser@example.com", deserializedSignupRequest.getEmail());
//        assertEquals("testpassword", deserializedSignupRequest.getPassword());
//        assertTrue(deserializedSignupRequest.getRole().contains("ROLE_USER"));
//    }
}
