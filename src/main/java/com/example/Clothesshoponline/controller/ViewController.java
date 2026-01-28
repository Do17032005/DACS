package com.example.Clothesshoponline.controller;

import com.example.Clothesshoponline.model.CartItem;
import com.example.Clothesshoponline.model.User;
import com.example.Clothesshoponline.model.Voucher;
import com.example.Clothesshoponline.repository.SavedVoucherRepository;
import com.example.Clothesshoponline.service.CartService;
import com.example.Clothesshoponline.service.ProductService;
import com.example.Clothesshoponline.service.UserService;
import com.example.Clothesshoponline.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ViewController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private SavedVoucherRepository savedVoucherRepository;

    @GetMapping("/")
    public String homePage(Authentication authentication, Model model) {
        model.addAttribute("products", productService.getNewProducts());
        model.addAttribute("bestsellers", productService.getBestsellers());
        model.addAttribute("newProducts", productService.getNewProducts());

        // Get active vouchers for banner
        List<Voucher> activeVouchers = voucherService.getActiveVouchers();
        model.addAttribute("activeVouchers", activeVouchers);

        // Get saved voucher IDs for current user
        if (authentication != null) {
            User user = userService.findByUsername(authentication.getName()).orElse(null);
            if (user != null) {
                Set<Long> savedVoucherIds = savedVoucherRepository.findByUser(user)
                        .stream()
                        .map(sv -> sv.getVoucher().getId())
                        .collect(Collectors.toSet());
                model.addAttribute("savedVoucherIds", savedVoucherIds);
            }
        }

        return "index";
    }

    @GetMapping("/checkout")
    public String checkoutPage(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        List<CartItem> cartItems = cartService.getCartItems(user);
        if (cartItems == null || cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        BigDecimal totalAmount = cartService.getCartTotal(user);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("user", user);
        return "checkout";
    }

    @GetMapping("/order-success")
    public String orderSuccessPage() {
        return "order-success";
    }

    @GetMapping("/membership")
    public String membershipPage(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        model.addAttribute("user", user);
        return "membership";
    }

    @GetMapping("/chat")
    public String chatPage() {
        return "chat";
    }
}
