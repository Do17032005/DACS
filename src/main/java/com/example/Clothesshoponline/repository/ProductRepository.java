package com.example.Clothesshoponline.repository;

import com.example.Clothesshoponline.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
        List<Product> findByCategory(String category);

        Page<Product> findByCategory(String category, Pageable pageable);

        // Tìm sản phẩm theo tên để update
        java.util.Optional<Product> findByName(String name);

        @Query("SELECT p FROM Product p WHERE p.isBestseller = true")
        List<Product> findBestsellers();

        @Query("SELECT p FROM Product p WHERE p.isBestseller = true")
        Page<Product> findBestsellers(Pageable pageable);

        @Query("SELECT p FROM Product p WHERE p.isNew = true")
        List<Product> findNewProducts();

        @Query("SELECT p FROM Product p WHERE p.isNew = true")
        Page<Product> findNewProducts(Pageable pageable);

        @Query("SELECT p FROM Product p WHERE " +
                        "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        List<Product> searchProducts(@Param("keyword") String keyword);

        @Query("SELECT p FROM Product p WHERE " +
                        "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);

        @Query("SELECT p FROM Product p WHERE " +
                        "(:category IS NULL OR p.category = :category) AND " +
                        "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
                        "(:maxPrice IS NULL OR p.price <= :maxPrice)")
        List<Product> findByFilters(
                        @Param("category") String category,
                        @Param("minPrice") Double minPrice,
                        @Param("maxPrice") Double maxPrice);

        @Query("SELECT p FROM Product p WHERE " +
                        "(:category IS NULL OR :category = '' OR p.category = :category) AND " +
                        "(:subcategory IS NULL OR :subcategory = '' OR LOWER(TRIM(p.subcategory)) = LOWER(TRIM(:subcategory))) AND "
                        +
                        "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
                        "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
                        "(:size IS NULL OR :size = '' OR p.availableSizes LIKE CONCAT('%', :size, '%')) AND " +
                        "(:color IS NULL OR :color = '' OR LOWER(p.color) LIKE LOWER(CONCAT('%', :color, '%'))) AND " +
                        "(:brand IS NULL OR :brand = '' OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND " +
                        "(:material IS NULL OR :material = '' OR LOWER(p.material) LIKE LOWER(CONCAT('%', :material, '%'))) AND "
                        +
                        "(:name IS NULL OR :name = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))")
        Page<Product> filterProducts(
                        @Param("category") String category,
                        @Param("subcategory") String subcategory,
                        @Param("minPrice") Double minPrice,
                        @Param("maxPrice") Double maxPrice,
                        @Param("size") String size,
                        @Param("color") String color,
                        @Param("brand") String brand,
                        @Param("material") String material,
                        @Param("name") String name,
                        Pageable pageable);

        @Query("SELECT DISTINCT p.category FROM Product p WHERE p.category IS NOT NULL ORDER BY p.category")
        List<String> findAllCategories();

        @Query("SELECT DISTINCT p.brand FROM Product p WHERE p.brand IS NOT NULL AND p.brand != '' ORDER BY p.brand")
        List<String> findAllBrands();

        @Query("SELECT DISTINCT p.material FROM Product p WHERE p.material IS NOT NULL AND p.material != '' ORDER BY p.material")
        List<String> findAllMaterials();

        @Query("SELECT DISTINCT p.color FROM Product p WHERE p.color IS NOT NULL AND p.color != '' ORDER BY p.color")
        List<String> findAllColors();
}
