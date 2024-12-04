package com.example.springmart.controller;

import com.example.springmart.model.Order;
import com.example.springmart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.springmart.model.Order;
import com.example.springmart.model.Product;
import com.example.springmart.model.User;
import com.example.springmart.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
@Controller
public class AdminController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/admin")
    public String adminPanel(@RequestParam String userId, Model model) {
        // No role validation for admin access
        String sql = "SELECT * FROM orders";
        List<Order> orders = jdbcTemplate.query(sql, (rs, rowNum) -> new Order(
                rs.getInt("id"),
                rs.getString("product_name"),
                rs.getInt("quantity"),
                rs.getBigDecimal("price")
        ));
        model.addAttribute("orders", orders);
        return "adminPanel";
    }
}
