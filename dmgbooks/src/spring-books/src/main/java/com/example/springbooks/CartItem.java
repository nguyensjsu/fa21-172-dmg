package com.example.springbooks;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Entity
@Table(name = "Cart_Item" )
@Data
@RequiredArgsConstructor
public class CartItem {
    private @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long item_id;
    
    @ManyToOne
    private Book book;

    @ManyToOne
    private ShoppingCart cart;

    private Integer quantity;
}
