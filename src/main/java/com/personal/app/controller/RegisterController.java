package com.personal.app.controller;

import com.personal.app.model.User;
import com.personal.app.service.RegisterService;
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
    public ResponseEntity<User> registerUser(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam MultipartFile fingerprintImage,
            @RequestParam String pin,
            @RequestParam(required = false) Double initialBalance) throws Exception {

        byte[] imageBytes = fingerprintImage.getBytes();
        User user = registerService.registerUser(fullName, email, imageBytes, pin, initialBalance);
        return ResponseEntity.ok(user);
    }
}