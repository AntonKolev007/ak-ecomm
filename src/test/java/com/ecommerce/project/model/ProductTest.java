package com.ecommerce.project.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    public void testProductDefaultConstructor() {
        Product product = new Product();
        assertNull(product.getProductId());
        assertNull(product.getProductName());
        assertNull(product.getImage());
        assertNull(product.getDescription());
        assertNull(product.getQuantity());
        assertEquals(0.0, product.getPrice());
        assertEquals(0.0, product.getDiscount());
        assertEquals(0.0, product.getSpecialPrice());
        assertNull(product.getCategory());
        assertNull(product.getUser());
        assertNotNull(product.getProducts());
        assertTrue(product.getProducts().isEmpty());
    }

    @Test
    public void testProductParameterizedConstructor() {
        Category category = new Category();
        User user = new User();
        List<CartItem> products = new ArrayList<>();

        Product product = new Product(1L, "Laptop", "image.png", "High-end gaming laptop", 10, 1500.0, 10.0, 1350.0, category, user, products);

        assertEquals(1L, product.getProductId());
        assertEquals("Laptop", product.getProductName());
        assertEquals("image.png", product.getImage());
        assertEquals("High-end gaming laptop", product.getDescription());
        assertEquals(10, product.getQuantity());
        assertEquals(1500.0, product.getPrice());
        assertEquals(10.0, product.getDiscount());
        assertEquals(1350.0, product.getSpecialPrice());
        assertEquals(category, product.getCategory());
        assertEquals(user, product.getUser());
        assertEquals(products, product.getProducts());
    }

    @Test
    public void testSettersAndGetters() {
        Product product = new Product();

        product.setProductId(1L);
        assertEquals(1L, product.getProductId());

        product.setProductName("Laptop");
        assertEquals("Laptop", product.getProductName());

        product.setImage("image.png");
        assertEquals("image.png", product.getImage());

        product.setDescription("High-end gaming laptop");
        assertEquals("High-end gaming laptop", product.getDescription());

        product.setQuantity(10);
        assertEquals(10, product.getQuantity());

        product.setPrice(1500.0);
        assertEquals(1500.0, product.getPrice());

        product.setDiscount(10.0);
        assertEquals(10.0, product.getDiscount());

        product.setSpecialPrice(1350.0);
        assertEquals(1350.0, product.getSpecialPrice());

        Category category = new Category();
        product.setCategory(category);
        assertEquals(category, product.getCategory());

        User user = new User();
        product.setUser(user);
        assertEquals(user, product.getUser());

        List<CartItem> products = new ArrayList<>();
        product.setProducts(products);
        assertEquals(products, product.getProducts());
    }

    @Test
    public void testToString() {
        Product product = new Product();
        product.setProductId(1L);
        product.setProductName("Laptop");
        product.setImage("image.png");
        product.setDescription("High-end gaming laptop");
        product.setQuantity(10);
        product.setPrice(1500.0);
        product.setDiscount(10.0);
        product.setSpecialPrice(1350.0);

        Category category = new Category();
        User user = new User();

        product.setCategory(category);
        product.setUser(user);

        String expected = "Product{productId=1, productName='Laptop', image='image.png', description='High-end gaming laptop', quantity=10, price=1500.0, discount=10.0, specialPrice=1350.0, category=" + category + ", user=" + user + "}";
        assertEquals(expected, product.toString());
    }
}
