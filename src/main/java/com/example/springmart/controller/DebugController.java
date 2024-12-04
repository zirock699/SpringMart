package com.example.springmart.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
public class DebugController {
    @GetMapping("/debugInfo")
    public String debugInfo(Model model) {
        model.addAttribute("javaVersion", System.getProperty("java.version"));
        model.addAttribute("osName", System.getProperty("os.name"));
        model.addAttribute("envVars", System.getenv());
        return "debugInfo";
    }
}
