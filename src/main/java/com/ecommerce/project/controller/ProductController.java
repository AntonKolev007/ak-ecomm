package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.ProductRequestDTO;
import com.ecommerce.project.payload.ProductResponseDTO;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductRequestDTO> addProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO,
                                                        @PathVariable Long categoryId) {

        ProductRequestDTO savedProductRequestDTO = productService.addProduct(categoryId, productRequestDTO);
        return new ResponseEntity<>(savedProductRequestDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponseDTO> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {
        ProductResponseDTO productResponseDTO = productService.getAllProducts(
                pageNumber, pageSize, sortBy, sortOrder
        );
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);

    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponseDTO> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder) {
        ProductResponseDTO productResponseDTO = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyWord}")
    public ResponseEntity<ProductResponseDTO> getProductsByKeyword(
            @PathVariable String keyWord,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder) {
        ProductResponseDTO productResponseDTO = productService.searchProductByKeyword(keyWord, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductRequestDTO> updateProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO,
                                                           @PathVariable Long productId) {
        ProductRequestDTO updatedProductDTO = productService.updateProduct(productId, productRequestDTO);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductRequestDTO> deleteProduct(@PathVariable Long productId) {
        ProductRequestDTO deletedProduct = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<ProductRequestDTO> updateProductImage(@PathVariable Long productId,
                                                                @RequestParam("image") MultipartFile image) throws IOException {
        ProductRequestDTO updatedProduct = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}
