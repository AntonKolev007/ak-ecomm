package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.*;
import com.ecommerce.project.payload.ProductRequestDTO;
import com.ecommerce.project.payload.ProductResponseDTO;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartService cartService;

    @Mock
    private FileService fileService;

    @Mock
    private ModelMapper modelMapper;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @Value("${project.image}")
    private String path;

    private Category category;
    private Product product;
    private ProductRequestDTO productRequestDTO;

    @BeforeEach
    void setUp() {
        try (MockedStatic<MockitoAnnotations> mockedStatic = mockStatic(MockitoAnnotations.class)) {
            mockedStatic.when(() -> MockitoAnnotations.openMocks(this)).thenAnswer(invocation -> {
                MockitoAnnotations.openMocks(this).close();
                return null;
            });

            // Initialize common objects
            category = new Category();
            category.setCategoryId(1L);
            category.setCategoryName("Test Category");

            product = new Product();
            product.setProductId(1L);
            product.setProductName("Test Product");
            product.setCategory(category);
            product.setPrice(100.0);
            product.setDiscount(10.0);
            product.setImage("default.png");

            productRequestDTO = new ProductRequestDTO();
            productRequestDTO.setProductName("Test Product");
        }
    }

    @Test
    void testAddProduct_Success() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(modelMapper.map(any(ProductRequestDTO.class), eq(Product.class))).thenReturn(product);
        when(modelMapper.map(any(Product.class), eq(ProductRequestDTO.class))).thenReturn(productRequestDTO);

        ProductRequestDTO result = productService.addProduct(1L, productRequestDTO);

        assertNotNull(result);
        assertEquals("Test Product", result.getProductName());
        verify(productRepository).save(productCaptor.capture());
        Product capturedProduct = productCaptor.getValue();
        assertEquals("Test Product", capturedProduct.getProductName());
    }

    @Test
    void testGetAllProducts_Success() {
        int pageNumber = 0;
        int pageSize = 10;
        String sortBy = "productName";
        String sortOrder = "asc";

        Page<Product> pageProduct = new PageImpl<>(List.of(product));

        when(productRepository.findAll(any(Pageable.class))).thenReturn(pageProduct);
        when(modelMapper.map(any(Product.class), eq(ProductRequestDTO.class))).thenReturn(productRequestDTO);

        ProductResponseDTO response = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        verify(productRepository).findAll(any(Pageable.class));
    }

    @Test
    void testSearchByCategory_Success() {
        int pageNumber = 0;
        int pageSize = 10;
        String sortBy = "productName";
        String sortOrder = "asc";

        Page<Product> pageProduct = new PageImpl<>(List.of(product));

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(productRepository.findByCategoryOrderByPriceAsc(eq(category), any(Pageable.class))).thenReturn(pageProduct);
        when(modelMapper.map(any(Product.class), eq(ProductRequestDTO.class))).thenReturn(productRequestDTO);

        ProductResponseDTO response = productService.searchByCategory(1L, pageNumber, pageSize, sortBy, sortOrder);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        verify(productRepository).findByCategoryOrderByPriceAsc(eq(category), any(Pageable.class));
    }

    @Test
    void testSearchProductByKeyword_Success() {
        String keyWord = "Test";
        int pageNumber = 0;
        int pageSize = 10;
        String sortBy = "productName";
        String sortOrder = "asc";

        Page<Product> pageProduct = new PageImpl<>(List.of(product));

        when(productRepository.findByProductNameLikeIgnoreCase(anyString(), any(Pageable.class))).thenReturn(pageProduct);
        when(modelMapper.map(any(Product.class), eq(ProductRequestDTO.class))).thenReturn(productRequestDTO);

        ProductResponseDTO response = productService.searchProductByKeyword(keyWord, pageNumber, pageSize, sortBy, sortOrder);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        verify(productRepository).findByProductNameLikeIgnoreCase(anyString(), any(Pageable.class));
    }

//    @Test
//    void testUpdateProduct_Success() {
//        Long productId = 1L;
//
//        Product updatedProduct = new Product();
//        updatedProduct.setProductId(productId);
//        updatedProduct.setProductName("Updated Product");
//
//        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
//        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
//        when(modelMapper.map(any(ProductRequestDTO.class), eq(Product.class))).thenReturn(updatedProduct);
//        when(modelMapper.map(any(Product.class), eq(ProductRequestDTO.class))).thenReturn(productRequestDTO);
//
//        ProductRequestDTO result = productService.updateProduct(productId, productRequestDTO);
//
//        assertNotNull(result);
//        assertEquals("Updated Product", result.getProductName());
//        verify(productRepository).save(productCaptor.capture());
//        Product savedProduct = productCaptor.getValue();
//        assertEquals("Updated Product", savedProduct.getProductName());
//    }
//
//    @Test
//    void testDeleteProduct_Success() {
//        Long productId = 1L;
//
//        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
//
//        ProductRequestDTO deletedProduct = productService.deleteProduct(productId);
//
//        assertNotNull(deletedProduct);
//        verify(productRepository).delete(product);
//    }
//
//    @Test
//    void testUpdateProductImage_Success() throws IOException {
//        Long productId = 1L;
//        MultipartFile image = mock(MultipartFile.class);
//
//        Product updatedProduct = new Product();
//        updatedProduct.setProductId(productId);
//        updatedProduct.setProductName("Test Product");
//        updatedProduct.setImage("newImage.png");
//
//        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
//        when(fileService.uploadImage(anyString(), eq(image))).thenReturn("newImage.png");
//        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
//        when(modelMapper.map(any(Product.class), eq(ProductRequestDTO.class))).thenReturn(new ProductRequestDTO());
//
//        ProductRequestDTO result = productService.updateProductImage(productId, image);
//
//        assertNotNull(result);
//        assertEquals("newImage.png", result.getImage());
//        verify(productRepository).save(productCaptor.capture());
//        Product savedProduct = productCaptor.getValue();
//        assertEquals("newImage.png", savedProduct.getImage());
//    }
}
