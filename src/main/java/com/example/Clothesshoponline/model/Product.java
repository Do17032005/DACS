package com.example.Clothesshoponline.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(min = 3, max = 255, message = "Tên sản phẩm phải từ 3-255 ký tự")
    @Column(nullable = false)
    private String name;

    @Size(max = 5000, message = "Mô tả không được quá 5000 ký tự")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    @DecimalMax(value = "999999999.99", message = "Giá không được quá 999,999,999")
    @Column(nullable = false)
    private BigDecimal price;

    @DecimalMin(value = "0.0", inclusive = false, message = "Giá gốc phải lớn hơn 0")
    @DecimalMax(value = "999999999.99", message = "Giá gốc không được quá 999,999,999")
    private BigDecimal originalPrice;

    @Min(value = 0, message = "Phần trăm giảm giá phải từ 0-100")
    @Max(value = 100, message = "Phần trăm giảm giá phải từ 0-100")
    @Column(name = "discount_percent")
    private Integer discountPercent = 0;

    @NotBlank(message = "Danh mục không được để trống")
    @Column(nullable = false)
    private String category; // NAM, NỮ, TRẺ EM, PHỤ KIỆN - kept for backward compatibility

    @Size(max = 100, message = "Phân loại sản phẩm không được quá 100 ký tự")
    @Column(name = "subcategory")
    private String subcategory; // Áo thun, Quần jean, Váy, Đầm, etc.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category categoryObj;

    @Size(max = 100, message = "Thương hiệu không được quá 100 ký tự")
    private String brand;

    @Size(max = 100, message = "Chất liệu không được quá 100 ký tự")
    private String material;

    @Size(max = 50, message = "Màu sắc không được quá 50 ký tự")
    private String color;

    @Size(max = 500, message = "URL ảnh không được quá 500 ký tự")
    @Column(name = "image_url")
    private String imageUrl;

    @Size(max = 1000, message = "Danh sách ảnh phụ không được quá 1000 ký tự")
    @Column(name = "additional_images", length = 1000)
    private String additionalImages; // Comma-separated URLs

    @Size(max = 200, message = "Danh sách kích cỡ không được quá 200 ký tự")
    @Column(name = "available_sizes")
    private String availableSizes; // Comma-separated sizes (S, M, L, XL)

    @NotNull(message = "Số lượng tồn kho không được để trống")
    @Min(value = 0, message = "Số lượng tồn kho phải >= 0")
    @Column(nullable = false)
    private Integer stock = 0;

    @Min(value = 0, message = "Số lượng đã bán phải >= 0")
    private Integer sold = 0;

    @Column(name = "is_new")
    private boolean isNew = false;

    @Column(name = "is_bestseller")
    private boolean isBestseller = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructors
    public Product() {
    }

    public Product(String name, BigDecimal price, String category, String imageUrl) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public Category getCategoryObj() {
        return categoryObj;
    }

    public void setCategoryObj(Category categoryObj) {
        this.categoryObj = categoryObj;
        // Sync category string for backward compatibility
        if (categoryObj != null) {
            this.category = categoryObj.getName();
        }
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAdditionalImages() {
        return additionalImages;
    }

    public void setAdditionalImages(String additionalImages) {
        this.additionalImages = additionalImages;
    }

    public String getAvailableSizes() {
        return availableSizes;
    }

    public void setAvailableSizes(String availableSizes) {
        this.availableSizes = availableSizes;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public boolean isBestseller() {
        return isBestseller;
    }

    public void setBestseller(boolean isBestseller) {
        this.isBestseller = isBestseller;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
