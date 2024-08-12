package com.ecommerce.project.repository;

import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.repositories.CartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

//    @Test
//    public void testFindNonOrderedCarts() {
//        List<Cart> nonOrderedCarts = cartRepository.findNonOrderedCarts();
//        assertFalse(nonOrderedCarts.isEmpty(), "There should be non-ordered carts with items");
//    }
}
