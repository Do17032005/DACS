package com.example.Clothesshoponline.service;

import com.example.Clothesshoponline.model.Order;
import com.example.Clothesshoponline.model.Product;
import com.example.Clothesshoponline.repository.OrderRepository;
import com.example.Clothesshoponline.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void placeOrder(Long userId, Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        // Deduct stock
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        // Create Order
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(product.getPrice().multiply(java.math.BigDecimal.valueOf(quantity)));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("COMPLETED");

        // Save Order (In a real app, we'd save OrderItems too, but keeping it simple
        // for now)
        orderRepository.save(order);
    }

    public java.util.List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public java.util.List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void updateOrderStatus(Long orderId, String status) {
        orderRepository.updateStatus(orderId, status);
    }
}
