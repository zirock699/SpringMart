package com.example.springmart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String username;
    private String password;
    private String email;

    public User(int id, String username, String password, String email) {
    }

    public User() {

    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }


    public int getId() {
        return this.id;
    }
}
