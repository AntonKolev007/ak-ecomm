package com.ecommerce.project.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class APIExceptionTest {

    @Test
    public void testNoArgConstructor() {
        APIException exception = new APIException();
        assertNull(exception.getMessage());
    }

    @Test
    public void testMessageConstructor() {
        String errorMessage = "This is a custom API exception message.";
        APIException exception = new APIException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }
}
