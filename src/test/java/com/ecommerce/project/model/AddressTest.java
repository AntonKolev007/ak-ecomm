package com.ecommerce.project.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AddressTest {

    @Test
    public void testAddressDefaultConstructor() {
        Address address = new Address();
        assertNull(address.getAddressId());
        assertNull(address.getStreet());
        assertNull(address.getBuildingName());
        assertNull(address.getCity());
        assertNull(address.getState());
        assertNull(address.getCountry());
        assertNull(address.getZipCode());
        assertNull(address.getUser());
    }

    @Test
    public void testAddressAllArgConstructor() {
        User user = new User();
        Address address = new Address(1L, "123 Baker Street", "Building A", "Metropolis", "NY", "USA", "123456", user);

        assertEquals(1L, address.getAddressId());
        assertEquals("123 Baker Street", address.getStreet());
        assertEquals("Building A", address.getBuildingName());
        assertEquals("Metropolis", address.getCity());
        assertEquals("NY", address.getState());
        assertEquals("USA", address.getCountry());
        assertEquals("123456", address.getZipCode());
        assertEquals(user, address.getUser());
    }

    @Test
    public void testAddressCustomConstructor() {
        User user = new User();
        Address address = new Address("123 Baker Street", "Building A", "Metropolis", "NY", "USA", "123456", user);

        assertNull(address.getAddressId()); // addressId is not set in this constructor
        assertEquals("123 Baker Street", address.getStreet());
        assertEquals("Building A", address.getBuildingName());
        assertEquals("Metropolis", address.getCity());
        assertEquals("NY", address.getState());
        assertEquals("USA", address.getCountry());
        assertEquals("123456", address.getZipCode());
        assertEquals(user, address.getUser());
    }

    @Test
    public void testSettersAndGetters() {
        Address address = new Address();
        User user = new User();

        address.setAddressId(1L);
        assertEquals(1L, address.getAddressId());

        address.setStreet("123 Baker Street");
        assertEquals("123 Baker Street", address.getStreet());

        address.setBuildingName("Building A");
        assertEquals("Building A", address.getBuildingName());

        address.setCity("Metropolis");
        assertEquals("Metropolis", address.getCity());

        address.setState("NY");
        assertEquals("NY", address.getState());

        address.setCountry("USA");
        assertEquals("USA", address.getCountry());

        address.setZipCode("123456");
        assertEquals("123456", address.getZipCode());

        address.setUser(user);
        assertEquals(user, address.getUser());
    }

    @Test
    public void testToString() {
        Address address = new Address();
        address.setAddressId(1L);
        address.setStreet("123 Baker Street");
        address.setBuildingName("Building A");
        address.setCity("Metropolis");
        address.setState("NY");
        address.setCountry("USA");
        address.setZipCode("123456");

        String expected = "Address{addressId=1, street='123 Baker Street', buildingName='Building A', city='Metropolis', state='NY', country='USA', zipCode='123456', user='null'}";
        assertEquals(expected, address.toString());
    }
}
