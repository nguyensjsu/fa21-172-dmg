package com.example.springusers;

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
@Table(name = "Users")
@Data
@RequiredArgsConstructor
class User {
    private @Id @GeneratedValue Long id;
    private String firstName ;
    private String lastName ;
    private String email ;
//    @Column(nullable = false)   private String firstName ;
//    @Column(nullable = false)   private String lastName ;
//    @Column(nullable = false)   private String email ;
//    @Column(nullable = false)   private double author ;
   // @Column(nullable = false)   private boolean edition ;
//    @Column(nullable = false)   private int stock ;
    
}
