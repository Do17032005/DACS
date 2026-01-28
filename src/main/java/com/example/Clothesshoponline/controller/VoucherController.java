package com.example.Clothesshoponline.controller;

import com.example.Clothesshoponline.model.User;
import com.example.Clothesshoponline.model.Voucher;
import com.example.Clothesshoponline.model.SavedVoucher;
import com.example.Clothesshoponline.repository.SavedVoucherRepository;
import com.example.Clothesshoponline.service.UserService;
import com.example.Clothesshoponline.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/vouchers")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private UserService userService;

    @Autowired
    private SavedVoucherRepository savedVoucherRepository;

    @GetMapping
    public String vouchersPage(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        List<Voucher> vouchers = voucherService.getActiveVouchers();
        model.addAttribute("vouchers", vouchers);
        model.addAttribute("user", user);
        return "vouchers";
    }

    @PostMapping("/apply")
    @ResponseBody
    public Map<String, Object> applyVoucher(@RequestParam String code,
            @RequestParam BigDecimal orderAmount) {
        Map<String, Object> response = new HashMap<>();
        try {
            BigDecimal discount = voucherService.calculateDiscount(code, orderAmount);
            response.put("success", true);
            response.put("discount", discount);
            response.put("message", "Áp dụng voucher thành công");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    @PostMapping("/validate")
    @ResponseBody
    public Map<String, Object> validateVoucher(@RequestParam String code) {
        Map<String, Object> response = new HashMap<>();
        try {
            Voucher voucher = voucherService.getVoucherByCode(code);
            response.put("valid", true);
            response.put("voucher", voucher);
        } catch (Exception e) {
            response.put("valid", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    /**
     * Save voucher for user
     */
    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveVoucher(
            @RequestParam Long voucherId,
            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (authentication == null) {
                response.put("success", false);
                response.put("message", "Vui lòng đăng nhập để lưu mã giảm giá");
                return ResponseEntity.ok(response);
            }

            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

            Voucher voucher = voucherService.getVoucherById(voucherId);

            // Check if already saved
            if (savedVoucherRepository.existsByUserAndVoucher(user, voucher)) {
                response.put("success", false);
                response.put("message", "Bạn đã lưu mã này rồi");
                return ResponseEntity.ok(response);
            }

            // Check if voucher has usage limit
            if (voucher.getUsageLimit() != null && voucher.getUsedCount() >= voucher.getUsageLimit()) {
                response.put("success", false);
                response.put("message", "Mã giảm giá đã hết lượt sử dụng");
                return ResponseEntity.ok(response);
            }

            // Save voucher
            SavedVoucher savedVoucher = new SavedVoucher(user, voucher);
            savedVoucherRepository.save(savedVoucher);

            response.put("success", true);
            response.put("message", "Đã lưu mã giảm giá thành công!");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
