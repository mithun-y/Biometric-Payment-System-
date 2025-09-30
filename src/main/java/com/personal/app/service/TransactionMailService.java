package com.personal.app.service;
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.context.Context;
//import org.springframework.stereotype.Service;
//
//@Service
//public class TransactionMailService {
//    private final JavaMailSender mailSender;
//    private final TemplateEngine templateEngine;
//
//    public TransactionMailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
//        this.mailSender = mailSender;
//        this.templateEngine = templateEngine;
//    }
//
//    public void sendFingerprintDebitEmail(String to, Transaction txn) throws MessagingException {
//        Context ctx = new Context();
//        ctx.setVariable("name", txn.getCustomerName());
//        ctx.setVariable("transactionId", txn.getId());
//        ctx.setVariable("dateTime", txn.getDateTime()); // format as string e.g. "2025-09-30 15:34"
//        ctx.setVariable("amount", txn.getFormattedAmount());
//        ctx.setVariable("balance", txn.getFormattedBalance());
//        ctx.setVariable("maskedAccount", maskAccount(txn.getAccountNumber()));
//        ctx.setVariable("deviceInfo", txn.getDeviceInfo());
//        ctx.setVariable("status", txn.getStatus());
//        ctx.setVariable("supportLink", "https://github.com/mithun-y");
//        ctx.setVariable("supportPhone", "1800-XXX-XXX");
//        ctx.setVariable("year", java.time.LocalDate.now().getYear());
//
//        String html = templateEngine.process("transaction-email", ctx);
//
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//        helper.setTo(to);
//        helper.setSubject("Alert: Fingerprint Payment on your account");
//        helper.setText(html, true);
//
//        mailSender.send(message);
//    }
//
//    private String maskAccount(String acct) {
//        if (acct == null || acct.length() < 4) return "XXXX";
//        String last4 = acct.substring(acct.length()-4);
//        return "XXXX-XXXX-" + last4;
//    }
//}
