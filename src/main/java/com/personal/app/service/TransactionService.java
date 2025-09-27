package com.personal.app.service;

import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintImageOptions;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;
import com.personal.app.model.User;
import com.personal.app.repository.UserRepository;
import com.personal.app.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptionUtil aesUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KeyProvider keyProvider;

    private final FingerprintMatcher matcher = new FingerprintMatcher();


    public double compare(byte[] probeBytes, byte[] candidateBytes) {
        FingerprintTemplate probe = templateFromImageBytes(probeBytes);
        FingerprintTemplate candidate = templateFromImageBytes(candidateBytes);


        FingerprintMatcher matcher = new FingerprintMatcher(probe);
        double score = matcher.match(candidate);
        return score;
    }
    public FingerprintTemplate templateFromImageBytes(byte[] imageBytes) {

        FingerprintImage image = new FingerprintImage(imageBytes);
        return new FingerprintTemplate(image);
    }


    public String makePayment(String pin, MultipartFile fingerprintFile, Double amount) throws Exception {
        byte[] uploadedBytes = fingerprintFile.getBytes();

        // Convert uploaded image → template
        FingerprintTemplate uploadedTemplate = new FingerprintTemplate(
                new FingerprintImage(uploadedBytes)
        );

        User matchedUser = null;
        double bestScore = 0;

        // Search in DB
        for (User user : userRepository.findAll()) {

            // Decrypt stored fingerprint
            byte[] decryptedImageBytes = aesUtil.decrypt(user.getFingerprintImage(), keyProvider.getMasterKey());

            // Convert stored image → template
            FingerprintTemplate storedTemplate = new FingerprintTemplate(
                    new FingerprintImage(decryptedImageBytes)
            );

            // Match uploaded template against stored template
            double score = new FingerprintMatcher(storedTemplate).match(uploadedTemplate);

            if (score > bestScore) {
                bestScore = score;
                matchedUser = user;
            }
        }

        if (matchedUser == null || bestScore < 40) { // threshold ~40
            throw new RuntimeException("Fingerprint not recognized");
        }
//        System.out.println(bestScore);

        // Verify PIN
        if (!passwordEncoder.matches(pin, matchedUser.getPinHash())) {
            throw new RuntimeException("Invalid PIN");
        }

        // Check balance
        if (matchedUser.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        // Deduct money
        matchedUser.setBalance(matchedUser.getBalance() - amount);
        userRepository.save(matchedUser);

        return "Payment successful! New balance: " + matchedUser.getBalance();
    }
}
