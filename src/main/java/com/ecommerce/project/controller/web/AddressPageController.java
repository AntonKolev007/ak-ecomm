package com.ecommerce.project.controller.web;

import com.ecommerce.project.payload.request.AddressRequestDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AddressPageController {

    @GetMapping("/addresses")
    public String addresses(@RequestParam(value = "redirectTo", required = false) String redirectTo, Model model) {
        setAuthenticationAttributes(model);
        model.addAttribute("addressRequest", new AddressRequestDTO());
        if (redirectTo != null) {
            model.addAttribute("redirectTo", redirectTo);
        }
        return "addresses";
    }

    @GetMapping("/addresses-view")
    public String addressesView(Model model) {
        setAuthenticationAttributes(model);
        return "addresses-view";
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