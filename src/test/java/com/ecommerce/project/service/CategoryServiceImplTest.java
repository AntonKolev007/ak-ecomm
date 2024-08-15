package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.request.CategoryRequestDTO;
import com.ecommerce.project.payload.response.CategoryResponseDTO;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryRequestDTO categoryRequestDTO;
    private CategoryResponseDTO categoryResponseDTO;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setCategoryId(1L);
        category.setCategoryName("Electronics");

        categoryRequestDTO = new CategoryRequestDTO();
        categoryRequestDTO.setCategoryId(1L);
        categoryRequestDTO.setCategoryName("Electronics");

        categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setContent(List.of(categoryRequestDTO));
    }

    @Test
    void testGetAllCategories() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("categoryName").ascending());
        Page<Category> page = new PageImpl<>(List.of(category), pageable, 1);

        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(modelMapper.map(any(Category.class), eq(CategoryRequestDTO.class))).thenReturn(categoryRequestDTO);

        CategoryResponseDTO result = categoryService.getAllCategories(0, 10, "categoryName", "asc");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(categoryRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testGetAllCategories_DescendingOrder() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("categoryName").descending());
        Page<Category> page = new PageImpl<>(List.of(category), pageable, 1);

        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(modelMapper.map(any(Category.class), eq(CategoryRequestDTO.class))).thenReturn(categoryRequestDTO);

        CategoryResponseDTO result = categoryService.getAllCategories(0, 10, "categoryName", "desc");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(categoryRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testGetAllCategories_NoCategories() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("categoryName").ascending());
        Page<Category> page = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(page);

        APIException exception = assertThrows(APIException.class, () -> {
            categoryService.getAllCategories(0, 10, "categoryName", "asc");
        });

        assertEquals("No categories initialized!", exception.getMessage());
        verify(categoryRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testCreateCategory() {
        when(modelMapper.map(any(CategoryRequestDTO.class), eq(Category.class))).thenReturn(category);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(modelMapper.map(any(Category.class), eq(CategoryRequestDTO.class))).thenReturn(categoryRequestDTO);

        CategoryRequestDTO result = categoryService.createCategory(categoryRequestDTO);

        assertNotNull(result);
        assertEquals("Electronics", result.getCategoryName());
        verify(categoryRepository, times(1)).findByCategoryName(anyString());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testCreateCategory_CategoryExists() {
        when(modelMapper.map(any(CategoryRequestDTO.class), eq(Category.class))).thenReturn(category);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(category);

        APIException exception = assertThrows(APIException.class, () -> {
            categoryService.createCategory(categoryRequestDTO);
        });

        assertEquals("Category with the name Electronics already exists!", exception.getMessage());
        verify(categoryRepository, times(1)).findByCategoryName(anyString());
        verify(categoryRepository, times(0)).save(any(Category.class));
    }

    @Test
    void testDeleteCategory() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(modelMapper.map(any(Category.class), eq(CategoryRequestDTO.class))).thenReturn(categoryRequestDTO);

        CategoryRequestDTO result = categoryService.deleteCategory(1L);

        assertNotNull(result);
        assertEquals("Electronics", result.getCategoryName());
        verify(categoryRepository, times(1)).findById(anyLong());
        verify(categoryRepository, times(1)).delete(any(Category.class));
    }

    @Test
    void testDeleteCategory_NotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.deleteCategory(1L);
        });

        assertEquals("Category not found with categoryId: 1.", exception.getMessage());
        verify(categoryRepository, times(1)).findById(anyLong());
        verify(categoryRepository, times(0)).delete(any(Category.class));
    }

    @Test
    void testUpdateCategory() {
        when(modelMapper.map(any(CategoryRequestDTO.class), eq(Category.class))).thenReturn(category);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(modelMapper.map(any(Category.class), eq(CategoryRequestDTO.class))).thenReturn(categoryRequestDTO);

        CategoryRequestDTO result = categoryService.updateCategory(categoryRequestDTO, 1L);

        assertNotNull(result);
        assertEquals("Electronics", result.getCategoryName());
        verify(categoryRepository, times(1)).findById(anyLong());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdateCategory_NotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.updateCategory(categoryRequestDTO, 1L);
        });

        assertEquals("Category not found with categoryId: 1.", exception.getMessage());
        verify(categoryRepository, times(1)).findById(anyLong());
        verify(categoryRepository, times(0)).save(any(Category.class));
    }
}
