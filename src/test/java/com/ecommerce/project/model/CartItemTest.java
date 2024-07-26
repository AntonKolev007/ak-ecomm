package com.ecommerce.project.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CartItemTest {

    @Test
    public void testCartItemDefaultConstructor() {
        CartItem cartItem = new CartItem();
        assertNull(cartItem.getCartItemId());
        assertNull(cartItem.getCart());
        assertNull(cartItem.getProduct());
        assertNull(cartItem.getQuantity());
        assertNull(cartItem.getDiscount());
        assertNull(cartItem.getProductPrice());
    }

    @Test
    public void testCartItemParameterizedConstructor() {
        Cart cart = new Cart();
        Product product = new Product();
        CartItem cartItem = new CartItem(1L, cart, product, 5, 10.0, 150.0);

        assertEquals(1L, cartItem.getCartItemId());
        assertEquals(cart, cartItem.getCart());
        assertEquals(product, cartItem.getProduct());
        assertEquals(5, cartItem.getQuantity());
        assertEquals(10.0, cartItem.getDiscount());
        assertEquals(150.0, cartItem.getProductPrice());
    }

    @Test
    public void testSettersAndGetters() {
        CartItem cartItem = new CartItem();
        Cart cart = new Cart();
        Product product = new Product();

        cartItem.setCartItemId(1L);
        assertEquals(1L, cartItem.getCartItemId());

        cartItem.setCart(cart);
        assertEquals(cart, cartItem.getCart());

        cartItem.setProduct(product);
        assertEquals(product, cartItem.getProduct());

        cartItem.setQuantity(10);
        assertEquals(10, cartItem.getQuantity());

        cartItem.setDiscount(5.0);
        assertEquals(5.0, cartItem.getDiscount());

        cartItem.setProductPrice(200.0);
        assertEquals(200.0, cartItem.getProductPrice());
    }
}
