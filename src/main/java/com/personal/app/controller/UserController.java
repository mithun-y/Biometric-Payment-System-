package com.personal.app.controller;

import com.personal.app.model.User;
import com.personal.app.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
        User user = userService.registerUser(fullName, email, imageBytes, pin, initialBalance);
        return ResponseEntity.ok(user);
    }
}