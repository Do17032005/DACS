package com.example.Clothesshoponline.controller;

import com.example.Clothesshoponline.model.CartItem;
import com.example.Clothesshoponline.model.User;
import com.example.Clothesshoponline.service.CartService;
import com.example.Clothesshoponline.service.ProductService;
import com.example.Clothesshoponline.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String cartPage(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        List<CartItem> cartItems = cartService.getCartItems(user);
        BigDecimal totalAmount = cartService.getCartTotal(user);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("user", user);
        return "cart";
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addToCart(@RequestParam Long productId,
            @RequestParam(defaultValue = "1") Integer quantity,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String color,
            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (authentication == null) {
                response.put("success", false);
                response.put("message", "Vui lòng đăng nhập để thêm vào giỏ hàng");
                response.put("redirect", "/login");
                return response;
            }

            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

            cartService.addToCart(user, productId, quantity,
                    size != null ? size : "M",
                    color != null ? color : "Mặc định");

            int cartSize = cartService.getCartItemCount(user);

            response.put("success", true);
            response.put("cartSize", cartSize);
            response.put("message", "Đã thêm vào giỏ hàng");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> updateCart(@RequestParam Long productId,
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) Integer delta,
            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (authentication == null) {
                response.put("success", false);
                response.put("message", "Vui lòng đăng nhập");
                return response;
            }

            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

            CartItem item;
            if (delta != null) {
                item = cartService.updateQuantityByDelta(user, productId, delta);
            } else if (quantity != null) {
                item = cartService.updateQuantity(user, productId, quantity);
            } else {
                response.put("success", false);
                response.put("message", "Thiếu thông tin số lượng");
                return response;
            }

            BigDecimal totalAmount = cartService.getCartTotal(user);
            response.put("success", true);
            response.put("itemTotal", item != null ? item.getSubtotal() : BigDecimal.ZERO);
            response.put("totalAmount", totalAmount);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        cartService.removeFromCart(user, productId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(Authentication authentication) {
        if (authentication != null) {
            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            cartService.clearCart(user);
        }
        return "redirect:/cart";
    }

    @PostMapping("/buy-now")
    public String buyNow(@RequestParam Long productId,
            @RequestParam(defaultValue = "1") Integer quantity,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String color,
            Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        try {
            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

            cartService.addToCart(user, productId, quantity,
                    size != null ? size : "M",
                    color != null ? color : "Mặc định");

            return "redirect:/checkout";
        } catch (Exception e) {
            return "redirect:/products?error=" + e.getMessage();
        }
    }

    @GetMapping("/count")
    @ResponseBody
    public Map<String, Integer> getCartCount(Authentication authentication) {
        Map<String, Integer> response = new HashMap<>();
        if (authentication != null) {
            User user = userService.findByUsername(authentication.getName()).orElse(null);
            if (user != null) {
                response.put("count", cartService.getCartItemCount(user));
                return response;
            }
        }
        response.put("count", 0);
        return response;
    }
}
