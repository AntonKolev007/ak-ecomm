package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.payload.CategoryRequestDTO;
import com.ecommerce.project.payload.CategoryResponseDTO;
import com.ecommerce.project.service.CategoryService;
import com.ecommerce.project.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;
    private final AuthUtil authUtil;

    public CategoryController(CategoryService categoryService, AuthUtil authUtil) {
        this.categoryService = categoryService;
        this.authUtil = authUtil;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<Object> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder) {
        try {
            CategoryResponseDTO categoryResponseDTO = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
            return new ResponseEntity<>(categoryResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<Object> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        if (!authUtil.isAuthenticated()) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized: No active session."), HttpStatus.UNAUTHORIZED);
        }
        try {
            CategoryRequestDTO savedCategoryDTO = categoryService.createCategory(categoryRequestDTO);
            return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long categoryId) {
        if (!authUtil.isAuthenticated()) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized: No active session."), HttpStatus.UNAUTHORIZED);
        }
        try {
            CategoryRequestDTO deletedCategory = categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(deletedCategory, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Resource not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Object> updateCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO,
                                                 @PathVariable Long categoryId) {
        if (!authUtil.isAuthenticated()) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized: No active session."), HttpStatus.UNAUTHORIZED);
        }
        try {
            CategoryRequestDTO savedCategoryRequestDTO = categoryService.updateCategory(categoryRequestDTO, categoryId);
            return new ResponseEntity<>(savedCategoryRequestDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Resource not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
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