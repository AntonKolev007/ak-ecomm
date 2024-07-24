package com.ecommerce.project.service;

import com.ecommerce.project.payload.CartRequestDTO;

import java.util.List;

public interface CartService {
    CartRequestDTO addProductToCart(Long productId, Integer quantity);

    List<CartRequestDTO> getAllCarts();

    CartRequestDTO getCart(String emailId, Long cartId);

    CartRequestDTO updateProductQuantityInCart(Long productId, Integer delete);

    String deleteProductFromCart(Long cartId, Long productId);

    void updateProductInCarts(Long cartId, Long productId);
}