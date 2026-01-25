package com.example.Clothesshoponline.controller;

import com.example.Clothesshoponline.model.User;
import com.example.Clothesshoponline.repository.UserRepository;
import com.example.Clothesshoponline.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class OrderController {

    private final OrderService orderService;

    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @PostMapping("/order/place")
    public String placeOrder(@RequestParam Long productId, @RequestParam(defaultValue = "1") int quantity) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Long userId = userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            orderService.placeOrder(userId, productId, quantity);
            return "redirect:/?success=Order placed successfully";
        } catch (Exception e) {
            return "redirect:/?error=" + e.getMessage();
        }
    }
}
