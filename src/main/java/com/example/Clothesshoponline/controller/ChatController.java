package com.example.Clothesshoponline.controller;

import com.example.Clothesshoponline.model.ChatHistory;
import com.example.Clothesshoponline.model.User;
import com.example.Clothesshoponline.repository.ChatHistoryRepository;
import com.example.Clothesshoponline.repository.UserRepository;
import com.example.Clothesshoponline.service.GeminiService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final GeminiService geminiService;
    private final ChatHistoryRepository chatHistoryRepository;
    private final UserRepository userRepository;

    public ChatController(GeminiService geminiService, ChatHistoryRepository chatHistoryRepository,
            UserRepository userRepository) {
        this.geminiService = geminiService;
        this.chatHistoryRepository = chatHistoryRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public String chat(@RequestBody Map<String, String> payload) {
        String message = payload.get("message");

        // Find User ID from Security Context (this snippet assumes auth; for anonymous
        // chat, we might need a session ID)
        // For simplicity, let's allow anonymous for now but try to link if logged in.
        Long userId = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            userId = userRepository.findByUsername(auth.getName()).map(User::getId).orElse(null);
        }

        String response = geminiService.generateContent(message);

        // Save history
        ChatHistory history = new ChatHistory();
        history.setUserId(userId);
        history.setMessage(message);
        history.setResponse(response);
        history.setTimestamp(LocalDateTime.now());
        chatHistoryRepository.save(history);

        return response;
    }

    @GetMapping("/history/{userId}")
    public List<ChatHistory> getHistory(@PathVariable Long userId) {
        return chatHistoryRepository.findByUserId(userId);
    }
}
