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
    private static final String PROP_MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String PROP_MAIL_SMTP_STARTTLS = "mail.smtp.starttls.enable";
    private static final String PROP_MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String PROP_MAIL_SMTP_PORT = "mail.smtp.port";

    private static String smtpHost;
    private static String smtpPort;
    private static String emailSender;
    private static String emailPassword;

    // costruttore privato per nascondere quello pubblico implicito (java:S1118)
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

            LOGGER.info("Configurazione email caricata con successo.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Errore nel caricamento della configurazione email.", e);
        }
    }

    private static Properties createEmailProperties() {
        Properties props = new Properties();
        props.put(PROP_MAIL_SMTP_AUTH, "true");
        props.put(PROP_MAIL_SMTP_STARTTLS, "true");
        props.put(PROP_MAIL_SMTP_HOST, smtpHost);
        props.put(PROP_MAIL_SMTP_PORT, smtpPort);
        return props;
    }

    public static void sendConfirmationEmail(String recipientEmail, String username) {
        Properties props = createEmailProperties();

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
            LOGGER.log(Level.INFO, "Email di conferma inviata a {0}", recipientEmail);
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Errore nell'invio dell'email", e);
        }
    }

    public static void sendOrderSummaryEmail(String recipientEmail, String nomeCliente, Map<Prodotto, Integer> carrello) {
        Properties props = createEmailProperties();

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
                        .append(" -> €").append(String.format("%.2f", subtotale)).append("\n");
            }

            content.append("\nTotale ordine: €").append(String.format("%.2f", totale));
            content.append("\n\nGrazie per il tuo acquisto!");

            message.setText(content.toString());

            Transport.send(message);
            LOGGER.log(Level.INFO, "Email riepilogo ordine inviata a {0}", recipientEmail);
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Errore nell'invio dell'email di ordine", e);
        }
    }

    public static void sendShippingConfirmationEmail(String recipientEmail, String nomeCliente, String codiceSpedizione) {
        Properties props = createEmailProperties();

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
            message.setSubject("Conferma Spedizione");

            String content = String.format("""
                Ciao %s,

                Il tuo ordine è stato spedito!
                Codice di spedizione: %s

                Grazie per aver ordinato da noi!

                """, nomeCliente, codiceSpedizione);

            message.setText(content);

            Transport.send(message);
            LOGGER.log(Level.INFO, "Email di spedizione inviata a {0}", recipientEmail);
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Errore nell'invio dell'email di spedizione", e);
        }
    }

    public static void sendPaymentConfirmationEmail(String recipientEmail, String nomeCliente, double importoBonificato, double totalePagato, double residuo) {
        Properties props = createEmailProperties();

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
            message.setSubject("Conferma ricezione bonifico");

            String content = String.format("""
                Ciao %s,

                Abbiamo ricevuto un tuo bonifico di €%.2f.

                Totale pagato finora: €%.2f
                Importo residuo: €%.2f

                Grazie per l'acquisto!
                """, nomeCliente, importoBonificato, totalePagato, residuo);

            message.setText(content);

            Transport.send(message);
            LOGGER.log(Level.INFO, "Email di conferma bonifico inviata a {0}", recipientEmail);
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Errore nell'invio dell'email di conferma bonifico", e);
        }
    }
}