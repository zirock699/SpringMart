package com.example.springmart.controller;

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

@Controller
public class ProductController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ProductRepository productRepository;

    // SQL Injection Vulnerability
    @GetMapping("/search")
    public String searchProducts(@RequestParam String query, Model model) {
        String sql = "SELECT * FROM products WHERE name LIKE '%" + query + "%'";
        List<Product> products = jdbcTemplate.query(sql, (rs, rowNum) -> new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getBigDecimal("price")
        ));
        model.addAttribute("products", products);
        return "searchResults";
    }

    // XSS Vulnerability
    @GetMapping("/product")
    public String getProduct(@RequestParam String name, Model model) {
        String sql = "SELECT * FROM products WHERE name = ?";
        Product product = jdbcTemplate.queryForObject(sql, new Object[]{name}, (rs, rowNum) -> new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getBigDecimal("price")
        ));
        model.addAttribute("product", product);
        return "product";
    }

    // Insecure Deserialization Vulnerability
    @GetMapping("/deserialize")
    public String deserializeObject(@RequestParam("data") String data) {
        try {
            byte[] bytes = data.getBytes();
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            Object obj = ois.readObject();
            ois.close();
            // Vulnerable: Deserializing untrusted data
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "deserialized";
    }

    @PostMapping("/comment")
    public String addComment(@RequestParam String productId, @RequestParam String comment) {
        String sql = "INSERT INTO comments (product_id, comment) VALUES ('" + productId + "', '" + comment + "')";
        jdbcTemplate.execute(sql);
        return "redirect:/product/" + productId;
    }


    @GetMapping("/product/{id}")
    public String getProductById(@PathVariable String name, Model model) {
        String sql = "SELECT * FROM products WHERE name = ?";

        Product product = jdbcTemplate.queryForObject(sql, new Object[]{name}, (rs, rowNum) -> new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getBigDecimal("price")
        ));
        List<String> comments = jdbcTemplate.queryForList("SELECT comment FROM comments WHERE product_id = " + name, String.class);
        model.addAttribute("product", product);
        model.addAttribute("comments", comments);
        return "product";
    }
    @PostMapping("/updateProfile")
    @CrossOrigin(origins = "*")
    public String updateProfile(@RequestParam String username, @RequestParam String email) {
        String sql = "UPDATE users SET email = '" + email + "' WHERE username = '" + username + "'";
        jdbcTemplate.execute(sql);
        return "profileUpdated";
    }

    @GetMapping("/orders")
    public String getUserOrders(@RequestParam String userId, Model model) {
        String sql = "SELECT * FROM orders WHERE user_id = '" + userId + "'";
        List<Order> orders = jdbcTemplate.query(sql, (rs, rowNum) -> new Order(
                rs.getInt("id"),
                rs.getString("product_name"),
                rs.getInt("quantity"),
                rs.getBigDecimal("price")
        ));
        model.addAttribute("orders", orders);
        return "orders";
    }
    @PostMapping("/applyDiscount")
    public String applyDiscount(@RequestParam String userId, @RequestParam int discountCode) {
        // Simple discount logic without proper validation
        String sql = "UPDATE users SET discount = discount + ? WHERE id = ?";
        jdbcTemplate.update(sql, discountCode, userId);
        return "discountApplied";
    }


    @PostMapping("/updateProfile")
    public String updateProfile(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, role = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getId());
        return "profileUpdated";
    }

    @GetMapping("/userInfo")
    public String getUserInfo(@RequestParam String username, Model model) {
        String sql = "SELECT * FROM users WHERE username = '" + username + "'";
        User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),  // Exposing passwords
                rs.getString("email")
        ));
        model.addAttribute("user", user);
        return "userInfo";
    }
    @GetMapping("/redirect")
    public String redirectToUrl(@RequestParam String url) {
        return "redirect:" + url;  // Unvalidated Redirect
    }
    @GetMapping("/fetchData")
    public String fetchData(@RequestParam String url, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        model.addAttribute("data", response);
        return "dataView";
    }


    public String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return new String(hashInBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/adminPanel")
    public String adminPanel() {
        return "adminPanel";
    }
    @PostMapping("/validateCoupon")
    public String validateCoupon(@RequestParam String coupon) {
        // Vulnerable regex pattern
        if (coupon.matches("^(a+)+$")) {
            return "validCoupon";
        } else {
            return "invalidCoupon";
        }
    }




    @PostMapping("/upload")
    public String uploadObject(@RequestParam("file") MultipartFile file) {
        try (ObjectInputStream ois = new ObjectInputStream(file.getInputStream())) {
            Object obj = ois.readObject();  // Vulnerable to insecure deserialization
            // process the object
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "uploadSuccess";
    }


    @GetMapping("/config")
    public String showConfig(Model model) {
        model.addAttribute("dbUrl", "jdbc:mysql://localhost:3306/springmart");
        model.addAttribute("dbUser", "root");
        model.addAttribute("dbPassword", "password");  // Exposing sensitive configuration
        return "config";
    }


    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam String username) {
        String sql = "DELETE FROM users WHERE username = '" + username + "'";
        jdbcTemplate.execute(sql);  // No authorization check
        return "userDeleted";
    }

    @GetMapping("/triggerError")
    public String triggerError() {
        throw new RuntimeException("Intentional Error");  // Unhandled exception
    }


}
