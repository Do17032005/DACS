package com.example.Clothesshoponline.service;

import com.example.Clothesshoponline.model.Voucher;
import com.example.Clothesshoponline.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    public List<Voucher> getActiveVouchers() {
        LocalDateTime now = LocalDateTime.now();
        return voucherRepository.findByActiveAndStartDateBeforeAndEndDateAfter(true, now, now);
    }

    public Voucher getVoucherByCode(String code) {
        return voucherRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mã giảm giá"));
    }

    public BigDecimal calculateDiscount(String voucherCode, BigDecimal orderAmount) {
        Voucher voucher = getVoucherByCode(voucherCode);

        LocalDateTime now = LocalDateTime.now();
        if (!voucher.isActive() ||
                now.isBefore(voucher.getStartDate()) ||
                now.isAfter(voucher.getEndDate())) {
            throw new RuntimeException("Mã giảm giá không hợp lệ hoặc đã hết hạn");
        }

        if (voucher.getMinOrderAmount() != null &&
                orderAmount.compareTo(voucher.getMinOrderAmount()) < 0) {
            throw new RuntimeException("Đơn hàng chưa đủ điều kiện áp dụng mã giảm giá");
        }

        if (voucher.getUsageLimit() != null &&
                voucher.getUsedCount() >= voucher.getUsageLimit()) {
            throw new RuntimeException("Mã giảm giá đã hết lượt sử dụng");
        }

        BigDecimal discount = BigDecimal.ZERO;

        if (voucher.getDiscountPercent() != null) {
            discount = orderAmount.multiply(BigDecimal.valueOf(voucher.getDiscountPercent()))
                    .divide(BigDecimal.valueOf(100));

            if (voucher.getMaxDiscount() != null &&
                    discount.compareTo(voucher.getMaxDiscount()) > 0) {
                discount = voucher.getMaxDiscount();
            }
        } else if (voucher.getDiscountAmount() != null) {
            discount = voucher.getDiscountAmount();
        }

        return discount;
    }

    /**
     * Check if voucher is applicable to a specific product
     */
    public boolean isApplicableToProduct(Voucher voucher, Long productId) {
        // If no specific products defined, voucher applies to all products
        if (voucher.getApplicableProductIds() == null || voucher.getApplicableProductIds().isEmpty()) {
            return true;
        }

        // Check if product ID is in the applicable list
        String[] productIds = voucher.getApplicableProductIds().split(",");
        for (String id : productIds) {
            if (id.trim().equals(productId.toString())) {
                return true;
            }
        }

        return false;
    }

    public void incrementVoucherUsage(String code) {
        Voucher voucher = getVoucherByCode(code);
        voucher.setUsedCount(voucher.getUsedCount() + 1);
        voucherRepository.save(voucher);
    }

    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    public Voucher saveVoucher(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    public Voucher getVoucherById(Long id) {
        return voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy voucher"));
    }

    public void deleteVoucher(Long id) {
        voucherRepository.deleteById(id);
    }
}
