package com.example.Clothesshoponline.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private String category;
    private Integer stock;
}
