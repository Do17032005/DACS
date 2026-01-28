package com.example.Clothesshoponline.repository;

import com.example.Clothesshoponline.model.Product;
import com.example.Clothesshoponline.model.Review;
import com.example.Clothesshoponline.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Lấy tất cả review của một sản phẩm, sắp xếp theo mới nhất
    List<Review> findByProductOrderByCreatedAtDesc(Product product);

    // Lấy review theo product id
    List<Review> findByProductIdOrderByCreatedAtDesc(Long productId);

    // Kiểm tra user đã review sản phẩm này chưa
    Optional<Review> findByProductAndUser(Product product, User user);

    // Kiểm tra user đã review sản phẩm này chưa (by id)
    boolean existsByProductIdAndUserId(Long productId, Long userId);

    // Đếm số review của sản phẩm
    long countByProductId(Long productId);

    // Tính điểm trung bình của sản phẩm
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double getAverageRatingByProductId(@Param("productId") Long productId);

    // Đếm số review theo từng rating (1-5 sao)
    @Query("SELECT r.rating, COUNT(r) FROM Review r WHERE r.product.id = :productId GROUP BY r.rating ORDER BY r.rating DESC")
    List<Object[]> countByRatingForProduct(@Param("productId") Long productId);

    // Lấy review của user cho một sản phẩm
    Optional<Review> findByProductIdAndUserId(Long productId, Long userId);

    // Lấy tất cả review của một user
    List<Review> findByUserOrderByCreatedAtDesc(User user);
}
