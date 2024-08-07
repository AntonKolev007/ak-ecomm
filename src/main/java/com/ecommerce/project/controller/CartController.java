package com.ecommerce.project.controller;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.payload.CartRequestDTO;
import com.ecommerce.project.repositories.CartRepository;
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
    private final CartRepository cartRepository;
    private final AuthUtil authUtil;

    public CartController(CartService cartService, CartRepository cartRepository, AuthUtil authUtil) {
        this.cartService = cartService;
        this.cartRepository = cartRepository;
        this.authUtil = authUtil;
    }

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartRequestDTO> addProductToCart(
            @PathVariable Long productId,
            @PathVariable Integer quantity) {

        CartRequestDTO cartRequestDTO = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<CartRequestDTO>(cartRequestDTO, HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartRequestDTO>> getCarts() {
        List<CartRequestDTO> cartDTOs = cartService.getAllCarts();
        return new ResponseEntity<List<CartRequestDTO>>(cartDTOs, HttpStatus.FOUND);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartRequestDTO> getCartById() {

        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "emailId", emailId);
        }
        Long cartId = cart.getCartId();
        CartRequestDTO cartDTO = cartService.getCart(emailId, cartId);

        return new ResponseEntity<CartRequestDTO>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartRequestDTO> updateCartProduct(
            @PathVariable Long productId,
            @PathVariable String operation) {

        CartRequestDTO cartDTO = cartService.updateProductQuantityInCart(productId,
                operation.equalsIgnoreCase("delete") ? -1 : 1);

        return new ResponseEntity<CartRequestDTO>(cartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,
                                                        @PathVariable Long productId) {
        String status = cartService.deleteProductFromCart(cartId, productId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}