package com.ecommerce.project.controller.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductPageController {

    @GetMapping("/products")
    public String products(Model model) {
        setAuthenticationAttributes(model);
        return "products";
    }

    @GetMapping("/products/product")
    public String product(Model model) {
        setAuthenticationAttributes(model);
        return "product";
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