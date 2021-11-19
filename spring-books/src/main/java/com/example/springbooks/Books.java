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
@Table(name = "Books" )
@Data
@RequiredArgsConstructor
class Books {
   private @Id @GeneratedValue Long id;
   private Long isbn ;
   private String title ;
   private String author ;
   private int stock ;

    //    @Column(nullable = false)  private Long isbn ;
//    @Column(nullable = false)   private String title ;
//    @Column(nullable = false)   private String author ;
//
//    @Column(nullable = false)   private int stock ;
    
}
