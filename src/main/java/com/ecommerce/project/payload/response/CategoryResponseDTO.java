package com.ecommerce.project.payload.response;

import com.ecommerce.project.payload.request.CategoryRequestDTO;

import java.io.Serializable;
import java.util.List;

public class CategoryResponseDTO implements Serializable {
    private List<CategoryRequestDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPages;


    public CategoryResponseDTO() {
    }

    public CategoryResponseDTO(List<CategoryRequestDTO> content) {
        this.content = content;
    }

    public List<CategoryRequestDTO> getContent() {
        return content;
    }

    public void setContent(List<CategoryRequestDTO> content) {
        this.content = content;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLastPages() {
        return lastPages;
    }

    public void setLastPages(boolean lastPages) {
        this.lastPages = lastPages;
    }
}
