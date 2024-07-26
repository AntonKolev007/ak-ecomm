package com.ecommerce.project.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentDTOTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDefaultConstructor() {
        PaymentDTO dto = new PaymentDTO();
        assertNull(dto.getPaymentId(), "paymentId should be null");
        assertNull(dto.getPaymentMethod(), "paymentMethod should be null");
        assertNull(dto.getPgPaymentId(), "pgPaymentId should be null");
        assertNull(dto.getPgStatus(), "pgStatus should be null");
        assertNull(dto.getPgResponseMessage(), "pgResponseMessage should be null");
        assertNull(dto.getPgName(), "pgName should be null");
    }

    @Test
    public void testParameterizedConstructor() {
        PaymentDTO dto = new PaymentDTO(1L, "Credit Card", "PG12345", "Success", "Payment processed successfully", "PayPal");

        assertEquals(1L, dto.getPaymentId(), "paymentId should be 1");
        assertEquals("Credit Card", dto.getPaymentMethod(), "paymentMethod should be 'Credit Card'");
        assertEquals("PG12345", dto.getPgPaymentId(), "pgPaymentId should be 'PG12345'");
        assertEquals("Success", dto.getPgStatus(), "pgStatus should be 'Success'");
        assertEquals("Payment processed successfully", dto.getPgResponseMessage(), "pgResponseMessage should be 'Payment processed successfully'");
        assertEquals("PayPal", dto.getPgName(), "pgName should be 'PayPal'");
    }

    @Test
    public void testGettersAndSetters() {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(1L);
        assertEquals(1L, dto.getPaymentId(), "paymentId should be 1");

        dto.setPaymentMethod("Credit Card");
        assertEquals("Credit Card", dto.getPaymentMethod(), "paymentMethod should be 'Credit Card'");

        dto.setPgPaymentId("PG12345");
        assertEquals("PG12345", dto.getPgPaymentId(), "pgPaymentId should be 'PG12345'");

        dto.setPgStatus("Success");
        assertEquals("Success", dto.getPgStatus(), "pgStatus should be 'Success'");

        dto.setPgResponseMessage("Payment processed successfully");
        assertEquals("Payment processed successfully", dto.getPgResponseMessage(), "pgResponseMessage should be 'Payment processed successfully'");

        dto.setPgName("PayPal");
        assertEquals("PayPal", dto.getPgName(), "pgName should be 'PayPal'");
    }

    @Test
    public void testJsonSerialization() throws JsonProcessingException {
        PaymentDTO dto = new PaymentDTO(1L, "Credit Card", "PG12345", "Success", "Payment processed successfully", "PayPal");

        String json = mapper.writeValueAsString(dto);

        assertTrue(json.contains("\"paymentId\":1"), "JSON should contain the paymentId");
        assertTrue(json.contains("\"paymentMethod\":\"Credit Card\""), "JSON should contain the paymentMethod");
        assertTrue(json.contains("\"pgPaymentId\":\"PG12345\""), "JSON should contain the pgPaymentId");
        assertTrue(json.contains("\"pgStatus\":\"Success\""), "JSON should contain the pgStatus");
        assertTrue(json.contains("\"pgResponseMessage\":\"Payment processed successfully\""), "JSON should contain the pgResponseMessage");
        assertTrue(json.contains("\"pgName\":\"PayPal\""), "JSON should contain the pgName");
    }

    @Test
    public void testJsonDeserialization() throws JsonProcessingException {
        String json = "{\"paymentId\":1,\"paymentMethod\":\"Credit Card\",\"pgPaymentId\":\"PG12345\",\"pgStatus\":\"Success\",\"pgResponseMessage\":\"Payment processed successfully\",\"pgName\":\"PayPal\"}";

        PaymentDTO dto = mapper.readValue(json, PaymentDTO.class);

        assertEquals(1L, dto.getPaymentId(), "paymentId should be 1");
        assertEquals("Credit Card", dto.getPaymentMethod(), "paymentMethod should be 'Credit Card'");
        assertEquals("PG12345", dto.getPgPaymentId(), "pgPaymentId should be 'PG12345'");
        assertEquals("Success", dto.getPgStatus(), "pgStatus should be 'Success'");
        assertEquals("Payment processed successfully", dto.getPgResponseMessage(), "pgResponseMessage should be 'Payment processed successfully'");
        assertEquals("PayPal", dto.getPgName(), "pgName should be 'PayPal'");
    }

    @Test
    public void testJavaSerialization() throws IOException, ClassNotFoundException {
        PaymentDTO dto = new PaymentDTO(1L, "Credit Card", "PG12345", "Success", "Payment processed successfully", "PayPal");

        // Serialize to a byte array
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(dto);

        // Deserialize from the byte array
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        PaymentDTO deserializedDTO = (PaymentDTO) in.readObject();

        assertEquals(dto.getPaymentId(), deserializedDTO.getPaymentId(), "paymentId should match");
        assertEquals(dto.getPaymentMethod(), deserializedDTO.getPaymentMethod(), "paymentMethod should match");
        assertEquals(dto.getPgPaymentId(), deserializedDTO.getPgPaymentId(), "pgPaymentId should match");
        assertEquals(dto.getPgStatus(), deserializedDTO.getPgStatus(), "pgStatus should match");
        assertEquals(dto.getPgResponseMessage(), deserializedDTO.getPgResponseMessage(), "pgResponseMessage should match");
        assertEquals(dto.getPgName(), deserializedDTO.getPgName(), "pgName should match");
    }
}
