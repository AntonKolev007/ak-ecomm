package com.ecommerce.project.payload;

import java.io.Serializable;

public class CartItemsRequestDTO implements Serializable {
    private Long cartItemId;
    private CartRequestDTO cart;
    private ProductRequestDTO product;
    private Integer quantity;
    private Double discount;
    private Double productPrice;

    public CartItemsRequestDTO() {
    }

    public CartItemsRequestDTO(Long cartItemId, CartRequestDTO cart, ProductRequestDTO product, Integer quantity, Double discount, Double productPrice) {
        this.cartItemId = cartItemId;
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.discount = discount;
        this.productPrice = productPrice;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public CartRequestDTO getCart() {
        return cart;
    }

    public void setCart(CartRequestDTO cart) {
        this.cart = cart;
    }

    public ProductRequestDTO getProduct() {
        return product;
    }

    public void setProduct(ProductRequestDTO product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }
}
