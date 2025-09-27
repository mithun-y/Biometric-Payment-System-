package com.personal.app.service;
import com.personal.app.model.User;
import com.personal.app.repository.UserRepository;
import com.personal.app.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private EncryptionUtil aesUtil;

    @Autowired
    private KeyProvider keyProvider;

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


        byte[] encryptedFingerprint = aesUtil.encrypt(fingerprintImage, keyProvider.getMasterKey());

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setFingerprintImage(encryptedFingerprint); // store encrypted JPEG
        user.setPinHash(passwordEncoder.encode(pin));
        user.setAccountNumber(generateAccountNumber());
        user.setBalance(initialBalance != null ? initialBalance : 0.0);

        return userRepository.save(user);
    }

    // Optional: decrypt fingerprint
    public byte[] decryptFingerprint(User user) throws Exception {
        return EncryptionUtil.decrypt(user.getFingerprintImage(), keyProvider.getMasterKey());
    }
}