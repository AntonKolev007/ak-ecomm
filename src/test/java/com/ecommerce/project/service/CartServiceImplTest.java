package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.model.User;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

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

    private User user;
    private Cart cart;
    private CartItem cartItem;
    private Product product;
    private CartRequestDTO cartRequestDTO;
    private ProductRequestDTO productRequestDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setUserName("testUser");
        user.setEmail("test@example.com");

        cart = new Cart();
        cart.setCartId(1L);
        cart.setTotalPrice(0.00);
        cart.setUser(user);

        product = new Product();
        product.setProductId(1L);
        product.setProductName("Test Product");
        product.setQuantity(10);
        product.setSpecialPrice(100.00);
        product.setDiscount(0.10);

        cartItem = new CartItem();
        cartItem.setCartItemId(1L);
        cartItem.setProduct(product);
        cartItem.setCart(cart);
        cartItem.setQuantity(1);
        cartItem.setProductPrice(product.getSpecialPrice());
        cartItem.setDiscount(product.getDiscount());

        cart.setCartItems(new ArrayList<>(List.of(cartItem)));

        cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setProducts(new ArrayList<>());

        productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setProductId(product.getProductId());
        productRequestDTO.setProductName(product.getProductName());
        productRequestDTO.setQuantity(cartItem.getQuantity());
    }

    @Test
    void testAddProductToCart() {
        when(authUtil.loggedInEmail()).thenReturn(user.getEmail());
        when(cartRepository.findCartByEmail(user.getEmail())).thenReturn(null);
        when(authUtil.loggedInUser()).thenReturn(user);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(productRepository.findById(product.getProductId())).thenReturn(Optional.of(product));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(modelMapper.map(cart, CartRequestDTO.class)).thenReturn(cartRequestDTO);
        when(modelMapper.map(product, ProductRequestDTO.class)).thenReturn(productRequestDTO);

        CartRequestDTO result = cartService.addProductToCart(product.getProductId(), 1);

        assertNotNull(result);
        verify(cartRepository, times(2)).save(any(Cart.class)); // Adjusted to times(2)
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testGetAllCarts() {
        when(cartRepository.findAll()).thenReturn(List.of(cart));
        when(modelMapper.map(cart, CartRequestDTO.class)).thenReturn(cartRequestDTO);
        when(modelMapper.map(cartItem.getProduct(), ProductRequestDTO.class)).thenReturn(productRequestDTO);

        List<CartRequestDTO> result = cartService.getAllCarts();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(cartRepository, times(1)).findAll();
    }

    @Test
    void testGetCart() {
        when(cartRepository.findCartByEmailAndCartId(user.getEmail(), cart.getCartId())).thenReturn(cart);
        when(modelMapper.map(cart, CartRequestDTO.class)).thenReturn(cartRequestDTO);
        when(modelMapper.map(cartItem.getProduct(), ProductRequestDTO.class)).thenReturn(productRequestDTO);

        CartRequestDTO result = cartService.getCart(user.getEmail(), cart.getCartId());

        assertNotNull(result);
        verify(cartRepository, times(1)).findCartByEmailAndCartId(user.getEmail(), cart.getCartId());
    }

    @Test
    void testUpdateProductQuantityInCart() {
        when(authUtil.loggedInEmail()).thenReturn(user.getEmail());
        when(cartRepository.findCartByEmail(user.getEmail())).thenReturn(cart);
        when(cartRepository.findById(cart.getCartId())).thenReturn(Optional.of(cart));
        when(productRepository.findById(product.getProductId())).thenReturn(Optional.of(product));
        when(cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), product.getProductId())).thenReturn(cartItem);
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem); // Ensure save returns a non-null value
        when(modelMapper.map(cart, CartRequestDTO.class)).thenReturn(cartRequestDTO);
        when(modelMapper.map(product, ProductRequestDTO.class)).thenReturn(productRequestDTO);

        CartRequestDTO result = cartService.updateProductQuantityInCart(product.getProductId(), 1);

        assertNotNull(result);
        verify(cartItemRepository, times(1)).save(cartItem);
    }

    @Test
    void testUpdateProductQuantityInCart_ProductNotAvailable() {
        when(authUtil.loggedInEmail()).thenReturn(user.getEmail());
        when(cartRepository.findCartByEmail(user.getEmail())).thenReturn(cart);
        when(cartRepository.findById(cart.getCartId())).thenReturn(Optional.of(cart));
        when(productRepository.findById(product.getProductId())).thenReturn(Optional.of(product));

        product.setQuantity(0);

        APIException exception = assertThrows(APIException.class, () -> {
            cartService.updateProductQuantityInCart(product.getProductId(), 1);
        });

        assertEquals("Test Product is not available", exception.getMessage());
    }

    @Test
    void testUpdateProductQuantityInCart_ProductQuantityLessThanOrder() {
        when(authUtil.loggedInEmail()).thenReturn(user.getEmail());
        when(cartRepository.findCartByEmail(user.getEmail())).thenReturn(cart);
        when(cartRepository.findById(cart.getCartId())).thenReturn(Optional.of(cart));
        when(productRepository.findById(product.getProductId())).thenReturn(Optional.of(product));

        product.setQuantity(5);

        APIException exception = assertThrows(APIException.class, () -> {
            cartService.updateProductQuantityInCart(product.getProductId(), 10);
        });

        assertEquals("Please, make an order of the Test Product less than or equal to the quantity 5.", exception.getMessage());
    }

    @Test
    void testUpdateProductQuantityInCart_CartItemNotAvailable() {
        when(authUtil.loggedInEmail()).thenReturn(user.getEmail());
        when(cartRepository.findCartByEmail(user.getEmail())).thenReturn(cart);
        when(cartRepository.findById(cart.getCartId())).thenReturn(Optional.of(cart));
        when(productRepository.findById(product.getProductId())).thenReturn(Optional.of(product));
        when(cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), product.getProductId())).thenReturn(null);

        APIException exception = assertThrows(APIException.class, () -> {
            cartService.updateProductQuantityInCart(product.getProductId(), 1);
        });

        assertEquals("Product Test Product not available in the cart!!!", exception.getMessage());
    }

    @Test
    void testUpdateProductQuantityInCart_QuantityLessThanZero() {
        when(authUtil.loggedInEmail()).thenReturn(user.getEmail());
        when(cartRepository.findCartByEmail(user.getEmail())).thenReturn(cart);
        when(cartRepository.findById(cart.getCartId())).thenReturn(Optional.of(cart));
        when(productRepository.findById(product.getProductId())).thenReturn(Optional.of(product));
        when(cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), product.getProductId())).thenReturn(cartItem);

        APIException exception = assertThrows(APIException.class, () -> {
            cartService.updateProductQuantityInCart(product.getProductId(), -10);
        });

        assertEquals("The quantity cannot be negative!", exception.getMessage());
    }

    @Test
    void testDeleteProductFromCart() {
        when(cartRepository.findById(cart.getCartId())).thenReturn(Optional.of(cart));
        when(cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), product.getProductId())).thenReturn(cartItem);

        String result = cartService.deleteProductFromCart(cart.getCartId(), product.getProductId());

        assertNotNull(result);
        verify(cartItemRepository, times(1)).deleteCartItemByProductIdAndCartId(cart.getCartId(), product.getProductId());
    }

    @Test
    void testUpdateProductInCarts() {
        when(cartRepository.findById(cart.getCartId())).thenReturn(Optional.of(cart));
        when(productRepository.findById(product.getProductId())).thenReturn(Optional.of(product));
        when(cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), product.getProductId())).thenReturn(cartItem);

        cartService.updateProductInCarts(cart.getCartId(), product.getProductId());

        verify(cartItemRepository, times(1)).save(cartItem);
    }

    @Test
    void testUpdateProductQuantityInCart_NewQuantityIsZero() {
        when(authUtil.loggedInEmail()).thenReturn(user.getEmail());
        when(cartRepository.findCartByEmail(user.getEmail())).thenReturn(cart);
        when(cartRepository.findById(cart.getCartId())).thenReturn(Optional.of(cart));
        when(productRepository.findById(product.getProductId())).thenReturn(Optional.of(product));
        when(cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), product.getProductId())).thenReturn(cartItem);

        // Mock the return value of the save method to ensure it returns a non-null CartItem
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(modelMapper.map(cart, CartRequestDTO.class)).thenReturn(cartRequestDTO);
        when(modelMapper.map(any(Product.class), eq(ProductRequestDTO.class))).thenReturn(productRequestDTO);

        cartService.updateProductQuantityInCart(product.getProductId(), -1);

        // Verify the call to deleteCartItemByProductIdAndCartId instead of deleteById
        verify(cartItemRepository, times(1)).deleteCartItemByProductIdAndCartId(cart.getCartId(), product.getProductId());
    }
    @Test
    void testUpdateProductQuantityInCart_UpdatedItemQuantityZero() {
        // Mocking dependencies
        when(authUtil.loggedInEmail()).thenReturn(user.getEmail());
        when(cartRepository.findCartByEmail(user.getEmail())).thenReturn(cart);
        when(cartRepository.findById(cart.getCartId())).thenReturn(Optional.of(cart));
        when(productRepository.findById(product.getProductId())).thenReturn(Optional.of(product));
        when(cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), product.getProductId())).thenReturn(cartItem);

        // Mock the modelMapper to return appropriate DTOs
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setProductId(product.getProductId());
        productRequestDTO.setQuantity(0);

        when(modelMapper.map(any(Product.class), eq(ProductRequestDTO.class))).thenReturn(productRequestDTO);

        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        when(modelMapper.map(any(Cart.class), eq(CartRequestDTO.class))).thenReturn(cartRequestDTO);

        // Set cartItem quantity to zero and ensure save returns this cartItem
        cartItem.setQuantity(0);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        // Invoke the method under test
        cartService.updateProductQuantityInCart(product.getProductId(), -cartItem.getQuantity());

        // Verify interactions
        verify(cartItemRepository, times(1)).deleteById(cartItem.getCartItemId());
    }





    @Test
    void testGetCart_CartNotFound() {
        when(cartRepository.findCartByEmailAndCartId(user.getEmail(), 1L)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            cartService.getCart(user.getEmail(), 1L);
        });

        assertEquals("Cart not found with cartId: 1.", exception.getMessage());
    }

    @Test
    void testGetAllCarts_NoCartsExist() {
        when(cartRepository.findAll()).thenReturn(Collections.emptyList());

        APIException exception = assertThrows(APIException.class, () -> {
            cartService.getAllCarts();
        });

        assertEquals("No cart exists", exception.getMessage());
    }

    @Test
    void testAddProductToCart_CartItemExists() {
        when(authUtil.loggedInEmail()).thenReturn(user.getEmail());
        when(cartRepository.findCartByEmail(user.getEmail())).thenReturn(cart);
        when(productRepository.findById(product.getProductId())).thenReturn(Optional.of(product));
        when(cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), product.getProductId())).thenReturn(cartItem);

        APIException exception = assertThrows(APIException.class, () -> {
            cartService.addProductToCart(product.getProductId(), 1);
        });

        assertEquals("Product " + product.getProductName() + " already exists in the cart", exception.getMessage());
    }

    @Test
    void testAddProductToCart_ProductQuantityZero() {
        product.setQuantity(0);

        when(authUtil.loggedInEmail()).thenReturn(user.getEmail());
        when(cartRepository.findCartByEmail(user.getEmail())).thenReturn(cart);
        when(productRepository.findById(product.getProductId())).thenReturn(Optional.of(product));
        when(cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), product.getProductId())).thenReturn(null);

        APIException exception = assertThrows(APIException.class, () -> {
            cartService.addProductToCart(product.getProductId(), 1);
        });

        assertEquals(product.getProductName() + " is not available", exception.getMessage());
    }

    @Test
    void testAddProductToCart_QuantityExceedsProductStock() {
        product.setQuantity(5);

        when(authUtil.loggedInEmail()).thenReturn(user.getEmail());
        when(cartRepository.findCartByEmail(user.getEmail())).thenReturn(cart);
        when(productRepository.findById(product.getProductId())).thenReturn(Optional.of(product));
        when(cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), product.getProductId())).thenReturn(null);

        APIException exception = assertThrows(APIException.class, () -> {
            cartService.addProductToCart(product.getProductId(), 10);
        });

        assertEquals("Please, make an order of the " + product.getProductName() + " less than or equal to the quantity " + product.getQuantity() + ".", exception.getMessage());
    }

}
