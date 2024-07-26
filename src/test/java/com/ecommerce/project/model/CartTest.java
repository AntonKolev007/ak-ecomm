package com.ecommerce.project.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CartTest {

    @Test
    public void testCartDefaultConstructor() {
        Cart cart = new Cart();
        assertNull(cart.getCartId());
        assertNull(cart.getUser());
        assertNotNull(cart.getCartItems());
        assertTrue(cart.getCartItems().isEmpty());
        assertEquals(0.0, cart.getTotalPrice());
    }

    @Test
    public void testCartParameterizedConstructor() {
        User user = new User();
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem());
        Cart cart = new Cart(1L, user, cartItems, 300.0);

        assertEquals(1L, cart.getCartId());
        assertEquals(user, cart.getUser());
        assertEquals(cartItems, cart.getCartItems());
        assertEquals(300.0, cart.getTotalPrice());
    }

    @Test
    public void testSettersAndGetters() {
        Cart cart = new Cart();
        User user = new User();
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem());

        cart.setCartId(1L);
        assertEquals(1L, cart.getCartId());

        cart.setUser(user);
        assertEquals(user, cart.getUser());

        cart.setCartItems(cartItems);
        assertEquals(cartItems, cart.getCartItems());
        assertEquals(1, cart.getCartItems().size());

        cart.setTotalPrice(200.0);
        assertEquals(200.0, cart.getTotalPrice());
    }

    @Test
    public void testTotalPriceUpdate() {
        Cart cart = new Cart();
        cart.setTotalPrice(100.0);
        assertEquals(100.0, cart.getTotalPrice());

        cart.setTotalPrice(cart.getTotalPrice() + 50.0);
        assertEquals(150.0, cart.getTotalPrice());
    }
}
