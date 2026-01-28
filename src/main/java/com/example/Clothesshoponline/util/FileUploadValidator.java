package com.example.Clothesshoponline.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class để validate file uploads
 */
public class FileUploadValidator {

    // Allowed image MIME types
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/gif",
            "image/webp");

    // Allowed file extensions
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".webp");

    // Max file size: 5MB
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB in bytes

    // Max total size for multiple files: 20MB
    private static final long MAX_TOTAL_SIZE = 20 * 1024 * 1024; // 20MB

    /**
     * Validate single file upload
     */
    public static ValidationResult validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new ValidationResult(true, ""); // Empty is valid (optional file)
        }

        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            return new ValidationResult(false,
                    "Kích thước file không được vượt quá 5MB. File hiện tại: " + formatFileSize(file.getSize()));
        }

        // Check content type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            return new ValidationResult(false,
                    "Chỉ chấp nhận file ảnh định dạng: JPG, JPEG, PNG, GIF, WEBP. File hiện tại: " + contentType);
        }

        // Check file extension
        String filename = file.getOriginalFilename();
        if (filename == null) {
            return new ValidationResult(false, "Tên file không hợp lệ");
        }

        String extension = getFileExtension(filename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            return new ValidationResult(false,
                    "Phần mở rộng file không hợp lệ: " + extension + ". Chỉ chấp nhận: "
                            + String.join(", ", ALLOWED_EXTENSIONS));
        }

        // Check for dangerous filenames
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            return new ValidationResult(false, "Tên file chứa ký tự không an toàn");
        }

        return new ValidationResult(true, "");
    }

    /**
     * Validate multiple file uploads
     */
    public static ValidationResult validateImageFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return new ValidationResult(true, "");
        }

        long totalSize = 0;
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                // Validate each file
                ValidationResult result = validateImageFile(file);
                if (!result.isValid()) {
                    return result;
                }
                totalSize += file.getSize();
            }
        }

        // Check total size
        if (totalSize > MAX_TOTAL_SIZE) {
            return new ValidationResult(false,
                    "Tổng kích thước các file không được vượt quá 20MB. Tổng hiện tại: " + formatFileSize(totalSize));
        }

        return new ValidationResult(true, "");
    }

    /**
     * Sanitize filename to prevent path traversal attacks
     */
    public static String sanitizeFilename(String filename) {
        if (filename == null) {
            return "unnamed" + System.currentTimeMillis();
        }

        // Remove path separators and dangerous characters
        String sanitized = filename.replaceAll("[^a-zA-Z0-9._-]", "_");

        // Limit filename length
        if (sanitized.length() > 100) {
            String extension = getFileExtension(sanitized);
            String nameWithoutExt = sanitized.substring(0, sanitized.lastIndexOf('.'));
            sanitized = nameWithoutExt.substring(0, 95 - extension.length()) + extension;
        }

        return sanitized;
    }

    /**
     * Get file extension
     */
    private static String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1) {
            return "";
        }
        return filename.substring(lastDot);
    }

    /**
     * Format file size for display
     */
    private static String formatFileSize(long bytes) {
        if (bytes < 1024)
            return bytes + " B";
        if (bytes < 1024 * 1024)
            return String.format("%.2f KB", bytes / 1024.0);
        return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
    }

    /**
     * Result class for validation
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;

        public ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        public boolean isValid() {
            return valid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
