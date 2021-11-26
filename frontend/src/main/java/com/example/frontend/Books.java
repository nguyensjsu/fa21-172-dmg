package com.example.frontend;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
class Books {
   //private @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long book_id;
   private String isbn ;
   private String title ;
   private String author ;
   private String description;
   private float price;
   private int stock ;

    //    @Column(nullable = false)  private Long isbn ;
//    @Column(nullable = false)   private String title ;
//    @Column(nullable = false)   private String author ;
//
//    @Column(nullable = false)   private int stock ;
    
}
