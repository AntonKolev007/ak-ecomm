package com.ecommerce.project.security;

import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsImplTest {

    private User user;
    private UserDetailsImpl userDetails;

    @BeforeEach
    public void setUp() {
        Role userRole = new Role(AppRole.ROLE_USER);
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        user = new User();
        user.setUserId(1L);
        user.setUserName("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("testpassword");
        user.setRoles(roles);

        userDetails = UserDetailsImpl.build(user);
    }

    @Test
    public void testBuild() {
        assertNotNull(userDetails);
        assertEquals(user.getUserId(), userDetails.getId());
        assertEquals(user.getUserName(), userDetails.getUsername());
        assertEquals(user.getEmail(), userDetails.getEmail());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    public void testGettersAndSetters() {
        userDetails.setId(2L);
        userDetails.setUsername("anotheruser");
        userDetails.setEmail("anotheruser@example.com");
        userDetails.setPassword("anotherpassword");
        Collection<? extends GrantedAuthority> newAuthorities = Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        userDetails.setAuthorities(newAuthorities);

        assertEquals(2L, userDetails.getId());
        assertEquals("anotheruser", userDetails.getUsername());
        assertEquals("anotheruser@example.com", userDetails.getEmail());
        assertEquals("anotherpassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    public void testUserDetailsMethods() {
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    public void testEquals() {
        UserDetailsImpl userDetails1 = UserDetailsImpl.build(user);
        UserDetailsImpl userDetails2 = UserDetailsImpl.build(user);
        assertEquals(userDetails1, userDetails2);

        userDetails2.setId(3L);
        assertNotEquals(userDetails1, userDetails2);
    }
}
