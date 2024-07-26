package com.ecommerce.project.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderItemTest {

    @Test
    public void testOrderItemDefaultConstructor() {
        OrderItem orderItem = new OrderItem();
        assertNull(orderItem.getOrderItemId());
        assertNull(orderItem.getProduct());
        assertNull(orderItem.getOrder());
        assertNull(orderItem.getQuantity());
        assertEquals(0.0, orderItem.getDiscount());
        assertEquals(0.0, orderItem.getOrderedProductPrice());
    }

    @Test
    public void testOrderItemParameterizedConstructor() {
        Product product = new Product();
        Order order = new Order();
        OrderItem orderItem = new OrderItem(1L, product, order, 5, 10.0, 500.0);

        assertEquals(1L, orderItem.getOrderItemId());
        assertEquals(product, orderItem.getProduct());
        assertEquals(order, orderItem.getOrder());
        assertEquals(5, orderItem.getQuantity());
        assertEquals(10.0, orderItem.getDiscount());
        assertEquals(500.0, orderItem.getOrderedProductPrice());
    }

    @Test
    public void testSettersAndGetters() {
        OrderItem orderItem = new OrderItem();
        Product product = new Product();
        Order order = new Order();

        orderItem.setOrderItemId(1L);
        assertEquals(1L, orderItem.getOrderItemId());

        orderItem.setProduct(product);
        assertEquals(product, orderItem.getProduct());

        orderItem.setOrder(order);
        assertEquals(order, orderItem.getOrder());

        orderItem.setQuantity(10);
        assertEquals(10, orderItem.getQuantity());

        orderItem.setDiscount(15.0);
        assertEquals(15.0, orderItem.getDiscount());

        orderItem.setOrderedProductPrice(1000.0);
        assertEquals(1000.0, orderItem.getOrderedProductPrice());
    }
}
