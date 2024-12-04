package com.example.springmart.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/api/files")
public class FileController {


    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        Path path = Paths.get("/uploads/" + file.getOriginalFilename());
        Files.write(path, file.getBytes());
        return "fileUploaded";
    }

}
