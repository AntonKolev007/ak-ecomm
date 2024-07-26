package com.ecommerce.project.security;

import com.ecommerce.project.security.response.UserInfoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserInfoResponseTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testConstructors() {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");

        UserInfoResponse userInfoResponse1 = new UserInfoResponse(1L, "testuser", roles, "token");
        assertEquals(1L, userInfoResponse1.getId());
        assertEquals("testuser", userInfoResponse1.getUsername());
        assertEquals(roles, userInfoResponse1.getRoles());
        assertEquals("token", userInfoResponse1.getJwtToken());

        UserInfoResponse userInfoResponse2 = new UserInfoResponse(2L, "anotheruser", roles);
        assertEquals(2L, userInfoResponse2.getId());
        assertEquals("anotheruser", userInfoResponse2.getUsername());
        assertEquals(roles, userInfoResponse2.getRoles());
        assertNull(userInfoResponse2.getJwtToken());
    }

    @Test
    public void testGettersAndSetters() {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");

        UserInfoResponse userInfoResponse = new UserInfoResponse(1L, "testuser", roles);
        userInfoResponse.setJwtToken("token");

        assertEquals(1L, userInfoResponse.getId());
        assertEquals("testuser", userInfoResponse.getUsername());
        assertEquals(roles, userInfoResponse.getRoles());
        assertEquals("token", userInfoResponse.getJwtToken());

        userInfoResponse.setId(2L);
        userInfoResponse.setUsername("anotheruser");
        userInfoResponse.setRoles(new ArrayList<>());
        userInfoResponse.setJwtToken("newtoken");

        assertEquals(2L, userInfoResponse.getId());
        assertEquals("anotheruser", userInfoResponse.getUsername());
        assertTrue(userInfoResponse.getRoles().isEmpty());
        assertEquals("newtoken", userInfoResponse.getJwtToken());
    }

    @Test
    public void testJsonSerialization() throws Exception {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");

        UserInfoResponse userInfoResponse = new UserInfoResponse(1L, "testuser", roles, "token");

        String json = objectMapper.writeValueAsString(userInfoResponse);

        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"username\":\"testuser\""));
        assertTrue(json.contains("\"roles\":[\"ROLE_USER\"]"));
        assertTrue(json.contains("\"jwtToken\":\"token\""));
    }

//    @Test
//    public void testJsonDeserialization() throws Exception {
//        String json = "{\"id\":1,\"username\":\"testuser\",\"roles\":[\"ROLE_USER\"],\"jwtToken\":\"token\"}";
//
//        UserInfoResponse userInfoResponse = objectMapper.readValue(json, UserInfoResponse.class);
//
//        assertEquals(1L, userInfoResponse.getId());
//        assertEquals("testuser", userInfoResponse.getUsername());
//        assertTrue(userInfoResponse.getRoles().contains("ROLE_USER"));
//        assertEquals("token", userInfoResponse.getJwtToken());
//    }

//    @Test
//    public void testJavaSerialization() throws IOException, ClassNotFoundException {
//        List<String> roles = new ArrayList<>();
//        roles.add("ROLE_USER");
//
//        UserInfoResponse userInfoResponse = new UserInfoResponse(1L, "testuser", roles, "token");
//
//        // Serialize to a byte array
//        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
//        ObjectOutputStream out = new ObjectOutputStream(byteOut);
//        out.writeObject(userInfoResponse);
//
//        // Deserialize from the byte array
//        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
//        ObjectInputStream in = new ObjectInputStream(byteIn);
//        UserInfoResponse deserializedUserInfoResponse = (UserInfoResponse) in.readObject();
//
//        assertEquals(1L, deserializedUserInfoResponse.getId());
//        assertEquals("testuser", deserializedUserInfoResponse.getUsername());
//        assertTrue(deserializedUserInfoResponse.getRoles().contains("ROLE_USER"));
//        assertEquals("token", deserializedUserInfoResponse.getJwtToken());
//    }
}
