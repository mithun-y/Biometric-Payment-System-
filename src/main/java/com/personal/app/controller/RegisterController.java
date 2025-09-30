package com.personal.app.controller;

import com.personal.app.service.RegisterService;
import com.personal.app.dto.RegisterResponse;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class RegisterController {
    private final RegisterService registerService;

    public RegisterController(RegisterService userService) {
        this.registerService = userService;
    }

    @GetMapping("/hello")
    public String greet(){
        return "hello";
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam MultipartFile fingerprintImage,
            @RequestParam String pin,
            @RequestParam(required = false) Double initialBalance) throws Exception {

        try {
            byte[] imageBytes = fingerprintImage.getBytes();
            RegisterResponse res = registerService.registerUser(fullName, email, imageBytes, pin, initialBalance);
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}