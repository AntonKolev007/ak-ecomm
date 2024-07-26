package com.ecommerce.project.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserBasicNoArgsConstructor() {
        User user = new User();
        assertEquals(0L, user.getUserId()); // Default value for long is 0
        assertNull(user.getUserName());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNotNull(user.getRoles());
        assertTrue(user.getRoles().isEmpty());
        assertNull(user.getProducts()); // Assuming products is initially null
        assertNotNull(user.getAddresses());
        assertTrue(user.getAddresses().isEmpty());
        assertNull(user.getCart());
    }

    @Test
    public void testUserConstructorForSignup() {
        User user = new User("john_doe", "john.doe@example.com", "password123");

        assertEquals("john_doe", user.getUserName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals(0L, user.getUserId());
        assertNotNull(user.getRoles());
        assertTrue(user.getRoles().isEmpty());
        assertNull(user.getProducts());
        assertNotNull(user.getAddresses());
        assertTrue(user.getAddresses().isEmpty());
        assertNull(user.getCart());
    }

    @Test
    public void testUserParameterizedConstructor() {
        Set<Role> roles = new HashSet<>();
        Set<Product> products = new HashSet<>();
        Cart cart = new Cart();
        List<Address> addresses = new ArrayList<>();

        User user = new User(1L, "john_doe", "john.doe@example.com", "password123", roles, products, cart);
        user.setAddresses(addresses);

        assertEquals(1L, user.getUserId());
        assertEquals("john_doe", user.getUserName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals(roles, user.getRoles());
        assertEquals(products, user.getProducts());
        assertEquals(addresses, user.getAddresses());
        assertEquals(cart, user.getCart());
    }

    @Test
    public void testSettersAndGetters() {
        User user = new User();

        user.setUserId(1L);
        assertEquals(1L, user.getUserId());

        user.setUserName("john_doe");
        assertEquals("john_doe", user.getUserName());

        user.setEmail("john.doe@example.com");
        assertEquals("john.doe@example.com", user.getEmail());

        user.setPassword("password123");
        assertEquals("password123", user.getPassword());

        Set<Role> roles = new HashSet<>();
        user.setRoles(roles);
        assertEquals(roles, user.getRoles());

        Set<Product> products = new HashSet<>();
        user.setProducts(products);
        assertEquals(products, user.getProducts());

        List<Address> addresses = new ArrayList<>();
        user.setAddresses(addresses);
        assertEquals(addresses, user.getAddresses());

        Cart cart = new Cart();
        user.setCart(cart);
        assertEquals(cart, user.getCart());
    }

    @Test
    public void testToString() {
        User user = new User();
        user.setUserId(1L);
        user.setUserName("john_doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        String expected = "User{userID=1, userName='john_doe', email='john.doe@example.com', password='password123', roles=[], addresses=[]}";
        assertEquals(expected, user.toString());
    }
}
