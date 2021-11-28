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


//@Entity
//@Table(name = "Books" )
@Data
@RequiredArgsConstructor
class Book {
   private @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long bookID;
   private String isbn ;
   private String title ;
   private String author ;
   private String description;
   private float price;
   private int stock ;
}
