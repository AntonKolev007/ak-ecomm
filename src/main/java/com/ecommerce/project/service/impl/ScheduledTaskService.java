//package com.ecommerce.project.service.impl;
//
//import com.ecommerce.project.model.Cart;
//import com.ecommerce.project.repositories.CartRepository;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class ScheduledTaskService {
//
//    private final CartRepository cartRepository;
//
//    public ScheduledTaskService(CartRepository cartRepository) {
//        this.cartRepository = cartRepository;
//    }
//
//    @Scheduled(cron = "0 * * * * ?") // Runs every minute
//    public void checkNonOrderedCarts() {
//        processNonOrderedCarts();
//    }
//
//    public List<String> processNonOrderedCarts() {
//        List<Cart> nonOrderedCarts = cartRepository.findNonOrderedCarts();
//        List<String> messages = new ArrayList<>();
//
//        if (nonOrderedCarts.isEmpty()) {
//            String message = "No non-ordered carts found.";
//            System.out.println(message);
//            messages.add(message);
//            return messages;
//        }
//
//        nonOrderedCarts.forEach(cart -> {
//            String emailId = cart.getUser().getEmail();
//            String message = "Sending reminder to " + emailId + ": You have items in your cart waiting to be ordered.";
//            System.out.println(message);
//            messages.add(message);
//        });
//
//        return messages;
//    }
//}
