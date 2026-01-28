package com.example.Clothesshoponline.controller;

import com.example.Clothesshoponline.model.Product;
import com.example.Clothesshoponline.model.Review;
import com.example.Clothesshoponline.model.User;
import com.example.Clothesshoponline.service.ProductService;
import com.example.Clothesshoponline.service.ReviewService;
import com.example.Clothesshoponline.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private static final int PAGE_SIZE = 12;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String productsPage(@RequestParam(required = false) String category,
            @RequestParam(required = false) String subcategory,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean isNew,
            @RequestParam(required = false) Boolean bestseller,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String material,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "newest") String sort,
            Model model) {
        log.info("=== PRODUCTS PAGE REQUEST ===");
        log.info(
                "Category: {}, Subcategory: {}, Search: {}, isNew: {}, bestseller: {}, page: {}, sort: {}, minPrice: {}, maxPrice: {}, size: {}, color: {}, brand: {}, material: {}, name: {}",
                category, subcategory, search, isNew, bestseller, page, sort, minPrice, maxPrice, size, color, brand,
                material,
                name);

        Page<Product> productPage;

        // Use advanced filter if any filter param is provided
        if (minPrice != null || maxPrice != null || size != null || color != null ||
                brand != null || material != null || name != null ||
                (category != null && !category.trim().isEmpty()) ||
                (subcategory != null && !subcategory.trim().isEmpty())) {
            log.info("Using advanced filters");
            productPage = productService.filterProducts(category, subcategory, minPrice, maxPrice, size, color, brand,
                    material,
                    name, page, PAGE_SIZE,
                    sort);
        } else if (search != null && !search.trim().isEmpty()) {
            log.info("Performing search for: '{}'", search);
            productPage = productService.searchProducts(search, page, PAGE_SIZE, sort);
        } else if (isNew != null && isNew) {
            log.info("Filtering by new products");
            productPage = productService.getNewProducts(page, PAGE_SIZE, sort);
        } else if (bestseller != null && bestseller) {
            log.info("Filtering by bestsellers");
            productPage = productService.getBestsellers(page, PAGE_SIZE, sort);
        } else {
            log.info("Loading all products");
            productPage = productService.getAllProducts(page, PAGE_SIZE, sort);
        }

        log.info("Total products: {}, Total pages: {}, Current page: {}",
                productPage.getTotalElements(), productPage.getTotalPages(), page);

        // Get all filter options for sidebar
        List<String> allCategories = productService.getAllCategories();
        log.info("=== DEBUG CATEGORIES IN PRODUCT CONTROLLER ===");
        log.info("Categories list size: {}", allCategories.size());
        for (int i = 0; i < allCategories.size(); i++) {
            String cat = allCategories.get(i);
            log.info("Category {}: [{}] (length: {})", i + 1, cat, cat.length());
            log.info("Category {} bytes: {}", i + 1,
                    java.util.Arrays.toString(cat.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        }
        log.info("=== END DEBUG CATEGORIES ===");

        List<String> allBrands = productService.getAllBrands();
        List<String> allMaterials = productService.getAllMaterials();
        List<String> allColors = productService.getAllColors();

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedSubcategory", subcategory);
        model.addAttribute("searchKeyword", search);
        model.addAttribute("isNewFilter", isNew);
        model.addAttribute("bestsellerFilter", bestseller);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("selectedSize", size);
        model.addAttribute("selectedColor", color);
        model.addAttribute("selectedBrand", brand);
        model.addAttribute("selectedMaterial", material);
        model.addAttribute("selectedName", name);
        model.addAttribute("allCategories", allCategories);
        model.addAttribute("allBrands", allBrands);
        model.addAttribute("allMaterials", allMaterials);
        model.addAttribute("allColors", allColors);
        model.addAttribute("currentSort", sort);

        return "products";
    }

    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("=== PRODUCT DETAIL REQUEST ===");
        log.info("Requested product ID: {}", id);
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                log.error("Product not found for ID: {}", id);
                return "redirect:/products";
            }
            log.info("Found product: {}", product.getName());
            model.addAttribute("product", product);

            // Load reviews
            List<Review> reviews = reviewService.getReviewsByProductId(id);
            Double averageRating = reviewService.getAverageRating(id);
            long reviewCount = reviewService.getReviewCount(id);
            Map<Integer, Long> ratingStats = reviewService.getRatingStats(id);

            model.addAttribute("reviews", reviews);
            model.addAttribute("averageRating", averageRating);
            model.addAttribute("reviewCount", reviewCount);
            model.addAttribute("ratingStats", ratingStats);

            // Check if current user has already reviewed
            boolean hasReviewed = false;
            if (userDetails != null) {
                User user = userService.getUserByUsername(userDetails.getUsername());
                if (user != null) {
                    hasReviewed = reviewService.hasUserReviewed(id, user.getId());
                    model.addAttribute("currentUser", user);
                }
            }
            model.addAttribute("hasReviewed", hasReviewed);

            return "product-detail";
        } catch (Exception e) {
            log.error("Error loading product: {}", e.getMessage());
            return "redirect:/products";
        }
    }

    @PostMapping("/{id}/review")
    public String addReview(@PathVariable Long id,
            @RequestParam Integer rating,
            @RequestParam String comment,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (userDetails == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để đánh giá");
            return "redirect:/login";
        }

        try {
            User user = userService.getUserByUsername(userDetails.getUsername());
            Product product = productService.getProductById(id);

            if (reviewService.hasUserReviewed(id, user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Bạn đã đánh giá sản phẩm này rồi");
                return "redirect:/products/" + id;
            }

            reviewService.addReview(product, user, rating, comment);
            redirectAttributes.addFlashAttribute("success", "Cảm ơn bạn đã đánh giá sản phẩm!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/products/" + id;
    }

    @PostMapping("/{productId}/review/{reviewId}/edit")
    public String editReview(@PathVariable Long productId,
            @PathVariable Long reviewId,
            @RequestParam Integer rating,
            @RequestParam String comment,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (userDetails == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập");
            return "redirect:/login";
        }

        try {
            User user = userService.getUserByUsername(userDetails.getUsername());
            Review review = reviewService.getReviewById(reviewId);

            // Check if user owns this review
            if (!review.getUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Bạn không có quyền sửa đánh giá này");
                return "redirect:/products/" + productId;
            }

            reviewService.updateReview(reviewId, rating, comment);
            redirectAttributes.addFlashAttribute("success", "Đã cập nhật đánh giá của bạn!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/products/" + productId;
    }

    @GetMapping("/{productId}/review/{reviewId}/delete")
    public String deleteReview(@PathVariable Long productId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (userDetails == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập");
            return "redirect:/login";
        }

        try {
            User user = userService.getUserByUsername(userDetails.getUsername());
            Review review = reviewService.getReviewById(reviewId);

            // Check if user owns this review
            if (!review.getUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Bạn không có quyền xóa đánh giá này");
                return "redirect:/products/" + productId;
            }

            reviewService.deleteReview(reviewId);
            redirectAttributes.addFlashAttribute("success", "Đã xóa đánh giá của bạn!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/products/" + productId;
    }

    @GetMapping("/bestsellers")
    @ResponseBody
    public List<Product> getBestsellers() {
        return productService.getBestsellers();
    }

    @GetMapping("/new")
    @ResponseBody
    public List<Product> getNewProducts() {
        return productService.getNewProducts();
    }
}
