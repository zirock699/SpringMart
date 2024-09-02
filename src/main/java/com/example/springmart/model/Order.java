package com.example.springmart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String product_name;
    private int quantity;
    private BigDecimal price;

    public Order(int id, String productName, int quantity, BigDecimal price) {
    }
}
