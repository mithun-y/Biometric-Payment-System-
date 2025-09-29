package com.personal.app.service;

import com.personal.app.model.BankAccount;
import com.personal.app.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Component
public class BankService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TokenService tokenService;


    public BankAccount createBankAccount(String accountNumber, Double initialBalance) {
        BankAccount account = new BankAccount();
        account.setAccountNumber(accountNumber);
        account.setBalance(initialBalance != null ? initialBalance : 0.0);
        return bankAccountRepository.save(account);
    }

    public Optional<BankAccount> getAccount(String accountNumber) {
        return bankAccountRepository.findById(accountNumber);
    }

    public void updateBalance(String accountNumber, Double newBalance) {
        BankAccount account = bankAccountRepository.findById(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(newBalance);
        bankAccountRepository.save(account);
    }

    public Boolean makePayment(String accountNumber,Double amount){
        try {
            if(accountNumber==null)  new RuntimeException("Account not found");

            Boolean tokenStatus=tokenService.validateAndUseToken(accountNumber);

            if (!tokenStatus) {
                throw new RuntimeException("Invalid or already used token");
            }

            BankAccount matchedUser = bankAccountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            // Check balance
            if (matchedUser.getBalance() < amount) {
                throw new RuntimeException("Insufficient balance");
            }

            // Deduct money
            matchedUser.setBalance(matchedUser.getBalance() - amount);
            bankAccountRepository.save(matchedUser);

            return true;
        } catch (RuntimeException e) {
//            throw new RuntimeException(e);
            return false;
        }

    }
}