package com.ecommerce.project.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    @Test
    public void testCategoryDefaultConstructor() {
        Category category = new Category();
        assertNull(category.getCategoryId());
        assertNull(category.getCategoryName());
        assertNull(category.getProducts()); // Checking for null instead of empty list
    }

    @Test
    public void testCategoryParameterizedConstructor() {
        Category category = new Category(1L, "Electronics");
        assertEquals(1L, category.getCategoryId());
        assertEquals("Electronics", category.getCategoryName());
        assertEquals(0,category.getProducts().size()); // Expect null because constructor does not initialize list
    }

    @Test
    public void testSettersAndGetters() {
        Category category = new Category();
        category.setCategoryId(1L);
        assertEquals(1L, category.getCategoryId());

        category.setCategoryName("Gadgets");
        assertEquals("Gadgets", category.getCategoryName());

        List<Product> products = new ArrayList<>();
        products.add(new Product());
        category.setProducts(products);
        assertNotNull(category.getProducts()); // Should not be null after setting
        assertEquals(products, category.getProducts());
        assertEquals(1, category.getProducts().size());
    }
}
