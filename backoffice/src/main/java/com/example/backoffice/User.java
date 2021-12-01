package com.example.backoffice;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
class User {
    private String firstName ;
    private String lastName ;
    private String email ;
    private String password ;
    private String newPassword ;
//    @Column(nullable = false)   private String firstName ;
//    @Column(nullable = false)   private String lastName ;
//    @Column(nullable = false)   private String email ;
//    @Column(nullable = false)   private double author ;
   // @Column(nullable = false)   private boolean edition ;
//    @Column(nullable = false)   private int stock ;
    
}
