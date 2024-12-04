package com.example.springmart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service

public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String storePassword(String password) {
        // Weak MD5 hashing for password storage
        String hashedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, "testuser", hashedPassword);
        return "passwordStored";
    }
    public void registerUser(String username, String password, String email) {
        String hashedPassword = storePassword(password);
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, username, hashedPassword, email);
    }
}
