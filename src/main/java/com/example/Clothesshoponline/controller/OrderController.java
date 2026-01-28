package com.example.Clothesshoponline.controller;

import com.example.Clothesshoponline.model.Order;
import com.example.Clothesshoponline.model.OrderItem;
import com.example.Clothesshoponline.model.User;
import com.example.Clothesshoponline.model.CartItem;
import com.example.Clothesshoponline.model.Voucher;
import com.example.Clothesshoponline.service.CartService;
import com.example.Clothesshoponline.service.OrderService;
import com.example.Clothesshoponline.service.UserService;
import com.example.Clothesshoponline.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private VoucherService voucherService;

    @GetMapping
    public String ordersPage(Authentication authentication, Model model) {
        if (authentication != null) {
            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            List<Order> orders = orderService.getUserOrders(user);
            model.addAttribute("orders", orders);
            model.addAttribute("user", user);
        } else {
            return "redirect:/login";
        }
        return "orders";
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "order-detail";
    }

    @PostMapping("/place")
    public String placeOrder(@RequestParam String customerName,
            @RequestParam String customerPhone,
            @RequestParam(required = false) String customerEmail,
            @RequestParam String shippingAddress,
            @RequestParam(required = false) String shippingCity,
            @RequestParam(required = false) String shippingDistrict,
            @RequestParam(required = false) String note,
            @RequestParam String paymentMethod,
            @RequestParam(required = false) String voucherCode,
            Authentication authentication,
            Model model) {
        try {
            if (authentication == null) {
                return "redirect:/login";
            }

            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Vui lòng đăng nhập"));

            // Get cart from database
            List<CartItem> cartItems = cartService.getCartItems(user);

            if (cartItems == null || cartItems.isEmpty()) {
                model.addAttribute("error", "Giỏ hàng trống");
                return "redirect:/cart";
            }

            // Create order
            Order order = new Order();
            order.setCustomerName(customerName);
            order.setCustomerPhone(customerPhone);
            order.setCustomerEmail(customerEmail);
            order.setShippingAddress(shippingAddress);
            order.setShippingCity(shippingCity);
            order.setShippingDistrict(shippingDistrict);
            order.setNote(note);
            order.setPaymentMethod(Order.PaymentMethod.valueOf(paymentMethod.toUpperCase()));
            order.setStatus(Order.OrderStatus.PENDING);

            // Calculate total and create order items
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (CartItem cartItem : cartItems) {
                OrderItem orderItem = new OrderItem(
                        cartItem.getProduct(),
                        cartItem.getQuantity(),
                        cartItem.getProduct().getPrice(),
                        cartItem.getSize(),
                        cartItem.getColor());
                order.getOrderItems().add(orderItem);
                totalAmount = totalAmount.add(cartItem.getSubtotal());
            }

            // Apply voucher if provided
            BigDecimal discountAmount = BigDecimal.ZERO;
            if (voucherCode != null && !voucherCode.trim().isEmpty()) {
                try {
                    discountAmount = voucherService.calculateDiscount(voucherCode, totalAmount);
                    totalAmount = totalAmount.subtract(discountAmount);

                    // Increment voucher usage count
                    voucherService.incrementVoucherUsage(voucherCode);
                } catch (Exception e) {
                    // If voucher invalid, continue without discount
                    System.out.println("Voucher error: " + e.getMessage());
                }
            }

            order.setTotalAmount(totalAmount);

            // Save order
            Order createdOrder = orderService.createOrder(order, user);

            // Clear cart after successful order
            cartService.clearCart(user);

            return "redirect:/order-success?code=" + createdOrder.getOrderCode();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/checkout?error=" + e.getMessage();
        }
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute Order order,
            Authentication authentication,
            Model model) {
        try {
            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Vui lòng đăng nhập"));
            Order createdOrder = orderService.createOrder(order, user);
            return "redirect:/order-success?code=" + createdOrder.getOrderCode();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "checkout";
        }
    }

    /**
     * API to validate and apply voucher
     */
    @PostMapping("/apply-voucher")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> applyVoucher(
            @RequestParam String voucherCode,
            @RequestParam BigDecimal orderAmount,
            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (authentication == null) {
                response.put("success", false);
                response.put("message", "Vui lòng đăng nhập");
                return ResponseEntity.ok(response);
            }

            // Get voucher by code
            Voucher voucher = voucherService.getVoucherByCode(voucherCode);

            // Validate voucher
            LocalDateTime now = LocalDateTime.now();
            if (!voucher.isActive()) {
                response.put("success", false);
                response.put("message", "Mã giảm giá không hợp lệ");
                return ResponseEntity.ok(response);
            }

            if (now.isBefore(voucher.getStartDate())) {
                response.put("success", false);
                response.put("message", "Mã giảm giá chưa đến thời gian sử dụng");
                return ResponseEntity.ok(response);
            }

            if (now.isAfter(voucher.getEndDate())) {
                response.put("success", false);
                response.put("message", "Mã giảm giá đã hết hạn");
                return ResponseEntity.ok(response);
            }

            if (voucher.getUsageLimit() != null && voucher.getUsedCount() >= voucher.getUsageLimit()) {
                response.put("success", false);
                response.put("message", "Mã giảm giá đã hết lượt sử dụng");
                return ResponseEntity.ok(response);
            }

            if (voucher.getMinOrderAmount() != null && orderAmount.compareTo(voucher.getMinOrderAmount()) < 0) {
                response.put("success", false);
                response.put("message", "Đơn hàng chưa đủ điều kiện. Tối thiểu: " +
                        String.format("%,dđ", voucher.getMinOrderAmount().longValue()));
                return ResponseEntity.ok(response);
            }

            // Calculate discount
            BigDecimal discount = voucherService.calculateDiscount(voucherCode, orderAmount);
            BigDecimal finalAmount = orderAmount.subtract(discount);

            // Calculate remaining usage
            Integer remainingUsage = null;
            if (voucher.getUsageLimit() != null) {
                remainingUsage = voucher.getUsageLimit() - voucher.getUsedCount();
            }

            response.put("success", true);
            response.put("message", "Áp dụng mã giảm giá thành công!");
            response.put("discount", discount);
            response.put("finalAmount", finalAmount);
            response.put("voucherCode", voucherCode);
            response.put("voucherTitle", voucher.getTitle());
            response.put("remainingUsage", remainingUsage);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
