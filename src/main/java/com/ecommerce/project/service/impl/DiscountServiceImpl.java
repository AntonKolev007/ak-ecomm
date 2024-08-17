package com.ecommerce.project.service.impl;


import com.ecommerce.project.payload.request.DiscountDTO;
import com.ecommerce.project.service.DiscountService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final RestTemplate restTemplate;

    public DiscountServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<DiscountDTO> getAllDiscounts() {
        try {
            String url = "http://localhost:8081/api/discount";
            return restTemplate.getForObject(url, List.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to fetch discounts: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
        }
    }

    @Override
    public DiscountDTO getDiscountById(Long id) {
        try {
            String url = "http://localhost:8081/api/discount/" + id;
            return restTemplate.getForObject(url, DiscountDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to fetch discount: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
        }
    }

    @Override
    public DiscountDTO createDiscount(DiscountDTO discountDTO) {
        try {
            String url = "http://localhost:8081/api/discount";
            return restTemplate.postForObject(url, discountDTO, DiscountDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to create discount: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
        }
    }

    @Override
    public DiscountDTO updateDiscount(Long id, DiscountDTO discountDTO) {
        try {
            String url = "http://localhost:8081/api/discount/" + id;
            restTemplate.put(url, discountDTO);
            return discountDTO;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to update discount: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
        }
    }

    @Override
    public void deleteDiscount(Long id) {
        try {
            String url = "http://localhost:8081/api/discount/" + id;
            restTemplate.delete(url);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to delete discount: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
        }
    }
}
