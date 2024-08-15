package com.ecommerce.project.controller.api;

import com.ecommerce.project.service.impl.ScheduledTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ScheduledTaskController {

    private final ScheduledTaskService scheduledTaskService;

    public ScheduledTaskController(ScheduledTaskService scheduledTaskService) {
        this.scheduledTaskService = scheduledTaskService;
    }

    @GetMapping("/simulate-email")
    public ResponseEntity<List<String>> simulateMail() {
        List<String> messages = scheduledTaskService.processNonOrderedCarts();
        return ResponseEntity.ok(messages);
    }
}
