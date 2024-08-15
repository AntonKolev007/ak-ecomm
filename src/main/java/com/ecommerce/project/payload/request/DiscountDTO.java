package com.ecommerce.project.payload.request;

import java.io.Serializable;

public class DiscountDTO implements Serializable {
    private Long productId;
    private Double discountPercentage;
    private String startDate;
    private String endDate;

    public DiscountDTO() {
    }

    public DiscountDTO(Long productId, Double discountPercentage, String startDate, String endDate) {
        this.productId = productId;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
