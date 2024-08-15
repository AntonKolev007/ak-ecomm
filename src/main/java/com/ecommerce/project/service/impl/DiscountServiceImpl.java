package com.ecommerce.project.service.impl;

import com.ecommerce.project.payload.request.DiscountDTO;
import com.ecommerce.project.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${microservice.discount.url}")
    private String discountMicroserviceUrl;

    @Override
    public DiscountDTO addDiscount(DiscountDTO discountDTO, String token) {
        String url = discountMicroserviceUrl + "/api/discounts/add";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<DiscountDTO> request = new HttpEntity<>(discountDTO, headers);
        ResponseEntity<DiscountDTO> response = restTemplate.exchange(url, HttpMethod.POST, request, DiscountDTO.class);
        return response.getBody();
    }
}
