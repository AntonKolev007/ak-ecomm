package com.ecommerce.project.payload;


public class OrderItemDTO {
    private Long orderItemId;
    private ProductRequestDTO product;
    private Integer quantity;
    private double discount;
    private double orderedProductPrice;

    public OrderItemDTO() {
    }

    public OrderItemDTO(Long orderItemId,
                        ProductRequestDTO product,
                        Integer quantity,
                        double discount,
                        double orderedProductPrice) {
        this.orderItemId = orderItemId;
        this.product = product;
        this.quantity = quantity;
        this.discount = discount;
        this.orderedProductPrice = orderedProductPrice;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
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

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getOrderedProductPrice() {
        return orderedProductPrice;
    }

    public void setOrderedProductPrice(double orderedProductPrice) {
        this.orderedProductPrice = orderedProductPrice;
    }
}
