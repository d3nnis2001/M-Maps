package com.gpse.basis.services;

/**
 * Interface für den EmailService.
 */
public interface EmailServices {
    void sendEmail(String to, String subject, String body);
    void sendEmailwithToken(String receiver, String token);
    void builtEmailTrackBuilder(String receiver, String id);
}
