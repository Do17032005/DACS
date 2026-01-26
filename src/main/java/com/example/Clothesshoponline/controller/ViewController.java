package com.example.Clothesshoponline.controller;

import com.example.Clothesshoponline.repository.UserRepository;
import com.example.Clothesshoponline.service.OrderService;
import com.example.Clothesshoponline.service.ProductService;
import com.example.Clothesshoponline.service.RecommendationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    private final ProductService productService;

    private final RecommendationService recommendationService;
    private final UserRepository userRepository;
    private final OrderService orderService;

    public ViewController(ProductService productService, RecommendationService recommendationService,
            UserRepository userRepository, OrderService orderService) {
        this.productService = productService;
        this.recommendationService = recommendationService;
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productService.getAllProducts());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            userRepository.findByUsername(auth.getName()).ifPresent(user -> {
                model.addAttribute("recommendations", recommendationService.getRecommendationsForUser(user.getId()));
            });
        }

        return "index";
    }

    @GetMapping("/product/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
        model.addAttribute("product",
                productService.getProductById(id).orElseThrow(() -> new RuntimeException("Product not found")));
        return "product-detail";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            userRepository.findByUsername(auth.getName()).ifPresent(user -> {
                model.addAttribute("orders", orderService.getOrdersByUserId(user.getId()));
                model.addAttribute("user", user);
            });
        }
        return "orders";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            userRepository.findByUsername(auth.getName()).ifPresent(user -> {
                model.addAttribute("user", user);
            });
        }
        return "profile";
    }

    @GetMapping("/cart")
    public String cart() {
        return "cart";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    }

    @GetMapping("/vouchers")
    public String vouchers() {
        return "vouchers";
    }

    @GetMapping("/membership")
    public String membership() {
        return "membership";
    }

    @GetMapping("/address")
    public String address() {
        return "address";
    }

    @GetMapping("/products")
    public String products(@RequestParam(value = "q", required = false) String query, Model model) {
        model.addAttribute("products", productService.searchProducts(query));
        model.addAttribute("query", query);
        return "products";
    }

    @GetMapping("/chat")
    public String chat() {
        return "chat";
    }
}
