package com.personal.app.service;

import com.personal.app.model.BankAccount;
import com.personal.app.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

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
}