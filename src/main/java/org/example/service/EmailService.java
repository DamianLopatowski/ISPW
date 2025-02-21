package org.example.service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailService {
    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    private static String SMTP_HOST;
    private static String SMTP_PORT;
    private static String EMAIL_SENDER;
    private static String EMAIL_PASSWORD;

    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("config.properties"));

            SMTP_HOST = properties.getProperty("email.smtp.host");
            SMTP_PORT = properties.getProperty("email.smtp.port");
            EMAIL_SENDER = properties.getProperty("email.sender");
            EMAIL_PASSWORD = properties.getProperty("email.password");

            LOGGER.info("📩 Configurazione email caricata con successo.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "❌ Errore nel caricamento della configurazione email.", e);
        }
    }

    public static void sendConfirmationEmail(String recipientEmail, String username) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_SENDER, EMAIL_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_SENDER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Conferma Registrazione");
            message.setText("Ciao " + username + ",\n\nGrazie per esserti registrato con successo!");

            Transport.send(message);
            LOGGER.info("✅ Email di conferma inviata a " + recipientEmail);
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "❌ Errore nell'invio dell'email", e);
        }
    }
}
