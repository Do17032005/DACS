package com.example.Clothesshoponline.controller;

import com.example.Clothesshoponline.model.SiteContent;
import com.example.Clothesshoponline.service.SiteContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/content")
public class SiteContentController {

    @Autowired
    private SiteContentService contentService;

    // Get all content (public)
    @GetMapping
    public ResponseEntity<Map<String, String>> getAllContent() {
        return ResponseEntity.ok(contentService.getAllContentAsMap());
    }

    // Get single content by key
    @GetMapping("/{key}")
    public ResponseEntity<Map<String, Object>> getContent(@PathVariable String key) {
        Map<String, Object> response = new HashMap<>();
        String value = contentService.getContent(key, "");
        response.put("key", key);
        response.put("value", value);
        return ResponseEntity.ok(response);
    }

    // Update content (admin only)
    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateContent(
            @RequestBody Map<String, String> request,
            Authentication auth) {

        Map<String, Object> response = new HashMap<>();

        try {
            String key = request.get("key");
            String value = request.get("value");
            String username = auth.getName();

            SiteContent updated = contentService.updateContent(key, value, username);

            response.put("success", true);
            response.put("message", "Đã cập nhật thành công!");
            response.put("content", updated.getContentValue());
            response.put("updatedAt", updated.getUpdatedAt().toString());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    // Get all content for admin management
    @GetMapping("/admin/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SiteContent>> getAllContentList() {
        return ResponseEntity.ok(contentService.getAllContent());
    }

    // Bulk update multiple contents
    @PostMapping("/bulk-update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> bulkUpdate(
            @RequestBody List<Map<String, String>> updates,
            Authentication auth) {

        Map<String, Object> response = new HashMap<>();
        String username = auth.getName();
        int successCount = 0;

        for (Map<String, String> update : updates) {
            try {
                String key = update.get("key");
                String value = update.get("value");
                contentService.updateContent(key, value, username);
                successCount++;
            } catch (Exception e) {
                // Continue with next
            }
        }

        response.put("success", true);
        response.put("updated", successCount);
        response.put("message", "Đã cập nhật " + successCount + " nội dung!");

        return ResponseEntity.ok(response);
    }

    // Upload image (admin only)
    @PostMapping("/upload-image")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("file") MultipartFile file) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "Vui lòng chọn file");
                return ResponseEntity.ok(response);
            }

            // Validate file type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                response.put("success", false);
                response.put("message", "Chỉ cho phép upload ảnh");
                return ResponseEntity.ok(response);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID().toString() + extension;

            // Save to uploads folder
            String uploadDir = "uploads/content";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(newFilename);
            file.transferTo(filePath.toFile());

            // Return URL
            String fileUrl = "/uploads/content/" + newFilename;
            response.put("success", true);
            response.put("url", fileUrl);
            response.put("message", "Upload thành công!");

        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "Lỗi upload: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}
