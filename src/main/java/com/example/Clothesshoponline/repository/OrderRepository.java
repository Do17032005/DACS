package com.example.Clothesshoponline.repository;

import com.example.Clothesshoponline.model.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Order> rowMapper = (rs, rowNum) -> {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setUserId(rs.getLong("user_id"));
        order.setTotalPrice(rs.getBigDecimal("total_price"));
        order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
        order.setStatus(rs.getString("status"));
        return order;
    };

    public void save(Order order) {
        String sql = "INSERT INTO orders (user_id, total_price, order_date, status) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, order.getUserId(), order.getTotalPrice(), Timestamp.valueOf(order.getOrderDate()),
                order.getStatus());
    }

    public List<Order> findByUserId(Long userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
        return jdbcTemplate.query(sql, rowMapper, userId);
    }

    public List<Order> findAll() {
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void updateStatus(Long id, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, id);
    }
}
