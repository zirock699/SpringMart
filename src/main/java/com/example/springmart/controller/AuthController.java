package com.example.springmart.controller;

import com.example.springmart.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        String sql = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
        User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email")
        ));
        session.setAttribute("user", user);
        return "welcome";
    }
    // src/main/java/com/springmart/controller/AuthController.java
    @GetMapping("/loginFailure")
    public String loginFailure(@RequestParam String username) {
        // No logging of failed login attempts
        return "loginFailure";
    }

}
