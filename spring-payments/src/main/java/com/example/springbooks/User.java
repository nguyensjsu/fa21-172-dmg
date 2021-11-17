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
@Table(indexes=@Index(name = "altIndex", columnList = "cardNumber", unique = true))
@Data
@RequiredArgsConstructor
class User {
    
    @Id @Column(nullable = false)   private Long id ;
    @Column(nullable = false)   private String firstName ;
    @Column(nullable = false)   private String lastName ;
    @Column(nullable = false)   private double author ;
   // @Column(nullable = false)   private boolean edition ;
    @Column(nullable = false)   private int stock ;
    
}
