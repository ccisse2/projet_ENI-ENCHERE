package fr.eni.projet.eniencheres.bll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetPasswordEmail(String to, String token) {
        String resetUrl = "http://localhost:8080/utilisateurs/reset-password/" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Réinitialisation de mot de passe");
        message.setText("Pour réinitialiser votre mot de passe, cliquez sur le lien suivant : " + resetUrl);

        mailSender.send(message);
    }

    public void sendVerificationEmail(String recipientEmail, String link) {
        String subject = "Vérification de l'email";
        String content = "Cliquez sur le lien ci-dessous pour vérifier votre email : \n" + link;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }
}