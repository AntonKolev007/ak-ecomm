package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.request.CartRequestDTO;
import com.ecommerce.project.payload.request.ProductRequestDTO;
import com.ecommerce.project.repositories.CartItemRepository;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.service.impl.CartServiceImpl;
import com.ecommerce.project.util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthUtil authUtil;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart cart;
    private Product product;
    private CartItem cartItem;
    private CartRequestDTO cartRequestDTO;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        cart.setCartId(1L);
        cart.setTotalPrice(0.0);

        product = new Product();
        product.setProductId(1L);
        product.setQuantity(10);
        product.setSpecialPrice(100.0);

        cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setCart(cart);
        cartItem.setQuantity(2);
        cartItem.setProductPrice(100.0);
        cartItem.setDiscount(0.1);

        cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setCartId(1L);
        cartRequestDTO.setTotalPrice(200.0);
        cartRequestDTO.setProducts(new ArrayList<>());
    }

    @Test
    void testAddProductToCart_ValidProduct() {
        when(authUtil.loggedInEmail()).thenReturn("test@example.com");
        when(cartRepository.findCartByEmail(anyString())).thenReturn(cart);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findCartItemByProductIdAndCartId(anyLong(), anyLong())).thenReturn(null);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(modelMapper.map(any(Cart.class), eq(CartRequestDTO.class))).thenReturn(cartRequestDTO);

        CartRequestDTO result = cartService.addProductToCart(1L, 2);

        assertNotNull(result);
        assertEquals(1L, result.getCartId());
        verify(cartRepository, times(1)).save(cart);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testAddProductToCart_ProductAlreadyInCart() {
        when(authUtil.loggedInEmail()).thenReturn("test@example.com");
        when(cartRepository.findCartByEmail(anyString())).thenReturn(cart);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findCartItemByProductIdAndCartId(anyLong(), anyLong())).thenReturn(cartItem);

        APIException exception = assertThrows(APIException.class, () -> {
            cartService.addProductToCart(1L, 2);
        });

        assertEquals("Product " + product.getProductName() + " already exists in the cart", exception.getMessage());
    }

    @Test
    void testAddProductToCart_ProductNotAvailable() {
        product.setQuantity(0);

        when(authUtil.loggedInEmail()).thenReturn("test@example.com");
        when(cartRepository.findCartByEmail(anyString())).thenReturn(cart);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findCartItemByProductIdAndCartId(anyLong(), anyLong())).thenReturn(null);

        APIException exception = assertThrows(APIException.class, () -> {
            cartService.addProductToCart(1L, 2);
        });

        assertEquals(product.getProductName() + " is not available", exception.getMessage());
    }

    @Test
    void testAddProductToCart_InsufficientQuantity() {
        when(authUtil.loggedInEmail()).thenReturn("test@example.com");
        when(cartRepository.findCartByEmail(anyString())).thenReturn(cart);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findCartItemByProductIdAndCartId(anyLong(), anyLong())).thenReturn(null);

        APIException exception = assertThrows(APIException.class, () -> {
            cartService.addProductToCart(1L, 20);
        });

        assertEquals("Please, make an order of the " + product.getProductName() + " less than or equal to the quantity " + product.getQuantity() + ".", exception.getMessage());
    }

//    @Test
//    void testUpdateProductQuantityInCart_ValidProduct() {
//        when(authUtil.loggedInEmail()).thenReturn("test@example.com");
//        when(cartRepository.findCartByEmail(anyString())).thenReturn(cart);
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//        when(cartItemRepository.findCartItemByProductIdAndCartId(anyLong(), anyLong())).thenReturn(cartItem);
//        when(modelMapper.map(any(Cart.class), eq(CartRequestDTO.class))).thenReturn(cartRequestDTO);
//
//        CartRequestDTO result = cartService.updateProductQuantityInCart(1L, 2);
//
//        assertNotNull(result);
//        assertEquals(1L, result.getCartId());
//        verify(cartRepository, times(1)).save(cart);
//        verify(cartItemRepository, times(1)).save(any(CartItem.class));
//    }

//    @Test
//    void testUpdateProductQuantityInCart_ProductNotAvailable() {
//        when(authUtil.loggedInEmail()).thenReturn("test@example.com");
//        when(cartRepository.findCartByEmail(anyString())).thenReturn(cart);
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//        when(cartItemRepository.findCartItemByProductIdAndCartId(anyLong(), anyLong())).thenReturn(null);
//
//        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
//            cartService.updateProductQuantityInCart(1L, 2);
//        });
//
//        assertEquals("Cart not found with cartId: 1.", exception.getMessage());
//    }

    @Test
    void testDeleteProductFromCart_ValidProduct() {
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(cartItemRepository.findCartItemByProductIdAndCartId(anyLong(), anyLong())).thenReturn(cartItem);

        String result = cartService.deleteProductFromCart(1L, 1L);

        assertEquals("Product " + product.getProductName() + " removed from the cart !!!", result);
        verify(cartItemRepository, times(1)).deleteCartItemByProductIdAndCartId(anyLong(), anyLong());
    }

    @Test
    void testDeleteProductFromCart_ProductNotInCart() {
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(cartItemRepository.findCartItemByProductIdAndCartId(anyLong(), anyLong())).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            cartService.deleteProductFromCart(1L, 1L);
        });

        assertEquals("Product not found with productId: 1.", exception.getMessage());
    }

    @Test
    void testGetAllCarts_NoCarts() {
        when(cartRepository.findAll()).thenReturn(new ArrayList<>());

        APIException exception = assertThrows(APIException.class, () -> {
            cartService.getAllCarts();
        });

        assertEquals("No cart exists", exception.getMessage());
    }

//    @Test
//    void testGetCartByEmail_ValidCart() {
//        when(cartRepository.findCartByEmail(anyString())).thenReturn(cart);
//        when(modelMapper.map(any(Cart.class), eq(CartRequestDTO.class))).thenReturn(cartRequestDTO);
//
//        CartRequestDTO result = cartService.getCartByEmail("test@example.com");
//
//        assertNotNull(result);
//        assertEquals(1L, result.getCartId());
//    }

    @Test
    void testGetCartByEmail_CartNotFound() {
        when(cartRepository.findCartByEmail(anyString())).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            cartService.getCartByEmail("test@example.com");
        });

        assertEquals("Cart not found with email: test@example.com.", exception.getMessage());
    }

    @Test
    void testUpdateProductInCarts_ValidProduct() {
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(cartItemRepository.findCartItemByProductIdAndCartId(anyLong(), anyLong())).thenReturn(cartItem);

        assertDoesNotThrow(() -> {
            cartService.updateProductInCarts(1L, 1L);
        });

        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testUpdateProductInCarts_ProductNotInCart() {
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(cartItemRepository.findCartItemByProductIdAndCartId(anyLong(), anyLong())).thenReturn(null);

        APIException exception = assertThrows(APIException.class, () -> {
            cartService.updateProductInCarts(1L, 1L);
        });

        assertEquals("Product " + product.getProductName() + " not available in the cart!!!", exception.getMessage());
    }
}
