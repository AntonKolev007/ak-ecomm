package com.ecommerce.project.util;

import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthUtilTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthUtil authUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testLoggedInEmail_Success() {
        User user = new User();
        user.setUserName("testUser");
        user.setEmail("test@example.com");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));

        String email = authUtil.loggedInEmail();

        assertEquals("test@example.com", email);
        verify(userRepository, times(1)).findByUserName("testUser");
    }

    @Test
    public void testLoggedInEmail_UserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authUtil.loggedInEmail());
        verify(userRepository, times(1)).findByUserName("testUser");
    }

    @Test
    public void testLoggedInUserId_Success() {
        User user = new User();
        user.setUserName("testUser");
        user.setUserId(1L);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));

        Long userId = authUtil.loggedInUserId();

        assertEquals(1L, userId);
        verify(userRepository, times(1)).findByUserName("testUser");
    }

    @Test
    public void testLoggedInUserId_UserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authUtil.loggedInUserId());
        verify(userRepository, times(1)).findByUserName("testUser");
    }

    @Test
    public void testLoggedInUser_Success() {
        User user = new User();
        user.setUserName("testUser");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));

        User loggedInUser = authUtil.loggedInUser();

        assertEquals(user, loggedInUser);
        verify(userRepository, times(1)).findByUserName("testUser");
    }

    @Test
    public void testLoggedInUser_UserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authUtil.loggedInUser());
        verify(userRepository, times(1)).findByUserName("testUser");
    }
}
