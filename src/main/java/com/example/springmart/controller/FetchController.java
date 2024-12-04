package com.example.springmart.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/api/fetch")
public class FetchController {
    @GetMapping("/fetchUrl")
    public String fetchUrl(@RequestParam String url, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        model.addAttribute("response", response);
        return "fetchResult";
    }


}
