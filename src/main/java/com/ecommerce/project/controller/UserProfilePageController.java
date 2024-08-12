package com.ecommerce.project.controller;

import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.security.services.UserDetailsImpl;
import com.ecommerce.project.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserProfilePageController {

    private final UserService userService;

    public UserProfilePageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName());
        model.addAttribute("isLoggedIn", isLoggedIn);
        if (!isLoggedIn) {
            return "redirect:/login";
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userService.getUserById(userDetails.getId());

        model.addAttribute("id", user.getUserId());
        model.addAttribute("username", user.getUserName());
        model.addAttribute("roles", user.getRoles().stream().map(Role::getRoleName).toList());

        return "profile";
    }
}