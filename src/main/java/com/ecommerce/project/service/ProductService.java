package com.ecommerce.project.service;

import com.ecommerce.project.payload.request.ProductRequestDTO;
import com.ecommerce.project.payload.response.ProductResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    ProductRequestDTO addProduct(Long categoryId, ProductRequestDTO product);

    ProductResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponseDTO searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponseDTO searchProductByKeyword(String keyWord, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductRequestDTO updateProduct(Long productId, ProductRequestDTO product);

    ProductRequestDTO deleteProduct(Long productId);

    ProductRequestDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

    List<ProductRequestDTO> getFeaturedProducts();
}
