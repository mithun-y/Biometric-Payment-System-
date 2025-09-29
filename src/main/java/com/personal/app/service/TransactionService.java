package com.personal.app.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TransactionService {


    @Autowired
    private AuthService authService;

    @Autowired
    private BankService bankService;

    @Transactional
    public String makePayment(String pin, MultipartFile fingerprintFile, Double amount) throws Exception {

        //authenticating user
        String accountNum=authService.authenticate(pin,fingerprintFile);

        System.out.println(accountNum);

        if (accountNum == null) {
            return "failed";
        }

        Boolean bankStatus=bankService.makePayment(accountNum,amount);

        System.out.println(bankStatus);

        if(bankStatus){
            return "succuss";
        }

        return "failed";

    }
}