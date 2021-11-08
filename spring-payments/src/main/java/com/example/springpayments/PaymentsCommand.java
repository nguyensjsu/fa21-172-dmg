package com.example.springpayments;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity 
@Table(name="Payments")
@Data
@RequiredArgsConstructor
class PaymentsCommand {

    private @Id @GeneratedValue Long id;

    transient private String actipn;
    private String action ;
    private String firstname ;
    private String lastname ;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private String cardnum;
    private String cardexpmon;
    private String cardexpyear;
    private String cardcvv;
    private String email;
    private String notes;

    private String orderNumber;
    private String transactionAmount;
    private String transactionCurrency;
    private String authID;
    private String authStatus;
    private String captureId;
    private String captureStatus;

    

    
}
