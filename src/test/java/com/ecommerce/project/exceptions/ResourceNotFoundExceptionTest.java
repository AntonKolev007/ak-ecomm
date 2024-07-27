package com.ecommerce.project.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceNotFoundExceptionTest {

    @Test
    public void testNoArgsConstructor() {
        ResourceNotFoundException exception = new ResourceNotFoundException();
        assertNull(exception.getMessage());
    }

    @Test
    public void testConstructorWithStringField() {
        String resourceName = "User";
        String field = "Username";
        String fieldName = "john_doe";
        ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, field, fieldName);
        assertEquals("User not found with Username: john_doe.", exception.getMessage());
    }

    @Test
    public void testConstructorWithLongField() {
        String resourceName = "User";
        String field = "UserId";
        Long fieldId = 1L;
        ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, field, fieldId);
        assertEquals("User not found with UserId: 1.", exception.getMessage());
    }

    @Test
    public void testEqualsAndHashCode() {
        String resourceName = "User";
        String field = "Username";
        String fieldName = "john_doe";
        ResourceNotFoundException exception1 = new ResourceNotFoundException(resourceName, field, fieldName);
        ResourceNotFoundException exception2 = new ResourceNotFoundException(resourceName, field, fieldName);
        assertTrue(exception1.equals(exception2) && exception2.equals(exception1));
        assertEquals(exception1.hashCode(), exception2.hashCode());
    }
}
