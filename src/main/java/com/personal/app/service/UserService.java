package com.personal.app.service;
import com.personal.app.model.User;
import com.personal.app.repository.UserRepository;
import com.personal.app.utils.EncryptionUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder("ACC");
        for (int i = 0; i < 9; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public User registerUser(String fullName, String email, byte[] fingerprintImage, String pin, Double initialBalance) throws Exception {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered!");
        }

        // Generate AES key
        var key = EncryptionUtil.generateKey();
        byte[] encryptedFingerprint = EncryptionUtil.encrypt(fingerprintImage, key);

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setFingerprintImage(encryptedFingerprint); // store encrypted JPEG
        user.setFingerprintKey(EncryptionUtil.encodeKey(key)); // store AES key (Base64)
        user.setPinHash(passwordEncoder.encode(pin));
        user.setAccountNumber(generateAccountNumber());
        user.setBalance(initialBalance != null ? initialBalance : 0.0);

        return userRepository.save(user);
    }

    // Optional: decrypt fingerprint
    public byte[] decryptFingerprint(User user) throws Exception {
        var key = EncryptionUtil.decodeKey(user.getFingerprintKey());
        return EncryptionUtil.decrypt(user.getFingerprintImage(), key);
    }
}