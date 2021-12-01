package com.example.backoffice;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder.Case;

import lombok.Data;
import lombok.RequiredArgsConstructor;

//import javax.validation.constraints.Min;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;

//@Entity
//@Table(name="Payments")
@Data
//@RequiredArgsConstructor
class BookCommand {

    //private @Id @GeneratedValue Long id;

    //transient private String action ;
    private String quantity1 ;
    private String quantity2 ;
    private String quantity3;
    private String quantity4;
    private String quantity5;
    private String quantity6;
    private String quantity7;
    private String quantity8;
    private String quantity9;
    private String quantity10;

    public BookCommand () {
        this.quantity1 = String.valueOf(1);
        this.quantity2 = String.valueOf(1);
        this.quantity3 = String.valueOf(1);
        this.quantity4 = String.valueOf(1);
        this.quantity5 = String.valueOf(1);
        this.quantity6 = String.valueOf(1);
        this.quantity7 = String.valueOf(1);
        this.quantity8 = String.valueOf(1);
        this.quantity9 = String.valueOf(1);
        this.quantity10 = String.valueOf(1);
    }

    public String getQuantity(String id) {
        String quantity = String.valueOf(1);

        switch (id) {
            case "1":
                quantity = this.quantity1;
                break;
            case "2":
                quantity = this.quantity2;
                break;
            case "3":
                quantity = this.quantity3;
                break;
            case "4":
                quantity = this.quantity4;
                break;
            case "5":
                quantity = this.quantity5;
                break;
            case "6":
                quantity = this.quantity6;
                break;
            case "7":
                quantity = this.quantity7;
                break;
            case "8":
                quantity = this.quantity8;
                break;
            case "9":
                quantity = this.quantity9;
                break;
            case "10":
                quantity = this.quantity10;
                break;
            default:
                break;
        }

        return quantity;
    }
}
