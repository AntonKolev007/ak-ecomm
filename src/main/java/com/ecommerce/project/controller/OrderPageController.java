package com.ecommerce.project.controller;

import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressRequestDTO;
import com.ecommerce.project.payload.OrderRequestDTO;
import com.ecommerce.project.service.AddressService;
import com.ecommerce.project.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class OrderPageController {

    private final AddressService addressService;
    private final AuthUtil authUtil;

    public OrderPageController(AddressService addressService, AuthUtil authUtil) {
        this.addressService = addressService;
        this.authUtil = authUtil;
    }

    @GetMapping("/order")
    public String order(Model model) {
        setAuthenticationAttributes(model);
        User user = authUtil.loggedInUser();
        List<AddressRequestDTO> addresses = addressService.getUserAddresses(user);
        model.addAttribute("addresses", addresses);
        model.addAttribute("orderRequest", new OrderRequestDTO());
        return "orders";
    }

    @PostMapping("/order")
    public String placeOrder(@Valid @ModelAttribute("orderRequest") OrderRequestDTO orderRequest,
                             BindingResult bindingResult, Model model) {
        setAuthenticationAttributes(model);
        if (bindingResult.hasErrors()) {
            User user = authUtil.loggedInUser();
            List<AddressRequestDTO> addresses = addressService.getUserAddresses(user);
            model.addAttribute("addresses", addresses);
            return "orders";
        }
        // Call order service or process order here
        return "redirect:/orderConfirmation"; // Replace with appropriate URL after placing order
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