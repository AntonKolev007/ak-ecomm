package com.ecommerce.project.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    @Test
    public void testOrderDefaultConstructor() {
        Order order = new Order();
        assertNull(order.getOrderId());
        assertNull(order.getEmail());
        assertNotNull(order.getOrderItems());
        assertTrue(order.getOrderItems().isEmpty());
        assertNull(order.getOrderDate());
        assertNull(order.getTotalAmount());
        assertNull(order.getOrderStatus());
        assertNull(order.getAddress());
        assertNull(order.getPayment());
    }

    @Test
    public void testOrderParameterizedConstructor() {
        List<OrderItem> orderItems = new ArrayList<>();
        Address address = new Address();
        LocalDate date = LocalDate.now();

        Order order = new Order(1L, "user@example.com", orderItems, date, 500.0, "Processing", address);

        assertEquals(1L, order.getOrderId());
        assertEquals("user@example.com", order.getEmail());
        assertEquals(orderItems, order.getOrderItems());
        assertEquals(date, order.getOrderDate());
        assertEquals(500.0, order.getTotalAmount());
        assertEquals("Processing", order.getOrderStatus());
        assertEquals(address, order.getAddress());
    }

    @Test
    public void testSettersAndGetters() {
        Order order = new Order();
        order.setOrderId(1L);
        assertEquals(1L, order.getOrderId());

        order.setEmail("user@example.com");
        assertEquals("user@example.com", order.getEmail());

        List<OrderItem> orderItems = new ArrayList<>();
        order.setOrderItems(orderItems);
        assertEquals(orderItems, order.getOrderItems());

        LocalDate date = LocalDate.now();
        order.setOrderDate(date);
        assertEquals(date, order.getOrderDate());

        order.setTotalAmount(1000.0);
        assertEquals(1000.0, order.getTotalAmount());

        order.setOrderStatus("Completed");
        assertEquals("Completed", order.getOrderStatus());

        Address address = new Address();
        order.setAddress(address);
        assertEquals(address, order.getAddress());

        Payment payment = new Payment();
        order.setPayment(payment);
        assertEquals(payment, order.getPayment());
    }
}
