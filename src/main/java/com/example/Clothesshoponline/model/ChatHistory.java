package com.example.Clothesshoponline.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatHistory {
    private Long id;
    private Long userId;
    private String message;
    private String response;
    private LocalDateTime timestamp;
}
