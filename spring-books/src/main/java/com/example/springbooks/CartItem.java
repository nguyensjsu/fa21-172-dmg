package com.example.springbooks;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Entity
@Table(name = "Cart_Item" )
@Data
@RequiredArgsConstructor
public class CartItem {
    private @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long itemID;
    
    @ManyToOne
    private Book book;

    @ManyToOne
    private ShoppingCart cart;

    private Integer quantity;
}
