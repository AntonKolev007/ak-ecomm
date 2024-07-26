package com.ecommerce.project.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class OrderRequestDTOTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDefaultConstructor() {
        OrderRequestDTO dto = new OrderRequestDTO();
        assertNull(dto.getAddressId(), "addressId should be null");
        assertNull(dto.getPaymentMethod(), "paymentMethod should be null");
        assertNull(dto.getPgName(), "pgName should be null");
        assertNull(dto.getPgPaymentId(), "pgPaymentId should be null");
        assertNull(dto.getPgStatus(), "pgStatus should be null");
        assertNull(dto.getPgResponseMessage(), "pgResponseMessage should be null");
    }

    @Test
    public void testParameterizedConstructor() {
        OrderRequestDTO dto = new OrderRequestDTO(1L, "Credit Card", "PayPal", "PG12345", "Success", "Payment processed successfully");

        assertEquals(1L, dto.getAddressId(), "addressId should be 1");
        assertEquals("Credit Card", dto.getPaymentMethod(), "paymentMethod should be 'Credit Card'");
        assertEquals("PayPal", dto.getPgName(), "pgName should be 'PayPal'");
        assertEquals("PG12345", dto.getPgPaymentId(), "pgPaymentId should be 'PG12345'");
        assertEquals("Success", dto.getPgStatus(), "pgStatus should be 'Success'");
        assertEquals("Payment processed successfully", dto.getPgResponseMessage(), "pgResponseMessage should be 'Payment processed successfully'");
    }

    @Test
    public void testGettersAndSetters() {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setAddressId(1L);
        assertEquals(1L, dto.getAddressId(), "addressId should be 1");

        dto.setPaymentMethod("Credit Card");
        assertEquals("Credit Card", dto.getPaymentMethod(), "paymentMethod should be 'Credit Card'");

        dto.setPgName("PayPal");
        assertEquals("PayPal", dto.getPgName(), "pgName should be 'PayPal'");

        dto.setPgPaymentId("PG12345");
        assertEquals("PG12345", dto.getPgPaymentId(), "pgPaymentId should be 'PG12345'");

        dto.setPgStatus("Success");
        assertEquals("Success", dto.getPgStatus(), "pgStatus should be 'Success'");

        dto.setPgResponseMessage("Payment processed successfully");
        assertEquals("Payment processed successfully", dto.getPgResponseMessage(), "pgResponseMessage should be 'Payment processed successfully'");
    }

    @Test
    public void testJsonSerialization() throws JsonProcessingException {
        OrderRequestDTO dto = new OrderRequestDTO(1L, "Credit Card", "PayPal", "PG12345", "Success", "Payment processed successfully");

        String json = mapper.writeValueAsString(dto);

        assertTrue(json.contains("\"addressId\":1"), "JSON should contain the addressId");
        assertTrue(json.contains("\"paymentMethod\":\"Credit Card\""), "JSON should contain the paymentMethod");
        assertTrue(json.contains("\"pgName\":\"PayPal\""), "JSON should contain the pgName");
        assertTrue(json.contains("\"pgPaymentId\":\"PG12345\""), "JSON should contain the pgPaymentId");
        assertTrue(json.contains("\"pgStatus\":\"Success\""), "JSON should contain the pgStatus");
        assertTrue(json.contains("\"pgResponseMessage\":\"Payment processed successfully\""), "JSON should contain the pgResponseMessage");
    }

    @Test
    public void testJsonDeserialization() throws JsonProcessingException {
        String json = "{\"addressId\":1,\"paymentMethod\":\"Credit Card\",\"pgName\":\"PayPal\",\"pgPaymentId\":\"PG12345\",\"pgStatus\":\"Success\",\"pgResponseMessage\":\"Payment processed successfully\"}";

        OrderRequestDTO dto = mapper.readValue(json, OrderRequestDTO.class);

        assertEquals(1L, dto.getAddressId(), "addressId should be 1");
        assertEquals("Credit Card", dto.getPaymentMethod(), "paymentMethod should be 'Credit Card'");
        assertEquals("PayPal", dto.getPgName(), "pgName should be 'PayPal'");
        assertEquals("PG12345", dto.getPgPaymentId(), "pgPaymentId should be 'PG12345'");
        assertEquals("Success", dto.getPgStatus(), "pgStatus should be 'Success'");
        assertEquals("Payment processed successfully", dto.getPgResponseMessage(), "pgResponseMessage should be 'Payment processed successfully'");
    }

    @Test
    public void testJavaSerialization() throws IOException, ClassNotFoundException {
        OrderRequestDTO dto = new OrderRequestDTO(1L, "Credit Card", "PayPal", "PG12345", "Success", "Payment processed successfully");

        // Serialize to a byte array
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(dto);

        // Deserialize from the byte array
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        OrderRequestDTO deserializedDTO = (OrderRequestDTO) in.readObject();

        assertEquals(dto.getAddressId(), deserializedDTO.getAddressId(), "addressId should match");
        assertEquals(dto.getPaymentMethod(), deserializedDTO.getPaymentMethod(), "paymentMethod should match");
        assertEquals(dto.getPgName(), deserializedDTO.getPgName(), "pgName should match");
        assertEquals(dto.getPgPaymentId(), deserializedDTO.getPgPaymentId(), "pgPaymentId should match");
        assertEquals(dto.getPgStatus(), deserializedDTO.getPgStatus(), "pgStatus should match");
        assertEquals(dto.getPgResponseMessage(), deserializedDTO.getPgResponseMessage(), "pgResponseMessage should match");
    }
}
