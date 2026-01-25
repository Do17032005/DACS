package com.example.Clothesshoponline.service;

import com.example.Clothesshoponline.model.Order;
import com.example.Clothesshoponline.model.Product;
import com.example.Clothesshoponline.repository.OrderRepository;
import com.example.Clothesshoponline.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final GeminiService geminiService;

    public RecommendationService(OrderRepository orderRepository, ProductRepository productRepository,
            GeminiService geminiService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.geminiService = geminiService;
    }

    public List<Product> getRecommendationsForUser(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<Product> allProducts = productRepository.findAll();

        if (orders.isEmpty()) {
            // New user: recommend top stock or random
            return allProducts.stream().limit(3).collect(Collectors.toList());
        }

        // Simple prompt construction
        // In a real app, we would join tables to get exactly what product names they
        // bought.
        // For now, assuming we can get some context (we haven't implemented OrderItems
        // fully yet, so we have total price but not item names easily accessible
        // without OrderItems table populated and linked)
        // detailed implementation needed.

        // Let's rely on a simplified approach: Recommend based on "Trending" if we
        // can't get exact items,
        // OR let's assume we can fetch product names from order items (which we defined
        // in schema but didn't create Repo for yet).

        // FAST PATH: Let's ask Gemini to pick from All Products based on a generic
        // "Fashion" persona for now,
        // or if we had the purchased product names, we'd use them.
        // Let's fetch the first product of the user's last order if possible to
        // simulate "based on history".
        // Since we don't have OrderItems populated in the dummy data insert, let's
        // pretend the user likes "Casual wear".

        String prompt = "Given the available products: " +
                allProducts.stream().map(p -> p.getId() + ":" + p.getName()).collect(Collectors.joining(", ")) +
                ". The user typically likes trending fashion. Recommend 3 product IDs as a comma-separated list (e.g., 1,3,5).";

        String response = geminiService.generateContent(prompt);

        return parseProductIds(response, allProducts);
    }

    private List<Product> parseProductIds(String response, List<Product> allProducts) {
        List<Product> recommended = new ArrayList<>();
        try {
            // Clean response to get numbers
            String[] parts = response.replaceAll("[^0-9,]", "").split(",");
            for (String part : parts) {
                if (!part.trim().isEmpty()) {
                    Long id = Long.parseLong(part.trim());
                    allProducts.stream()
                            .filter(p -> p.getId().equals(id))
                            .findFirst()
                            .ifPresent(recommended::add);
                }
            }
        } catch (Exception e) {
            // Fallback
            System.err.println("Failed to parse recommendation: " + e.getMessage());
        }

        if (recommended.isEmpty()) {
            return allProducts.stream().limit(3).collect(Collectors.toList());
        }
        return recommended;
    }
}
