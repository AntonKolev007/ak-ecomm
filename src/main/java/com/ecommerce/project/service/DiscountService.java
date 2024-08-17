package com.ecommerce.project.service;

import com.ecommerce.project.payload.request.DiscountDTO;

import java.util.List;

public interface DiscountService {
    List<DiscountDTO> getAllDiscounts();
    DiscountDTO getDiscountById(Long id);
    DiscountDTO createDiscount(DiscountDTO discountDTO);
    DiscountDTO updateDiscount(Long id, DiscountDTO discountDTO);
    void deleteDiscount(Long id);
}
