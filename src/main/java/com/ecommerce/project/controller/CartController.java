package com.ecommerce.project.controller;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.payload.CartRequestDTO;
import com.ecommerce.project.service.CartService;
import com.ecommerce.project.util.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;
    private final AuthUtil authUtil;

    public CartController(CartService cartService, AuthUtil authUtil) {
        this.cartService = cartService;
        this.authUtil = authUtil;
    }

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<Object> addProductToCart(
            @PathVariable Long productId,
            @PathVariable Integer quantity) {
        if (!authUtil.isAuthenticated()) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized: No active session."), HttpStatus.UNAUTHORIZED);
        }
        try {
            CartRequestDTO cartRequestDTO = cartService.addProductToCart(productId, quantity);
            return new ResponseEntity<>(cartRequestDTO, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Resource not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/carts")
    public ResponseEntity<Object> getCarts() {
        if (!authUtil.isAuthenticated()) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized: No active session."), HttpStatus.UNAUTHORIZED);
        }
        try {
            List<CartRequestDTO> cartDTOs = cartService.getAllCarts();
            return new ResponseEntity<>(cartDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<Object> getCartById() {
        if (!authUtil.isAuthenticated()) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized: No active session."), HttpStatus.UNAUTHORIZED);
        }
        try {
            String emailId = authUtil.loggedInEmail();
            CartRequestDTO cartDTO = cartService.getCartByEmail(emailId);
            return new ResponseEntity<>(cartDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Resource not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<Object> updateCartProduct(
            @PathVariable Long productId,
            @PathVariable String operation) {
        if (!authUtil.isAuthenticated()) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized: No active session."), HttpStatus.UNAUTHORIZED);
        }
        try {
            CartRequestDTO cartDTO = cartService.updateProductQuantityInCart(productId,
                    operation.equalsIgnoreCase("delete") ? -1 : 1);
            return new ResponseEntity<>(cartDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Resource not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<Object> deleteProductFromCart(@PathVariable Long cartId,
                                                        @PathVariable Long productId) {
        if (!authUtil.isAuthenticated()) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized: No active session."), HttpStatus.UNAUTHORIZED);
        }
        try {
            String status = cartService.deleteProductFromCart(cartId, productId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Resource not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Inner class to represent an error response
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}