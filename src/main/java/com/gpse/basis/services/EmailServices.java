package com.gpse.basis.services;

public interface EmailServices {
    void sendEmail(String to, String subject, String body);
    void sendEmailwithToken(String receiver, String token);
}
