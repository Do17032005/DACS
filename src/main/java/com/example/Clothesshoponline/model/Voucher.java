package com.example.Clothesshoponline.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vouchers")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Mã voucher không được để trống")
    @Size(min = 3, max = 50, message = "Mã voucher phải từ 3-50 ký tự")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Mã voucher chỉ chứa chữ in hoa, số, gạch ngang và gạch dưới")
    @Column(nullable = false, unique = true)
    private String code;

    @NotBlank(message = "Tiêu đề voucher không được để trống")
    @Size(min = 3, max = 200, message = "Tiêu đề phải từ 3-200 ký tự")
    @Column(nullable = false)
    private String title;

    @Size(max = 1000, message = "Mô tả không được quá 1000 ký tự")
    private String description;

    @Min(value = 0, message = "Phần trăm giảm giá phải từ 0-100")
    @Max(value = 100, message = "Phần trăm giảm giá phải từ 0-100")
    @Column(name = "discount_percent")
    private Integer discountPercent;

    @DecimalMin(value = "0.0", inclusive = false, message = "Số tiền giảm phải lớn hơn 0")
    @DecimalMax(value = "99999999.99", message = "Số tiền giảm không được quá 99,999,999")
    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @DecimalMin(value = "0.0", message = "Giá trị đơn hàng tối thiểu phải >= 0")
    @Column(name = "min_order_amount")
    private BigDecimal minOrderAmount;

    @DecimalMin(value = "0.0", inclusive = false, message = "Giảm giá tối đa phải lớn hơn 0")
    @Column(name = "max_discount")
    private BigDecimal maxDiscount;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Min(value = 1, message = "Giới hạn sử dụng phải >= 1")
    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Min(value = 0, message = "Số lần đã dùng phải >= 0")
    @Column(name = "used_count")
    private Integer usedCount = 0;

    private boolean active = true;

    @Column(name = "applicable_product_ids", length = 2000)
    private String applicableProductIds; // Comma-separated product IDs (empty = all products)

    // Constructors
    public Voucher() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(BigDecimal minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public BigDecimal getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(BigDecimal maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getApplicableProductIds() {
        return applicableProductIds;
    }

    public void setApplicableProductIds(String applicableProductIds) {
        this.applicableProductIds = applicableProductIds;
    }
}
