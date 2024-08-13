package com.ecommerce.project.init;

import com.ecommerce.project.model.Testimonial;
import com.ecommerce.project.repositories.TestimonialRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestimonialInitializer {

    @Bean
    public CommandLineRunner initTestimonials(TestimonialRepository testimonialRepository) {
        return args -> {
            if (testimonialRepository.count() == 0) {
                Testimonial testimonial1 = new Testimonial("Great product and excellent service!", "John Doe");
                Testimonial testimonial2 = new Testimonial("I am very satisfied with my purchase.", "Jane Smith");
                Testimonial testimonial3 = new Testimonial("The best webshop experience I've ever had.", "Alice Johnson");
                Testimonial testimonial4 = new Testimonial("High-quality products at reasonable prices.", "Bob Brown");

                testimonialRepository.save(testimonial1);
                testimonialRepository.save(testimonial2);
                testimonialRepository.save(testimonial3);
                testimonialRepository.save(testimonial4);
            }
        };
    }
}
