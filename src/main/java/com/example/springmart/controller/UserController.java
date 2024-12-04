package com.example.springmart.controller;

import com.example.springmart.model.User;
import com.example.springmart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email) {
        try {
            userService.registerUser(username, password, email);
            return "User registered successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    @GetMapping("/getUser")
    public String getUser(@RequestParam String username, Model model) {
        String sql = "SELECT * FROM users WHERE username = '" + username + "'";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email")
        ));
        model.addAttribute("users", users);
        return "userDetails";
    }
}