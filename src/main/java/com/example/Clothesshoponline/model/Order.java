package com.example.Clothesshoponline.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;
    private Long userId;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
    private String status;
}
