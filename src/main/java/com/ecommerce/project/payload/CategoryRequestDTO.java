package com.ecommerce.project.payload;

import java.io.Serializable;

public class CategoryRequestDTO implements Serializable {
    private Long categoryId;
    private String categoryName;

    public CategoryRequestDTO() {
    }

    public CategoryRequestDTO(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
