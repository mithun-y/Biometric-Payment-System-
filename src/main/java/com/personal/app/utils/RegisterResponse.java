package com.personal.app.utils;

import com.personal.app.model.BankAccount;
import com.personal.app.model.User;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class RegisterResponse {
    private User user;
    private BankAccount bankAccount;

    public RegisterResponse(User user, BankAccount bankAccount) {
        this.user = user;
        this.bankAccount = bankAccount;
    }

}