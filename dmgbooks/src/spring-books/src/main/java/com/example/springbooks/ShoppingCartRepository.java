package com.example.springbooks;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {
    ShoppingCart findByCartId(Long cartId);
    ShoppingCart findByUserId(Long userId);
}
