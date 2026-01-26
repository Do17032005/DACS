package com.example.Clothesshoponline.repository;

import com.example.Clothesshoponline.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Product> rowMapper = (rs, rowNum) -> {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setDescription(rs.getString("description"));
        product.setImageUrl(rs.getString("image_url"));
        product.setCategory(rs.getString("category"));
        product.setStock(rs.getInt("stock"));
        return product;
    };

    public List<Product> findAll() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Product> search(String keyword) {
        String like = "%" + keyword.toLowerCase() + "%";
        String sql = "SELECT * FROM products WHERE LOWER(name) LIKE ? OR LOWER(description) LIKE ?";
        return jdbcTemplate.query(sql, rowMapper, like, like);
    }

    public Optional<Product> findById(Long id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        List<Product> products = jdbcTemplate.query(sql, rowMapper, id);
        return products.stream().findFirst();
    }

    public void save(Product product) {
        if (product.getId() == null) {
            String sql = "INSERT INTO products (name, price, description, image_url, category, stock) VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getDescription(), product.getImageUrl(), product.getCategory(), product.getStock());
        } else {
            String sql = "UPDATE products SET name = ?, price = ?, description = ?, image_url = ?, category = ?, stock = ? WHERE id = ?";
            jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getDescription(), product.getImageUrl(), product.getCategory(), product.getStock(), product.getId());
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
