package com.personal.app.model;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Lob
    private byte[] fingerprintImage; // store encrypted JPEG

    private String pinHash;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    private Double balance = 0.0;

    private LocalDateTime createdAt = LocalDateTime.now();
}