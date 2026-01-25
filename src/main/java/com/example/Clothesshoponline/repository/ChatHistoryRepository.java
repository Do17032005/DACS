package com.example.Clothesshoponline.repository;

import com.example.Clothesshoponline.model.ChatHistory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatHistoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public ChatHistoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ChatHistory> rowMapper = (rs, rowNum) -> {
        ChatHistory chat = new ChatHistory();
        chat.setId(rs.getLong("id"));
        chat.setUserId(rs.getLong("user_id"));
        chat.setMessage(rs.getString("message"));
        chat.setResponse(rs.getString("response"));
        chat.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        return chat;
    };

    public List<ChatHistory> findByUserId(Long userId) {
        String sql = "SELECT * FROM chat_history WHERE user_id = ? ORDER BY timestamp ASC";
        return jdbcTemplate.query(sql, rowMapper, userId);
    }

    public void save(ChatHistory chatHistory) {
        String sql = "INSERT INTO chat_history (user_id, message, response, timestamp) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, chatHistory.getUserId(), chatHistory.getMessage(), chatHistory.getResponse(),
                chatHistory.getTimestamp());
    }
}
