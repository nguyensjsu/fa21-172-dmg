package com.example.springbooks;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CartItemRepository extends JpaRepository<CartItem, Long> {
    public List<CartItem> findByCart(ShoppingCart cart);
}
