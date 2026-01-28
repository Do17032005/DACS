package com.example.Clothesshoponline.service;

import com.example.Clothesshoponline.model.Cart;
import com.example.Clothesshoponline.model.CartItem;
import com.example.Clothesshoponline.model.Product;
import com.example.Clothesshoponline.model.User;
import com.example.Clothesshoponline.repository.CartItemRepository;
import com.example.Clothesshoponline.repository.CartRepository;
import com.example.Clothesshoponline.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get or create cart for user
     */
    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart cart = new Cart(user);
                    return cartRepository.save(cart);
                });
    }

    /**
     * Get cart by user
     */
    public Optional<Cart> getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

    /**
     * Add item to cart
     */
    @Transactional
    public CartItem addToCart(User user, Long productId, Integer quantity, String size, String color) {
        Cart cart = getOrCreateCart(user);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        // Check if item with same product, size, color exists
        Optional<CartItem> existingItem = cartItemRepository
                .findByCartAndProductAndSizeAndColor(cart, product, size, color);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(cart);
            return cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem(product, quantity, size, color);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(cart);
            return cartItemRepository.save(newItem);
        }
    }

    /**
     * Update item quantity
     */
    @Transactional
    public CartItem updateQuantity(User user, Long productId, Integer quantity) {
        Cart cart = getOrCreateCart(user);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng"));

        if (quantity <= 0) {
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
            return null;
        } else {
            item.setQuantity(quantity);
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(cart);
            return cartItemRepository.save(item);
        }
    }

    /**
     * Update item quantity by delta (+1 or -1)
     */
    @Transactional
    public CartItem updateQuantityByDelta(User user, Long productId, Integer delta) {
        Cart cart = getOrCreateCart(user);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng"));

        int newQuantity = item.getQuantity() + delta;

        if (newQuantity <= 0) {
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
            return null;
        } else {
            item.setQuantity(newQuantity);
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(cart);
            return cartItemRepository.save(item);
        }
    }

    /**
     * Remove item from cart
     */
    @Transactional
    public void removeFromCart(User user, Long productId) {
        Cart cart = getOrCreateCart(user);

        cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    cart.getItems().remove(item);
                    cartItemRepository.delete(item);
                    cart.setUpdatedAt(LocalDateTime.now());
                    cartRepository.save(cart);
                });
    }

    /**
     * Clear cart
     */
    @Transactional
    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        cart.getItems().clear();
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    /**
     * Get cart items
     */
    public List<CartItem> getCartItems(User user) {
        Cart cart = getOrCreateCart(user);
        return cart.getItems();
    }

    /**
     * Get cart total
     */
    public BigDecimal getCartTotal(User user) {
        Cart cart = getOrCreateCart(user);
        return cart.getTotalAmount();
    }

    /**
     * Get cart item count
     */
    public int getCartItemCount(User user) {
        return cartRepository.findByUser(user)
                .map(cart -> cart.getItems().size())
                .orElse(0);
    }
}
