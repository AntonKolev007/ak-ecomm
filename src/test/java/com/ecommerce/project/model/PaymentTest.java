package com.ecommerce.project.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    @Test
    public void testPaymentDefaultConstructor() {
        Payment payment = new Payment();
        assertNull(payment.getPaymentId());
        assertNull(payment.getOrder());
        assertNull(payment.getPaymentMethod());
        assertNull(payment.getPgPaymentId());
        assertNull(payment.getPgStatus());
        assertNull(payment.getPgResponseMessage());
        assertNull(payment.getPgName());
    }

    @Test
    public void testPaymentParameterizedConstructor() {
        Order order = new Order();
        Payment payment = new Payment(1L, order, "Credit Card", "PG12345", "Success", "Payment processed successfully", "PayPal");

        assertEquals(1L, payment.getPaymentId());
        assertEquals(order, payment.getOrder());
        assertEquals("Credit Card", payment.getPaymentMethod());
        assertEquals("PG12345", payment.getPgPaymentId());
        assertEquals("Success", payment.getPgStatus());
        assertEquals("Payment processed successfully", payment.getPgResponseMessage());
        assertEquals("PayPal", payment.getPgName());
    }

    @Test
    public void testPaymentConstructorWithoutOrder() {
        Payment payment = new Payment("Credit Card", "PG12345", "Success", "Payment processed successfully", "PayPal");

        assertNull(payment.getPaymentId());
        assertNull(payment.getOrder());
        assertEquals("Credit Card", payment.getPaymentMethod());
        assertEquals("PG12345", payment.getPgPaymentId());
        assertEquals("Success", payment.getPgStatus());
        assertEquals("Payment processed successfully", payment.getPgResponseMessage());
        assertEquals("PayPal", payment.getPgName());
    }

    @Test
    public void testPaymentConstructorWithPartialParams() {
        Payment payment = new Payment(1L, "PG12345", "Success", "Payment processed successfully", "PayPal");

        assertEquals(1L, payment.getPaymentId());
        assertNull(payment.getOrder());
        assertNull(payment.getPaymentMethod());
        assertEquals("PG12345", payment.getPgPaymentId());
        assertEquals("Success", payment.getPgStatus());
        assertEquals("Payment processed successfully", payment.getPgResponseMessage());
        assertEquals("PayPal", payment.getPgName());
    }

    @Test
    public void testSettersAndGetters() {
        Payment payment = new Payment();

        payment.setPaymentId(1L);
        assertEquals(1L, payment.getPaymentId());

        Order order = new Order();
        payment.setOrder(order);
        assertEquals(order, payment.getOrder());

        payment.setPaymentMethod("Credit Card");
        assertEquals("Credit Card", payment.getPaymentMethod());

        payment.setPgPaymentId("PG12345");
        assertEquals("PG12345", payment.getPgPaymentId());

        payment.setPgStatus("Success");
        assertEquals("Success", payment.getPgStatus());

        payment.setPgResponseMessage("Payment processed successfully");
        assertEquals("Payment processed successfully", payment.getPgResponseMessage());

        payment.setPgName("PayPal");
        assertEquals("PayPal", payment.getPgName());
    }

    @Test
    public void testToString() {
        Payment payment = new Payment();
        payment.setPaymentId(1L);
        payment.setPaymentMethod("Credit Card");
        payment.setPgPaymentId("PG12345");
        payment.setPgStatus("Success");
        payment.setPgResponseMessage("Payment processed successfully");
        payment.setPgName("PayPal");

        String expected = "Payment{paymentId=1, paymentMethod='Credit Card', pgPaymentId='PG12345', pgStatus='Success', pgResponseMessage='Payment processed successfully', pgName='PayPal'}";
        assertEquals(expected, payment.toString());
    }
}
