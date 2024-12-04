package com.example.springmart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @PostMapping("/checkout")
    public String checkout(@RequestParam int productId, @RequestParam int quantity, Model model) {
        // Missing inventory validation
        String sql = "INSERT INTO orders (product_id, quantity) VALUES (?, ?)";
        jdbcTemplate.update(sql, productId, quantity);
        return "checkoutComplete";
    }
}
