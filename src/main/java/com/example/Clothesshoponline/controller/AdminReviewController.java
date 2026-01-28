package com.example.Clothesshoponline.controller;

import com.example.Clothesshoponline.model.Review;
import com.example.Clothesshoponline.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/reviews")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public String listReviews(Model model) {
        List<Review> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);

        // Statistics
        long totalReviews = reviews.size();
        long verifiedReviews = reviews.stream().filter(Review::isVerified).count();
        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        model.addAttribute("totalReviews", totalReviews);
        model.addAttribute("verifiedReviews", verifiedReviews);
        model.addAttribute("averageRating", Math.round(averageRating * 10.0) / 10.0);

        return "admin/reviews";
    }

    @PostMapping("/verify/{id}")
    public String verifyReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Review review = reviewService.getReviewById(id);
            review.setVerified(true);
            reviewService.saveReview(review);
            redirectAttributes.addFlashAttribute("success", "Đã xác minh đánh giá!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/reviews";
    }

    @PostMapping("/unverify/{id}")
    public String unverifyReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Review review = reviewService.getReviewById(id);
            review.setVerified(false);
            reviewService.saveReview(review);
            redirectAttributes.addFlashAttribute("success", "Đã bỏ xác minh đánh giá!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/reviews";
    }

    @GetMapping("/delete/{id}")
    public String deleteReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reviewService.deleteReview(id);
            redirectAttributes.addFlashAttribute("success", "Đã xóa đánh giá!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/reviews";
    }
}
