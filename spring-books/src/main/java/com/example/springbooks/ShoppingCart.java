package com.example.springbooks;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Entity
@Table(name = "Shopping_Cart" )
@Data
@RequiredArgsConstructor
public class ShoppingCart {
    private @Id @GeneratedValue Long cart_id;
    private Long user_id ;
}
