package com.ecommerce.project.service;

import com.ecommerce.project.payload.CategoryRequestDTO;
import com.ecommerce.project.payload.CategoryResponseDTO;

public interface CategoryService {

    CategoryResponseDTO getAllCategories(Integer pageNumber,
                                         Integer pageSize,
                                         String sortBy,
                                         String sortOrder);

    CategoryRequestDTO createCategory(CategoryRequestDTO categoryRequestDTO);

    CategoryRequestDTO deleteCategory(Long categoryId);

    CategoryRequestDTO updateCategory(CategoryRequestDTO categoryRequestDTO, Long categoryId);
}
