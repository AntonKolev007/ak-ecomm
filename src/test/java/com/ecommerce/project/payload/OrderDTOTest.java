package com.ecommerce.project.payload;

import com.ecommerce.project.payload.request.OrderDTO;
import com.ecommerce.project.payload.request.OrderItemDTO;
import com.ecommerce.project.payload.request.PaymentDTO;
import com.ecommerce.project.payload.request.ProductRequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDTOTest {

    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.findAndRegisterModules();
    }

    @Test
    public void testDefaultConstructor() {
        OrderDTO dto = new OrderDTO();
        assertNull(dto.getOrderId(), "orderId should be null");
        assertNull(dto.getEmail(), "email should be null");
        assertTrue(dto.getOrderItems().isEmpty(), "orderItems should be null");
        assertNull(dto.getOrderDate(), "orderDate should be null");
        assertNull(dto.getPayment(), "payment should be null");
        assertNull(dto.getTotalAmount(), "totalAmount should be null");
        assertNull(dto.getOrderStatus(), "orderStatus should be null");
        assertNull(dto.getAddressId(), "addressId should be null");
    }

    @Test
    public void testParameterizedConstructor() {
        ProductRequestDTO product = new ProductRequestDTO(1L, "Product1", "image1", "description1", 10, 100.0, 10.0, 90.0);
        List<OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(new OrderItemDTO(1L, product, 2, 10.0, 100.0));
        PaymentDTO payment = new PaymentDTO();
        LocalDate orderDate = LocalDate.now();

        OrderDTO dto = new OrderDTO(1L, "test@example.com", orderItems, orderDate, payment, 100.0, "Pending", 1L);

        assertEquals(1L, dto.getOrderId(), "orderId should be 1");
        assertEquals("test@example.com", dto.getEmail(), "email should be 'test@example.com'");
        assertEquals(orderItems, dto.getOrderItems(), "orderItems should match");
        assertEquals(orderDate, dto.getOrderDate(), "orderDate should match");
        assertEquals(payment, dto.getPayment(), "payment should match");
        assertEquals(100.0, dto.getTotalAmount(), "totalAmount should be 100.0");
        assertEquals("Pending", dto.getOrderStatus(), "orderStatus should be 'Pending'");
        assertEquals(1L, dto.getAddressId(), "addressId should be 1");
    }

    @Test
    public void testGettersAndSetters() {
        OrderDTO dto = new OrderDTO();
        ProductRequestDTO product = new ProductRequestDTO(1L, "Product1", "image1", "description1", 10, 100.0, 10.0, 90.0);
        List<OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(new OrderItemDTO(1L, product, 2, 10.0, 100.0));
        PaymentDTO payment = new PaymentDTO();
        LocalDate orderDate = LocalDate.now();

        dto.setOrderId(1L);
        assertEquals(1L, dto.getOrderId(), "orderId should be 1");

        dto.setEmail("test@example.com");
        assertEquals("test@example.com", dto.getEmail(), "email should be 'test@example.com'");

        dto.setOrderItems(orderItems);
        assertEquals(orderItems, dto.getOrderItems(), "orderItems should match");

        dto.setOrderDate(orderDate);
        assertEquals(orderDate, dto.getOrderDate(), "orderDate should match");

        dto.setPayment(payment);
        assertEquals(payment, dto.getPayment(), "payment should match");

        dto.setTotalAmount(100.0);
        assertEquals(100.0, dto.getTotalAmount(), "totalAmount should be 100.0");

        dto.setOrderStatus("Pending");
        assertEquals("Pending", dto.getOrderStatus(), "orderStatus should be 'Pending'");

        dto.setAddressId(1L);
        assertEquals(1L, dto.getAddressId(), "addressId should be 1");
    }

    @Test
    public void testJsonSerialization() throws JsonProcessingException {
        ProductRequestDTO product = new ProductRequestDTO(1L, "Product1", "image1", "description1", 10, 100.0, 10.0, 90.0);
        List<OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(new OrderItemDTO(1L, product, 2, 10.0, 100.0));
        PaymentDTO payment = new PaymentDTO();
        LocalDate orderDate = LocalDate.now();
        OrderDTO dto = new OrderDTO(1L, "test@example.com", orderItems, orderDate, payment, 100.0, "Pending", 1L);

        String json = mapper.writeValueAsString(dto);

        assertTrue(json.contains("\"orderId\":1"), "JSON should contain the orderId");
        assertTrue(json.contains("\"email\":\"test@example.com\""), "JSON should contain the email");
        assertTrue(json.contains("\"orderItems\":[{\"orderItemId\":1,\"product\":{\"productId\":1,\"productName\":\"Product1\",\"image\":\"image1\",\"description\":\"description1\",\"quantity\":10,\"price\":100.0,\"discount\":10.0,\"specialPrice\":90.0},\"quantity\":2,\"discount\":10.0,\"orderedProductPrice\":100.0}]"), "JSON should contain the orderItems");
        //assertTrue(json.contains("\"orderDate\":\"" + orderDate + "\""), "JSON should contain the orderDate");
        //assertTrue(json.contains("\"payment\":{}"), "JSON should contain the payment");
        assertTrue(json.contains("\"totalAmount\":100.0"), "JSON should contain the totalAmount");
        assertTrue(json.contains("\"orderStatus\":\"Pending\""), "JSON should contain the orderStatus");
        assertTrue(json.contains("\"addressId\":1"), "JSON should contain the addressId");
    }

    @Test
    public void testJsonDeserialization() throws JsonProcessingException {
        LocalDate orderDate = LocalDate.now();
        String json = "{\"orderId\":1,\"email\":\"test@example.com\",\"orderItems\":[{\"orderItemId\":1,\"product\":{\"productId\":1,\"productName\":\"Product1\",\"image\":\"image1\",\"description\":\"description1\",\"quantity\":10,\"price\":100.0,\"discount\":10.0,\"specialPrice\":90.0},\"quantity\":2,\"discount\":10.0,\"orderedProductPrice\":100.0}],\"orderDate\":\"" + orderDate.toString() + "\",\"payment\":{},\"totalAmount\":100.0,\"orderStatus\":\"Pending\",\"addressId\":1}";

        OrderDTO dto = mapper.readValue(json, OrderDTO.class);

        assertEquals(1L, dto.getOrderId(), "orderId should be 1");
        assertEquals("test@example.com", dto.getEmail(), "email should be 'test@example.com'");
        assertNotNull(dto.getOrderItems(), "orderItems should not be null");
        assertEquals(1, dto.getOrderItems().size(), "orderItems size should be 1");
        assertEquals(1L, dto.getOrderItems().get(0).getOrderItemId(), "orderItemId should be 1");
        assertNotNull(dto.getOrderItems().get(0).getProduct(), "product should not be null");
        assertEquals(1L, dto.getOrderItems().get(0).getProduct().getProductId(), "productId should be 1");
        assertEquals("Product1", dto.getOrderItems().get(0).getProduct().getProductName(), "productName should be 'Product1'");
        assertEquals("image1", dto.getOrderItems().get(0).getProduct().getImage(), "image should be 'image1'");
        assertEquals("description1", dto.getOrderItems().get(0).getProduct().getDescription(), "description should be 'description1'");
        assertEquals(10, dto.getOrderItems().get(0).getProduct().getQuantity(), "quantity should be 10");
        assertEquals(100.0, dto.getOrderItems().get(0).getProduct().getPrice(), "price should be 100.0");
        assertEquals(10.0, dto.getOrderItems().get(0).getProduct().getDiscount(), "discount should be 10.0");
        assertEquals(90.0, dto.getOrderItems().get(0).getProduct().getSpecialPrice(), "specialPrice should be 90.0");
        assertEquals(2, dto.getOrderItems().get(0).getQuantity(), "quantity should be 2");
        assertEquals(10.0, dto.getOrderItems().get(0).getDiscount(), "discount should be 10.0");
        assertEquals(100.0, dto.getOrderItems().get(0).getOrderedProductPrice(), "orderedProductPrice should be 100.0");
        assertEquals(orderDate, dto.getOrderDate(), "orderDate should match");
        assertNotNull(dto.getPayment(), "payment should not be null");
        assertEquals(100.0, dto.getTotalAmount(), "totalAmount should be 100.0");
        assertEquals("Pending", dto.getOrderStatus(), "orderStatus should be 'Pending'");
        assertEquals(1L, dto.getAddressId(), "addressId should be 1");
    }

    @Test
    public void testJavaSerialization() throws IOException, ClassNotFoundException {
        ProductRequestDTO product = new ProductRequestDTO(1L, "Product1", "image1", "description1", 10, 100.0, 10.0, 90.0);
        List<OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(new OrderItemDTO(1L, product, 2, 10.0, 100.0));
        PaymentDTO payment = new PaymentDTO();
        LocalDate orderDate = LocalDate.now();
        OrderDTO dto = new OrderDTO(1L, "test@example.com", orderItems, orderDate, payment, 100.0, "Pending", 1L);

        // Serialize to a byte array
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(dto);

        // Deserialize from the byte array
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        OrderDTO deserializedDTO = (OrderDTO) in.readObject();

        assertEquals(dto.getOrderId(), deserializedDTO.getOrderId(), "orderId should match");
        assertEquals(dto.getEmail(), deserializedDTO.getEmail(), "email should match");
        assertEquals(dto.getOrderItems(), deserializedDTO.getOrderItems(), "orderItems should match");
        assertEquals(dto.getOrderDate(), deserializedDTO.getOrderDate(), "orderDate should match");
        assertEquals(dto.getPayment(), deserializedDTO.getPayment(), "payment should match");
        assertEquals(dto.getTotalAmount(), deserializedDTO.getTotalAmount(), "totalAmount should match");
        assertEquals(dto.getOrderStatus(), deserializedDTO.getOrderStatus(), "orderStatus should match");
        assertEquals(dto.getAddressId(), deserializedDTO.getAddressId(), "addressId should match");
    }
}
