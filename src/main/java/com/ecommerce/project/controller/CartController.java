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
    public ResponseEntity<CartRequestDTO> addProductToCart(
            @PathVariable Long productId,
            @PathVariable Integer quantity) {
        try {
            CartRequestDTO cartRequestDTO = cartService.addProductToCart(productId, quantity);
            return new ResponseEntity<>(cartRequestDTO, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartRequestDTO>> getCarts() {
        try {
            List<CartRequestDTO> cartDTOs = cartService.getAllCarts();
            return new ResponseEntity<>(cartDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartRequestDTO> getCartById() {
        try {
            String emailId = authUtil.loggedInEmail();
            CartRequestDTO cartDTO = cartService.getCartByEmail(emailId);
            return new ResponseEntity<>(cartDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartRequestDTO> updateCartProduct(
            @PathVariable Long productId,
            @PathVariable String operation) {
        try {
            CartRequestDTO cartDTO = cartService.updateProductQuantityInCart(productId,
                    operation.equalsIgnoreCase("delete") ? -1 : 1);
            return new ResponseEntity<>(cartDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,
                                                        @PathVariable Long productId) {
        try {
            String status = cartService.deleteProductFromCart(cartId, productId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}