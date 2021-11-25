package com.example.springbooks;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "Shopping_Cart" )
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class ShoppingCart {
    private @GeneratedValue(strategy=GenerationType.IDENTITY) @Id Long cartId;
    private @NonNull Long userId ;
}
