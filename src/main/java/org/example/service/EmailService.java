package org.example.service;

import org.example.model.Prodotto;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailService {
    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    // 🔹 Campi rinominati per seguire le convenzioni Java (java:S3008)
    private static String smtpHost;
    private static String smtpPort;
    private static String emailSender;
    private static String emailPassword;

    // 🔹 Costruttore privato per nascondere quello pubblico implicito (java:S1118)
    private EmailService() {
        throw new UnsupportedOperationException("Questa è una classe di utility e non può essere istanziata.");
    }

    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("config.properties"));

            smtpHost = properties.getProperty("email.smtp.host");
            smtpPort = properties.getProperty("email.smtp.port");
            emailSender = properties.getProperty("email.sender");
            emailPassword = properties.getProperty("email.password");

            LOGGER.info("📩 Configurazione email caricata con successo.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "❌ Errore nel caricamento della configurazione email.", e);
        }
    }

    public static void sendConfirmationEmail(String recipientEmail, String username) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailSender, emailPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailSender));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Conferma Registrazione");
            message.setText(String.format("Ciao %s,%n%nGrazie per esserti registrato con successo!", username));

            Transport.send(message);

            // 🔹 Uso del logging con built-in formatting (java:S2629)
            LOGGER.log(Level.INFO, "✅ Email di conferma inviata a {0}", recipientEmail);
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "❌ Errore nell'invio dell'email", e);
        }
    }

    public static void sendOrderSummaryEmail(String recipientEmail, String nomeCliente, Map<Prodotto, Integer> carrello) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailSender, emailPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailSender));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Riepilogo del tuo ordine");

            StringBuilder content = new StringBuilder();
            content.append("Ciao ").append(nomeCliente).append(",\n\n")
                    .append("Ecco il riepilogo del tuo ordine:\n\n");

            double totale = 0.0;
            for (Map.Entry<Prodotto, Integer> entry : carrello.entrySet()) {
                Prodotto p = entry.getKey();
                int q = entry.getValue();
                double subtotale = p.getPrezzoVendita() * q;
                totale += subtotale;
                content.append("- ").append(p.getNome())
                        .append(" x").append(q)
                        .append(" → €").append(String.format("%.2f", subtotale)).append("\n");
            }

            content.append("\nTotale ordine: €").append(String.format("%.2f", totale));
            content.append("\n\nGrazie per il tuo acquisto!");

            message.setText(content.toString());

            Transport.send(message);
            LOGGER.log(Level.INFO, "✅ Email riepilogo ordine inviata a {0}", recipientEmail);
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "❌ Errore nell'invio dell'email di ordine", e);
        }
    }

}