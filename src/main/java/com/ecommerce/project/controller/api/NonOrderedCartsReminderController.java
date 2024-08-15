package com.ecommerce.project.controller.api;

import com.ecommerce.project.service.NonOrderedCartsReminderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NonOrderedCartsReminderController {

    private final NonOrderedCartsReminderService nonOrderedCartsReminderService;

    public NonOrderedCartsReminderController(NonOrderedCartsReminderService nonOrderedCartsReminderService) {
        this.nonOrderedCartsReminderService = nonOrderedCartsReminderService;
    }

    @GetMapping("/simulate-email")
    public ResponseEntity<List<String>> simulateMail() {
        List<String> messages = nonOrderedCartsReminderService.processNonOrderedCarts();
        return ResponseEntity.ok(messages);
    }
}
