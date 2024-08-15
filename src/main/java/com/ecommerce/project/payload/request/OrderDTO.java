package com.ecommerce.project.payload.request;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class OrderDTO implements Serializable {
    private Long orderId;
    private String email;
    private List<OrderItemDTO> orderItems = new ArrayList<>();
    private LocalDate orderDate;
    private PaymentDTO payment;
    private Double totalAmount;
    private String orderStatus;
    private Long addressId;

    public OrderDTO() {
    }

    public OrderDTO(Long orderId,
                    String email,
                    List<OrderItemDTO> orderItems,
                    LocalDate orderDate,
                    PaymentDTO payment,
                    Double totalAmount,
                    String orderStatus, Long addressId) {
        this.orderId = orderId;
        this.email = email;
        this.orderItems = orderItems;
        this.orderDate = orderDate;
        this.payment = payment;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.addressId = addressId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return Objects.equals(orderId, orderDTO.orderId) && Objects.equals(email, orderDTO.email) && Objects.equals(orderItems, orderDTO.orderItems) && Objects.equals(orderDate, orderDTO.orderDate) && Objects.equals(payment, orderDTO.payment) && Objects.equals(totalAmount, orderDTO.totalAmount) && Objects.equals(orderStatus, orderDTO.orderStatus) && Objects.equals(addressId, orderDTO.addressId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, email, orderItems, orderDate, payment, totalAmount, orderStatus, addressId);
    }
}
