package com.gpse.basis.services;

import com.gpse.basis.domain.Vorlage;
import com.gpse.basis.repositories.VorlagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Implementation des EmailServices.
 */
@Service
public class EmailServicesImpl implements EmailServices {
    @Autowired
    private JavaMailSender mailSender;
    private VorlagenRepository vorlagenrepo;
    private VorlagenService vorlagenService;
    private String link_string = "${Link}";

    public EmailServicesImpl(VorlagenService vorlagenService) {
        this.vorlagenService = vorlagenService;
    }

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
    //Hier Vorlage holen, wie mache ich das mit den Platzhaltern?
    public void sendEmailwithToken(String receiver, String token) {
        ArrayList<Vorlage> aktuellesVorlagenArray = vorlagenService.getVorlagenData();
        System.out.println(aktuellesVorlagenArray);
        Vorlage aktuelleVorlage = aktuellesVorlagenArray.stream()
            .filter(vorlage -> vorlage.getVorlagenId().equals("1"))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Vorlage mit ID 1 nicht gefunden"));
        System.out.println(aktuelleVorlage);
        String subject = aktuelleVorlage.getStringSubject();
        System.out.println(subject);
        String body = aktuelleVorlage.getStringBody();
        System.out.println(body);
        String link = "http://localhost:8080/reset-password?email=" + receiver + "&token=" + token;
        body = body.replace(link_string, link);
        sendEmail(receiver, subject, body);
    }

    /**
     * Email an den Gleisbauer schicken.
     * @param receiver - Empfänger
     * @param id - ID
     */
    public void builtEmailTrackBuilder(String receiver, String id) {
        ArrayList<Vorlage> aktuellesVorlagenArray = vorlagenService.getVorlagenData();
        System.out.println(aktuellesVorlagenArray);
        Vorlage aktuelleVorlage = aktuellesVorlagenArray.stream()
            .filter(vorlage -> vorlage.getVorlagenId().equals("2"))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Vorlage mit ID 2 nicht gefunden"));
        System.out.println(aktuelleVorlage);
        String subject = aktuelleVorlage.getStringSubject();
        System.out.println(subject);
        String body = aktuelleVorlage.getStringBody();
        System.out.println(body);
        String link = "http://localhost:8080/repair-order-trackbuilder?id=" + id;
        body = body.replace(link_string, link);
        body = body.replace("${Auftragsnummer}", id);
        sendEmail(receiver, subject, body);
    }
}
