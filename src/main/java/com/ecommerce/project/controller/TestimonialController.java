package com.ecommerce.project.controller;

import com.ecommerce.project.payload.TestimonialDTO;
import com.ecommerce.project.service.TestimonialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/testimonials")
public class TestimonialController {

    private final TestimonialService testimonialService;

    public TestimonialController(TestimonialService testimonialService) {
        this.testimonialService = testimonialService;
    }

    @GetMapping
    public ResponseEntity<List<TestimonialDTO>> getAllTestimonials() {
        List<TestimonialDTO> testimonials = testimonialService.getAllTestimonials();
        return ResponseEntity.ok(testimonials);
    }
}