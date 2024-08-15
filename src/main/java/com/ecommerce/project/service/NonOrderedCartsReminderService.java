package com.ecommerce.project.service;

import java.util.List;

public interface NonOrderedCartsReminderService {
    void checkNonOrderedCarts();
    List<String> processNonOrderedCarts();
}
