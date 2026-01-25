package com.example.Clothesshoponline.repository;

import com.example.Clothesshoponline.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> rowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        return user;
    };

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        List<User> users = jdbcTemplate.query(sql, rowMapper, username);
        return users.stream().findFirst();
    }

    public void save(User user) {
        if (user.getId() == null) {
            String sql = "INSERT INTO users (username, password, role, full_name, email) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getRole(), user.getFullName(),
                    user.getEmail());
        } else {
            String sql = "UPDATE users SET username = ?, password = ?, role = ?, full_name = ?, email = ? WHERE id = ?";
            jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getRole(), user.getFullName(),
                    user.getEmail(), user.getId());
        }
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
