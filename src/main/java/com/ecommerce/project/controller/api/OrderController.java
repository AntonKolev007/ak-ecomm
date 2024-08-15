package com.ecommerce.project.controller.api;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.payload.request.OrderDTO;
import com.ecommerce.project.payload.request.OrderRequestDTO;
import com.ecommerce.project.service.OrderService;
import com.ecommerce.project.util.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;
    private final AuthUtil authUtil;

    public OrderController(OrderService orderService, AuthUtil authUtil) {
        this.orderService = orderService;
        this.authUtil = authUtil;
    }

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<Object> orderProducts(@PathVariable String paymentMethod,
                                                @RequestBody OrderRequestDTO orderRequestDTO) {
        if (!authUtil.isAuthenticated()) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized: No active session."), HttpStatus.UNAUTHORIZED);
        }

        try {
            String emailId = authUtil.loggedInEmail();
            OrderDTO order = orderService.placeOrder(
                    emailId,
                    orderRequestDTO.getAddressId(),
                    paymentMethod,
                    orderRequestDTO.getPgName(),
                    orderRequestDTO.getPgPaymentId(),
                    orderRequestDTO.getPgStatus(),
                    orderRequestDTO.getPgResponseMessage()
            );
            return new ResponseEntity<>(order, HttpStatus.CREATED);
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