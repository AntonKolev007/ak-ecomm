package com.ecommerce.project.service;

import com.ecommerce.project.payload.request.DiscountDTO;

public interface DiscountService {
    DiscountDTO addDiscount(DiscountDTO discountDTO, String token);
}
