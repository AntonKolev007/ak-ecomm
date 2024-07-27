package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.*;
import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.payload.OrderItemDTO;
import com.ecommerce.project.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CartService cartService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private Cart cart;
    private Address address;
    private Order order;
    private Payment payment;
    private List<CartItem> cartItems;
    private List<OrderItem> orderItems;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setEmail("test@example.com");

        cartItems = new ArrayList<>();
        cart = new Cart(1L, user, cartItems, 100.0);

        address = new Address();
        address.setAddressId(1L);

        order = new Order();
        order.setOrderId(1L);
        order.setEmail("test@example.com");
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(100.0);
        order.setOrderStatus("Order Accepted!");
        order.setAddress(address);

        payment = new Payment("credit_card", "pgPaymentId", "pgStatus", "pgResponseMessage", "pgName");
        payment.setPaymentId(1L);
        payment.setOrder(order);

        orderItems = new ArrayList<>();
    }

    @Test
    void testPlaceOrder_Success() {
        // Adding a cart item to ensure the cart is not empty
        Product product = new Product();
        product.setProductId(1L);
        product.setQuantity(10);
        product.setPrice(50.0);

        CartItem cartItem = new CartItem();
        cartItem.setCartItemId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setDiscount(0.0);
        cartItem.setProductPrice(50.0);

        cartItems.add(cartItem);
        cart.setCartItems(cartItems);
        cart.setTotalPrice(100.0);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(1L);
        orderItem.setProduct(product);
        orderItem.setQuantity(2);
        orderItem.setDiscount(0.0);
        orderItem.setOrderedProductPrice(50.0);
        orderItem.setOrder(order);

        orderItems.add(orderItem);

        when(cartRepository.findCartByEmail(anyString())).thenReturn(cart);
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderItemRepository.saveAll(anyList())).thenReturn(orderItems);
        when(modelMapper.map(any(Order.class), eq(OrderDTO.class))).thenReturn(new OrderDTO());
        when(modelMapper.map(any(OrderItem.class), eq(OrderItemDTO.class))).thenReturn(new OrderItemDTO());

        OrderDTO result = orderService.placeOrder("test@example.com", 1L, "credit_card", "pgName", "pgPaymentId", "pgStatus", "pgResponseMessage");

        assertNotNull(result);
        verify(cartRepository, times(1)).findCartByEmail(anyString());
        verify(addressRepository, times(1)).findById(anyLong());
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).saveAll(anyList());
        verify(cartService, times(cartItems.size())).deleteProductFromCart(anyLong(), anyLong());
    }


    @Test
    void testPlaceOrder_CartNotFound() {
        when(cartRepository.findCartByEmail(anyString())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.placeOrder("test@example.com", 1L, "credit_card", "pgName", "pgPaymentId", "pgStatus", "pgResponseMessage");
        });

        verify(cartRepository, times(1)).findCartByEmail(anyString());
        verify(addressRepository, times(0)).findById(anyLong());
    }

    @Test
    void testPlaceOrder_AddressNotFound() {
        when(cartRepository.findCartByEmail(anyString())).thenReturn(cart);
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.placeOrder("test@example.com", 1L, "credit_card", "pgName", "pgPaymentId", "pgStatus", "pgResponseMessage");
        });

        verify(cartRepository, times(1)).findCartByEmail(anyString());
        verify(addressRepository, times(1)).findById(anyLong());
    }

    @Test
    void testPlaceOrder_CartIsEmpty() {
        cart.setCartItems(new ArrayList<>());
        when(cartRepository.findCartByEmail(anyString())).thenReturn(cart);
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));

        assertThrows(APIException.class, () -> {
            orderService.placeOrder("test@example.com", 1L, "credit_card", "pgName", "pgPaymentId", "pgStatus", "pgResponseMessage");
        });

        verify(cartRepository, times(1)).findCartByEmail(anyString());
        verify(addressRepository, times(1)).findById(anyLong());
    }
}
