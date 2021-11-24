package com.example.springbooks;

import org.springframework.data.jpa.repository.JpaRepository;

interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    
}
