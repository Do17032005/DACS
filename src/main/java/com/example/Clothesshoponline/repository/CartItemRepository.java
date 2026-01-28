package com.example.Clothesshoponline.repository;

import com.example.Clothesshoponline.model.Cart;
import com.example.Clothesshoponline.model.CartItem;
import com.example.Clothesshoponline.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    Optional<CartItem> findByCartAndProductAndSizeAndColor(Cart cart, Product product, String size, String color);

    void deleteByCart(Cart cart);
}
