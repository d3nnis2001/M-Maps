package com.gpse.basis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Implementation des EmailServices.
 */
@Service
public class EmailServicesImpl implements EmailServices {
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Konstruktur zum Versenden der Email.
     * @param to - Empfänger
     * @param subject - Betreff
     * @param body - Nachricht
     */
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("gpseteam1.2@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

        System.out.println("Message was sent with success!");
    }

    /**
     * Email mit TOken versenden.
     * @param receiver - Empfänger
     * @param token - token
     */
    public void sendEmailwithToken(String receiver, String token) {
        String link = "http://localhost:8080/reset-password?email=" + receiver + "&token=" + token;
        String subject = "Password Reset";
        String body = "Hello,\n\nYou have requested to reset your password. "
            + "Please click on the link below to reset your password:\n\n"
            + link + "\n\nIf you did not request a password reset, please ignore this email.";
        sendEmail(receiver, subject, body);
    }

    /**
     * Email an den Gleisbauer schicken.
     * @param receiver - Empfänger
     * @param id - ID
     */
    public void builtEmailTrackBuilder(String receiver, String id) {
        String link = "http://localhost:8080/repair-order-trackbuilder?id=" + id;
        String subject = "Repair Order";
        String body = "Hello,\n\nYou have a new repair order. "
            + "Please click on the link below to open the repair order:\n\n" + link;
        sendEmail(receiver, subject, body);
    }
}
