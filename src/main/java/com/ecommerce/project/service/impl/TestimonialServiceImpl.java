package com.ecommerce.project.service.impl;

import com.ecommerce.project.model.Testimonial;
import com.ecommerce.project.payload.TestimonialDTO;
import com.ecommerce.project.repositories.TestimonialRepository;
import com.ecommerce.project.service.TestimonialService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestimonialServiceImpl implements TestimonialService {

    private final TestimonialRepository testimonialRepository;

    public TestimonialServiceImpl(TestimonialRepository testimonialRepository) {
        this.testimonialRepository = testimonialRepository;
    }

    @Override
    public List<TestimonialDTO> getAllTestimonials() {
        List<Testimonial> testimonials = testimonialRepository.findAll();
        return testimonials.stream()
                .map(testimonial -> new TestimonialDTO(testimonial.getId(), testimonial.getText(), testimonial.getAuthor()))
                .collect(Collectors.toList());
    }
}
