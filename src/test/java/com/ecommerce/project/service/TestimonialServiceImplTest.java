package com.ecommerce.project.service;

import com.ecommerce.project.model.Testimonial;
import com.ecommerce.project.payload.request.TestimonialDTO;
import com.ecommerce.project.repositories.TestimonialRepository;
import com.ecommerce.project.service.impl.TestimonialServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestimonialServiceImplTest {

    @Mock
    private TestimonialRepository testimonialRepository;

    @InjectMocks
    private TestimonialServiceImpl testimonialService;

    private Testimonial testimonial1;
    private Testimonial testimonial2;

    @BeforeEach
    void setUp() {
        testimonial1 = new Testimonial();
        testimonial1.setId(1L);
        testimonial1.setText("Great service!");
        testimonial1.setAuthor("John Doe");

        testimonial2 = new Testimonial();
        testimonial2.setId(2L);
        testimonial2.setText("Loved the products!");
        testimonial2.setAuthor("Jane Smith");
    }

    @Test
    void testGetAllTestimonials() {
        when(testimonialRepository.findAll()).thenReturn(Arrays.asList(testimonial1, testimonial2));

        List<TestimonialDTO> testimonials = testimonialService.getAllTestimonials();

        assertEquals(2, testimonials.size());
        assertEquals("Great service!", testimonials.get(0).getText());
        assertEquals("John Doe", testimonials.get(0).getAuthor());
        assertEquals("Loved the products!", testimonials.get(1).getText());
        assertEquals("Jane Smith", testimonials.get(1).getAuthor());

        verify(testimonialRepository, times(1)).findAll();
    }
}
