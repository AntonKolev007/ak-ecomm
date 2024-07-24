package com.ecommerce.project.payload;

import com.ecommerce.project.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartRequestDTO {
    private Long cartId;
    private Double totalPrice=0.0;

    private List<ProductRequestDTO> products = new ArrayList<>();

    public CartRequestDTO() {
    }

    public CartRequestDTO(Long cartId, Double totalPrice, List<ProductRequestDTO> products) {
        this.cartId = cartId;
        this.totalPrice = totalPrice;
        this.products = products;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ProductRequestDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductRequestDTO> products) {
        this.products = products;
    }
}
