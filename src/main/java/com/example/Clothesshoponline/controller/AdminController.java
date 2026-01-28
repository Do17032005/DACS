package com.example.Clothesshoponline.controller;

import com.example.Clothesshoponline.model.Order;
import com.example.Clothesshoponline.model.Product;
import com.example.Clothesshoponline.model.User;
import com.example.Clothesshoponline.model.Voucher;

import com.example.Clothesshoponline.service.OrderService;
import com.example.Clothesshoponline.service.ProductService;
import com.example.Clothesshoponline.service.UserService;
import com.example.Clothesshoponline.service.VoucherService;
import com.example.Clothesshoponline.util.FileUploadValidator;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private com.example.Clothesshoponline.service.CategoryService categoryService;

    private final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String material,
            @RequestParam(required = false) String search,
            Model model) {

        List<Product> allProducts = productService.getAllProducts();
        List<Product> filteredProducts = allProducts;

        // Apply filters
        if (category != null && !category.isEmpty()) {
            filteredProducts = filteredProducts.stream()
                    .filter(p -> category.equals(p.getCategory()))
                    .collect(Collectors.toList());
        }
        if (brand != null && !brand.isEmpty()) {
            filteredProducts = filteredProducts.stream()
                    .filter(p -> brand.equals(p.getBrand()))
                    .collect(Collectors.toList());
        }
        if (material != null && !material.isEmpty()) {
            filteredProducts = filteredProducts.stream()
                    .filter(p -> material.equals(p.getMaterial()))
                    .collect(Collectors.toList());
        }
        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.toLowerCase().trim();
            filteredProducts = filteredProducts.stream()
                    .filter(p -> p.getName().toLowerCase().contains(searchLower))
                    .collect(Collectors.toList());
        }

        List<Order> orders = orderService.getAllOrders();
        List<User> users = userService.getAllUsers();

        // Statistics
        BigDecimal totalRevenue = orders.stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.DELIVERED)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long pendingOrders = orders.stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.PENDING)
                .count();

        long lowStockProducts = allProducts.stream()
                .filter(p -> p.getStock() != null && p.getStock() <= 10)
                .count();

        // Sort by stock ascending (lowest first) to show most critical items
        List<Product> lowStockList = allProducts.stream()
                .filter(p -> p.getStock() != null && p.getStock() <= 10)
                .sorted((a, b) -> a.getStock().compareTo(b.getStock()))
                .limit(5)
                .collect(Collectors.toList());

        List<Order> recentOrders = orders.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .collect(Collectors.toList());

        // Get unique values for filter dropdowns
        List<String> categories = allProducts.stream()
                .map(Product::getCategory)
                .distinct()
                .filter(c -> c != null && !c.isEmpty())
                .sorted()
                .collect(Collectors.toList());

        List<String> brands = allProducts.stream()
                .map(Product::getBrand)
                .distinct()
                .filter(b -> b != null && !b.isEmpty())
                .sorted()
                .collect(Collectors.toList());

        List<String> materials = allProducts.stream()
                .map(Product::getMaterial)
                .distinct()
                .filter(m -> m != null && !m.isEmpty())
                .sorted()
                .collect(Collectors.toList());

        model.addAttribute("products", filteredProducts);
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("materials", materials);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedBrand", brand);
        model.addAttribute("selectedMaterial", material);
        model.addAttribute("searchKeyword", search);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalOrders", orders.size());
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("totalProducts", allProducts.size());
        model.addAttribute("lowStockProducts", lowStockProducts);
        model.addAttribute("lowStockList", lowStockList);
        model.addAttribute("totalUsers", users.size());
        model.addAttribute("recentOrders", recentOrders);

        return "admin/dashboard";
    }

    @GetMapping("/products")
    public String manageProducts() {
        // Redirect to dashboard instead of trying to render it without all required
        // attributes
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/product/add")
    public String addProductForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/product_form";
    }

    @GetMapping("/product/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/product_form";
    }

    @PostMapping("/product/save")
    public String saveProduct(@Valid @ModelAttribute Product product,
            BindingResult result,
            @RequestParam(required = false) MultipartFile imageFile,
            @RequestParam(required = false) String imageUrl,
            @RequestParam(required = false) List<MultipartFile> additionalImageFiles,
            Model model,
            RedirectAttributes redirectAttributes) {

        log.info("=== SAVE PRODUCT REQUEST ===");
        log.info("Product: name={}, category={}, price={}", product.getName(), product.getCategory(),
                product.getPrice());

        if (result.hasErrors()) {
            log.error("Validation errors: {}", result.getAllErrors());
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/product_form";
        }

        // Validate file uploads
        FileUploadValidator.ValidationResult imageValidation = FileUploadValidator.validateImageFile(imageFile);
        if (!imageValidation.isValid()) {
            model.addAttribute("error", imageValidation.getErrorMessage());
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/product_form";
        }

        FileUploadValidator.ValidationResult additionalImagesValidation = FileUploadValidator
                .validateImageFiles(additionalImageFiles);
        if (!additionalImagesValidation.isValid()) {
            model.addAttribute("error", additionalImagesValidation.getErrorMessage());
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/product_form";
        }

        try {
            // Auto-cleanup discount fields when discount is disabled
            if (product.getDiscountPercent() == null || product.getDiscountPercent() == 0) {
                product.setDiscountPercent(0);
            }

            // Priority 1: File upload for main image
            if (imageFile != null && !imageFile.isEmpty()) {
                String originalFilename = FileUploadValidator.sanitizeFilename(imageFile.getOriginalFilename());
                String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath);
                product.setImageUrl("/uploads/" + fileName);
            }
            // Priority 2: URL input (only if no file uploaded and URL is provided)
            else if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                product.setImageUrl(imageUrl.trim());
            }

            // Handle additional images upload
            if (additionalImageFiles != null && !additionalImageFiles.isEmpty()) {
                List<String> additionalUrls = new ArrayList<>();
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                for (MultipartFile additionalFile : additionalImageFiles) {
                    if (additionalFile != null && !additionalFile.isEmpty()) {
                        String originalFilename = FileUploadValidator
                                .sanitizeFilename(additionalFile.getOriginalFilename());
                        String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
                        Path filePath = uploadPath.resolve(fileName);
                        Files.copy(additionalFile.getInputStream(), filePath);
                        additionalUrls.add("/uploads/" + fileName);
                    }
                }

                if (!additionalUrls.isEmpty()) {
                    String existingImages = product.getAdditionalImages();
                    if (existingImages != null && !existingImages.trim().isEmpty()) {
                        additionalUrls.add(0, existingImages);
                    }
                    product.setAdditionalImages(String.join(",", additionalUrls));
                }
            }

            // Set createdAt for new products
            if (product.getId() == null) {
                product.setCreatedAt(LocalDateTime.now());
                if (product.getSold() == null) {
                    product.setSold(0);
                }
            }

            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("success", "Lưu sản phẩm thành công!");
            return "redirect:/admin/dashboard";
        } catch (IOException e) {
            model.addAttribute("error", "Lỗi khi upload file: " + e.getMessage());
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/product_form";
        }
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        List<Order> orders = orderService.getAllOrders();

        try {
            // Sort orders by creation time - newest first
            if (orders != null && !orders.isEmpty()) {
                orders = orders.stream()
                        .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                        .collect(Collectors.toList());
            }

            // Count orders by status
            int pendingCount = 0;
            int shippingCount = 0;
            int deliveredCount = 0;
            int cancelledCount = 0;

            if (orders != null) {
                for (Order order : orders) {
                    if (order.getStatus() != null) {
                        switch (order.getStatus()) {
                            case PENDING:
                                pendingCount++;
                                break;
                            case SHIPPING:
                                shippingCount++;
                                break;
                            case DELIVERED:
                                deliveredCount++;
                                break;
                            case CANCELLED:
                                cancelledCount++;
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            model.addAttribute("orders", orders);
            model.addAttribute("totalOrders", orders != null ? orders.size() : 0);
            model.addAttribute("pendingCount", pendingCount);
            model.addAttribute("shippingCount", shippingCount);
            model.addAttribute("deliveredCount", deliveredCount);
            model.addAttribute("cancelledCount", cancelledCount);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("orders", new ArrayList<>());
            model.addAttribute("totalOrders", 0);
            model.addAttribute("pendingCount", 0);
            model.addAttribute("shippingCount", 0);
            model.addAttribute("deliveredCount", 0);
            model.addAttribute("cancelledCount", 0);
        }
        return "admin/orders";
    }

    @PostMapping("/order/update-status/{id}")
    public String updateOrderStatus(@PathVariable Long id,
            @RequestParam Order.OrderStatus status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/admin/orders";
    }

    // ==================== USER MANAGEMENT ====================

    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping("/user/add")
    public String addUserForm(Model model) {
        User user = new User();
        user.setEnabled(true);
        user.setMemberLevel("Thành viên mới");
        user.setMemberPoints(0);
        model.addAttribute("user", user);
        return "admin/user_form";
    }

    @GetMapping("/user/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/user_form";
    }

    @PostMapping("/user/save")
    public String saveUser(@ModelAttribute User user) {
        if (user.getId() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/user/toggle/{id}")
    public String toggleUserStatus(@PathVariable Long id) {
        User user = userService.getUserById(id);
        user.setEnabled(!user.isEnabled());
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    // ==================== VOUCHER MANAGEMENT ====================

    @GetMapping("/vouchers")
    public String manageVouchers(Model model) {
        List<Voucher> vouchers = voucherService.getAllVouchers();
        model.addAttribute("vouchers", vouchers);
        return "admin/vouchers";
    }

    @GetMapping("/voucher/add")
    public String addVoucherForm(Model model) {
        Voucher voucher = new Voucher();
        voucher.setActive(true);
        voucher.setUsedCount(0);
        voucher.setStartDate(LocalDateTime.now());
        voucher.setEndDate(LocalDateTime.now().plusMonths(1));
        model.addAttribute("voucher", voucher);
        model.addAttribute("allProducts", productService.getAllProducts());
        return "admin/voucher_form";
    }

    @GetMapping("/voucher/edit/{id}")
    public String editVoucherForm(@PathVariable Long id, Model model) {
        Voucher voucher = voucherService.getVoucherById(id);
        model.addAttribute("voucher", voucher);
        model.addAttribute("allProducts", productService.getAllProducts());
        return "admin/voucher_form";
    }

    @PostMapping("/voucher/save")
    public String saveVoucher(@Valid @ModelAttribute Voucher voucher,
            BindingResult result,
            @RequestParam(required = false) String active,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("voucher", voucher);
            model.addAttribute("allProducts", productService.getAllProducts());
            return "admin/voucher_form";
        }

        // Handle checkbox for modal form (checkbox sends "on" when checked)
        if (voucher.getId() == null) {
            voucher.setActive(active != null);
            if (voucher.getUsedCount() == null) {
                voucher.setUsedCount(0);
            }
        }
        voucherService.saveVoucher(voucher);
        return "redirect:/admin/vouchers";
    }

    @GetMapping("/voucher/toggle/{id}")
    public String toggleVoucherStatus(@PathVariable Long id) {
        Voucher voucher = voucherService.getVoucherById(id);
        voucher.setActive(!voucher.isActive());
        voucherService.saveVoucher(voucher);
        return "redirect:/admin/vouchers";
    }

    @GetMapping("/voucher/delete/{id}")
    public String deleteVoucher(@PathVariable Long id) {
        voucherService.deleteVoucher(id);
        return "redirect:/admin/vouchers";
    }

    @GetMapping("/debug/categories")
    @ResponseBody
    public String debugCategories() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== CATEGORY DEBUG ===\n");

        try {
            List<com.example.Clothesshoponline.model.Category> allCategories = categoryService.getAllCategories();
            sb.append("Total categories found: ").append(allCategories.size()).append("\n\n");

            for (com.example.Clothesshoponline.model.Category cat : allCategories) {
                sb.append("ID: ").append(cat.getId()).append("\n");
                sb.append("Name: [").append(cat.getName()).append("]\n");
                sb.append("Is Active: ").append(cat.isActive()).append("\n");
                sb.append("Display Order: ").append(cat.getDisplayOrder()).append("\n");
                sb.append("Name bytes: ");
                for (byte b : cat.getName().getBytes()) {
                    sb.append(String.format("0x%02X ", b));
                }
                sb.append("\n");
                sb.append("---\n");
            }
        } catch (Exception e) {
            sb.append("ERROR: ").append(e.getMessage()).append("\n");
            e.printStackTrace();
        }

        return sb.toString();
    }
}
