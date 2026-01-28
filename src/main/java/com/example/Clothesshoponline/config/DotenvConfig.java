package com.example.Clothesshoponline.config;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Load environment variables từ file .env TRƯỚC KHI Spring Boot khởi động
 * Sử dụng static block để load ngay khi class được load vào memory
 */
public class DotenvConfig {

    static {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("./") // Đọc file .env từ thư mục gốc project
                    .ignoreIfMissing() // Không báo lỗi nếu không tìm thấy file
                    .load();

            // Set system properties để Spring Boot có thể sử dụng ${DB_URL},
            // ${DB_USERNAME}, etc.
            dotenv.entries().forEach(entry -> {
                System.setProperty(entry.getKey(), entry.getValue());
            });

            System.out.println("✅ Loaded environment variables from .env file");
            System.out.println("   - DB_URL: " + (dotenv.get("DB_URL") != null ? "***configured***" : "NOT SET"));
            System.out.println(
                    "   - DB_USERNAME: " + (dotenv.get("DB_USERNAME") != null ? "***configured***" : "NOT SET"));
            System.out.println(
                    "   - DB_PASSWORD: " + (dotenv.get("DB_PASSWORD") != null ? "***configured***" : "NOT SET"));

        } catch (Exception e) {
            System.out.println("⚠️ Could not load .env file: " + e.getMessage());
            System.out.println("⚠️ Using system environment variables instead");
        }
    }
}
