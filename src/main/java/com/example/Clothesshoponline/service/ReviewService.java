package com.example.Clothesshoponline.service;

import com.example.Clothesshoponline.model.Product;
import com.example.Clothesshoponline.model.Review;
import com.example.Clothesshoponline.model.User;
import com.example.Clothesshoponline.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    /**
     * Lấy tất cả review của một sản phẩm
     */
    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    /**
     * Thêm review mới
     */
    public Review addReview(Product product, User user, Integer rating, String comment) {
        // Kiểm tra xem user đã review chưa
        Optional<Review> existingReview = reviewRepository.findByProductAndUser(product, user);
        if (existingReview.isPresent()) {
            throw new RuntimeException("Bạn đã đánh giá sản phẩm này rồi");
        }

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    /**
     * Cập nhật review
     */
    public Review updateReview(Long reviewId, Integer rating, String comment) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá"));

        review.setRating(rating);
        review.setComment(comment);
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    /**
     * Xóa review
     */
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    /**
     * Kiểm tra user đã review sản phẩm chưa
     */
    public boolean hasUserReviewed(Long productId, Long userId) {
        return reviewRepository.existsByProductIdAndUserId(productId, userId);
    }

    /**
     * Lấy review của user cho sản phẩm
     */
    public Optional<Review> getUserReview(Long productId, Long userId) {
        return reviewRepository.findByProductIdAndUserId(productId, userId);
    }

    /**
     * Lấy điểm trung bình của sản phẩm
     */
    public Double getAverageRating(Long productId) {
        Double avg = reviewRepository.getAverageRatingByProductId(productId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }

    /**
     * Đếm tổng số review của sản phẩm
     */
    public long getReviewCount(Long productId) {
        return reviewRepository.countByProductId(productId);
    }

    /**
     * Lấy thống kê rating (số lượng mỗi sao)
     */
    public Map<Integer, Long> getRatingStats(Long productId) {
        Map<Integer, Long> stats = new HashMap<>();
        // Khởi tạo mặc định
        for (int i = 1; i <= 5; i++) {
            stats.put(i, 0L);
        }

        List<Object[]> results = reviewRepository.countByRatingForProduct(productId);
        for (Object[] row : results) {
            Integer rating = (Integer) row[0];
            Long count = (Long) row[1];
            stats.put(rating, count);
        }

        return stats;
    }

    /**
     * Lấy review theo ID
     */
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá"));
    }

    /**
     * Lưu review (dùng cho data initializer)
     */
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    /**
     * Lấy tất cả reviews (cho admin)
     */
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }
}
