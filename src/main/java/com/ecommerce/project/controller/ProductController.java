package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.payload.ProductRequestDTO;
import com.ecommerce.project.payload.ProductResponseDTO;
import com.ecommerce.project.service.ProductService;
import com.ecommerce.project.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;
    private final AuthUtil authUtil;

    public ProductController(ProductService productService, AuthUtil authUtil) {
        this.productService = productService;
        this.authUtil = authUtil;
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<Object> addProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO,
                                             @PathVariable Long categoryId) {
        if (!authUtil.isAuthenticated()) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized: No active session."), HttpStatus.UNAUTHORIZED);
        }
        try {
            ProductRequestDTO savedProductRequestDTO = productService.addProduct(categoryId, productRequestDTO);
            return new ResponseEntity<>(savedProductRequestDTO, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Resource not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/public/products")
    public ResponseEntity<Object> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder) {
        try {
            ProductResponseDTO productResponseDTO = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
            return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<Object> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder) {
        try {
            ProductResponseDTO productResponseDTO = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
            return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Resource not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/public/products/keyword/{keyWord}")
    public ResponseEntity<Object> getProductsByKeyword(
            @PathVariable String keyWord,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder) {
        try {
            ProductResponseDTO productResponseDTO = productService.searchProductByKeyword(keyWord, pageNumber, pageSize, sortBy, sortOrder);
            return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<Object> updateProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO,
                                                @PathVariable Long productId) {
        if (!authUtil.isAuthenticated()) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized: No active session."), HttpStatus.UNAUTHORIZED);
        }
        try {
            ProductRequestDTO updatedProductDTO = productService.updateProduct(productId, productRequestDTO);
            return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Resource not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long productId) {
        if (!authUtil.isAuthenticated()) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized: No active session."), HttpStatus.UNAUTHORIZED);
        }
        try {
            ProductRequestDTO deletedProduct = productService.deleteProduct(productId);
            return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Resource not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<Object> updateProductImage(@PathVariable Long productId,
                                                     @RequestParam("image") MultipartFile image) {
        if (!authUtil.isAuthenticated()) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized: No active session."), HttpStatus.UNAUTHORIZED);
        }
        try {
            ProductRequestDTO updatedProduct = productService.updateProductImage(productId, image);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Resource not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(new ErrorResponse("Failed to upload image: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/public/products/featured")
    public ResponseEntity<Object> getFeaturedProducts() {
        try {
            List<ProductRequestDTO> featuredProducts = productService.getFeaturedProducts();
            return new ResponseEntity<>(featuredProducts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Inner class to represent an error response
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
