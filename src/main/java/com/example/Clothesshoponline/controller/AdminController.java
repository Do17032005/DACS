package com.example.Clothesshoponline.controller;

import com.example.Clothesshoponline.model.Product;
import com.example.Clothesshoponline.service.ProductService;
import com.example.Clothesshoponline.service.FileStorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final com.example.Clothesshoponline.repository.UserRepository userRepository;
    private final com.example.Clothesshoponline.service.AuthService authService;
    private final com.example.Clothesshoponline.service.OrderService orderService;
    private final FileStorageService fileStorageService;

    public AdminController(ProductService productService,
            com.example.Clothesshoponline.repository.UserRepository userRepository,
            com.example.Clothesshoponline.service.AuthService authService,
            com.example.Clothesshoponline.service.OrderService orderService,
            FileStorageService fileStorageService) {
        this.productService = productService;
        this.userRepository = userRepository;
        this.authService = authService;
        this.orderService = orderService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/dashboard";
    }

    @GetMapping("/product/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/product_form";
    }

    @PostMapping("/product/save")
    public String saveProduct(@ModelAttribute Product product,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String storedPath = fileStorageService.store(imageFile);
            product.setImageUrl(storedPath);
        }

        productService.saveProduct(product);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/product/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        model.addAttribute("product",
                productService.getProductById(id).orElseThrow(() -> new RuntimeException("Product not found")));
        return "admin/product_form";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/user/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new com.example.Clothesshoponline.model.User());
        return "admin/user_form";
    }

    @PostMapping("/user/save")
    public String saveUser(@ModelAttribute com.example.Clothesshoponline.model.User user) {
        authService.createUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }

    @PostMapping("/order/status")
    public String updateOrderStatus(@RequestParam Long id, @RequestParam String status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/admin/orders";
    }
}
