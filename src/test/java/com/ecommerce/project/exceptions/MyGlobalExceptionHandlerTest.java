package com.ecommerce.project.exceptions;

import com.ecommerce.project.payload.APIResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MyGlobalExceptionHandlerTest {

    private MyGlobalExceptionHandler exceptionHandler;

    @BeforeEach
    public void setUp() {
        exceptionHandler = new MyGlobalExceptionHandler();
    }

    @Test
    public void testMyMethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("objectName", "field1", "error1"));
        fieldErrors.add(new FieldError("objectName", "field2", "error2"));

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn((List) fieldErrors);

        ResponseEntity<Map<String, String>> responseEntity = exceptionHandler.myMethodArgumentNotValidException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(2, responseEntity.getBody().size());
        assertEquals("error1", responseEntity.getBody().get("field1"));
        assertEquals("error2", responseEntity.getBody().get("field2"));
    }

    @Test
    public void testMyResourceNotFoundException_NoArgs() {
        ResourceNotFoundException exception = new ResourceNotFoundException();
        ResponseEntity<APIResponse> responseEntity = exceptionHandler.myResourceNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(new APIResponse(null, false), responseEntity.getBody());
    }

    @Test
    public void testMyResourceNotFoundException_ThreeArgs_StringField() {
        ResourceNotFoundException exception = new ResourceNotFoundException("User", "Username", "john_doe");
        ResponseEntity<APIResponse> responseEntity = exceptionHandler.myResourceNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(new APIResponse("User not found with Username: john_doe.", false), responseEntity.getBody());
    }

    @Test
    public void testMyResourceNotFoundException_ThreeArgs_LongField() {
        ResourceNotFoundException exception = new ResourceNotFoundException("User", "UserId", 1L);
        ResponseEntity<APIResponse> responseEntity = exceptionHandler.myResourceNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(new APIResponse("User not found with UserId: 1.", false), responseEntity.getBody());
    }

    @Test
    public void testMyAPIException() {
        APIException exception = new APIException("API error occurred");
        ResponseEntity<APIResponse> responseEntity = exceptionHandler.myAPIException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(new APIResponse("API error occurred", false), responseEntity.getBody());
    }
}
