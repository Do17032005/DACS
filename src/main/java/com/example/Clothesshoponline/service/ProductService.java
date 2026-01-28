package com.example.Clothesshoponline.service;

import com.example.Clothesshoponline.model.Product;
import com.example.Clothesshoponline.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private static final int DEFAULT_PAGE_SIZE = 12;

    @Autowired
    private ProductRepository productRepository;

    private Sort getSortFromString(String sortType) {
        return switch (sortType) {
            case "price_asc" -> Sort.by("price").ascending();
            case "price_desc" -> Sort.by("price").descending();
            case "bestseller" -> Sort.by("sold").descending();
            default -> Sort.by("id").descending(); // newest
        };
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<Product> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return productRepository.findAll(pageable);
    }

    public Page<Product> getAllProducts(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, getSortFromString(sort));
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
    }

    public List<Product> getProductsByCategory(String category) {
        log.info("=== SEARCH DEBUG ===");
        log.info("Category search: {}", category);
        List<Product> products = productRepository.findByCategory(category);
        log.info("Found {} products", products.size());
        return products;
    }

    public Page<Product> getProductsByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return productRepository.findByCategory(category, pageable);
    }

    public Page<Product> getProductsByCategory(String category, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, getSortFromString(sort));
        return productRepository.findByCategory(category, pageable);
    }

    public List<Product> getBestsellers() {
        return productRepository.findBestsellers();
    }

    public Page<Product> getBestsellers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findBestsellers(pageable);
    }

    public Page<Product> getBestsellers(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, getSortFromString(sort));
        return productRepository.findBestsellers(pageable);
    }

    public List<Product> getNewProducts() {
        return productRepository.findNewProducts();
    }

    public Page<Product> getNewProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findNewProducts(pageable);
    }

    public Page<Product> getNewProducts(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, getSortFromString(sort));
        return productRepository.findNewProducts(pageable);
    }

    public List<Product> searchProducts(String keyword) {
        log.info("=== SEARCH DEBUG ===");
        log.info("Searching for keyword: '{}'", keyword);

        if (keyword == null || keyword.trim().isEmpty()) {
            log.warn("Empty keyword provided, returning all products");
            return productRepository.findAll();
        }

        List<Product> results = productRepository.searchProducts(keyword.trim());
        log.info("Found {} products for keyword: '{}'", results.size(), keyword);

        if (results.isEmpty()) {
            log.warn("No products found for keyword: '{}'", keyword);
        } else {
            results.forEach(p -> log.info("  - Product: {} (ID: {})", p.getName(), p.getId()));
        }

        return results;
    }

    public Page<Product> searchProducts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll(pageable);
        }
        return productRepository.searchProducts(keyword.trim(), pageable);
    }

    public Page<Product> searchProducts(String keyword, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, getSortFromString(sort));
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll(pageable);
        }
        return productRepository.searchProducts(keyword.trim(), pageable);
    }

    public List<Product> filterProducts(String category, Double minPrice, Double maxPrice) {
        return productRepository.findByFilters(category, minPrice, maxPrice);
    }

    public Page<Product> filterProducts(String category, String subcategory, Double minPrice, Double maxPrice,
            String size, String color, String brand, String material, String name,
            int page, int pageSize, String sort) {
        log.info(
                "Advanced filter - category: {}, subcategory: {}, minPrice: {}, maxPrice: {}, size: {}, color: {}, brand: {}, material: {}, name: {}",
                category, subcategory, minPrice, maxPrice, size, color, brand, material, name);
        Pageable pageable = PageRequest.of(page, pageSize, getSortFromString(sort));
        return productRepository.filterProducts(category, subcategory, minPrice, maxPrice, size, color, brand, material,
                name,
                pageable);
    }

    public List<String> getAllCategories() {
        return productRepository.findAllCategories();
    }

    public List<String> getAllBrands() {
        return productRepository.findAllBrands();
    }

    public List<String> getAllMaterials() {
        return productRepository.findAllMaterials();
    }

    public List<String> getAllColors() {
        return productRepository.findAllColors();
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product product = getProductById(id);

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setOriginalPrice(updatedProduct.getOriginalPrice());
        product.setDiscountPercent(updatedProduct.getDiscountPercent());
        product.setCategory(updatedProduct.getCategory());
        product.setBrand(updatedProduct.getBrand());
        product.setMaterial(updatedProduct.getMaterial());
        product.setColor(updatedProduct.getColor());
        product.setStock(updatedProduct.getStock());

        if (updatedProduct.getImageUrl() != null) {
            product.setImageUrl(updatedProduct.getImageUrl());
        }
        if (updatedProduct.getAdditionalImages() != null) {
            product.setAdditionalImages(updatedProduct.getAdditionalImages());
        }
        if (updatedProduct.getAvailableSizes() != null) {
            product.setAvailableSizes(updatedProduct.getAvailableSizes());
        }

        return productRepository.save(product);
    }
}
