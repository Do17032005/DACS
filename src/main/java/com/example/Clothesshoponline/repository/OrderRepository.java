package com.example.Clothesshoponline.repository;

import com.example.Clothesshoponline.model.Order;
import com.example.Clothesshoponline.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);

    Optional<Order> findByOrderCode(String orderCode);

    List<Order> findByStatusOrderByCreatedAtDesc(Order.OrderStatus status);

    List<Order> findAllByOrderByCreatedAtDesc();

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.user LEFT JOIN FETCH o.orderItems ORDER BY o.createdAt DESC")
    List<Order> findAllWithUser();
}
