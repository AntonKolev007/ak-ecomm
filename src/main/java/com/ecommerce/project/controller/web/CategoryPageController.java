package com.ecommerce.project.controller.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryPageController {

    @GetMapping("/categories")
    public String categories(Model model) {
        setAuthenticationAttributes(model);
        return "categories";
    }

    @GetMapping("/categories/category")
    public String category(Model model) {
        setAuthenticationAttributes(model);
        return "categories";
    }

    private void setAuthenticationAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName());
        model.addAttribute("isLoggedIn", isLoggedIn);
        if (isLoggedIn) {
            model.addAttribute("username", authentication.getName());
        }
    }
}
