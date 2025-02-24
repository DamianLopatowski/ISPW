package org.example.service;

import org.example.dao.ClienteDAO;
import org.example.model.Cliente;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrazioneService {
    private static final Logger LOGGER = Logger.getLogger(RegistrazioneService.class.getName());

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

    private final ClienteDAO clienteDAO;
    private final String codiceUnivoco;
    private int tentativiErrati = 0;
    private static final int MAX_TENTATIVI = 3;

    public RegistrazioneService(ClienteDAO clienteDAO, String codiceUnivoco) {
        this.clienteDAO = clienteDAO;
        this.codiceUnivoco = codiceUnivoco;
        LOGGER.log(Level.INFO, "✅ Codice Univoco caricato in RegistrazioneService: {0}", codiceUnivoco);
    }


    public boolean isUsernameValid(String username) {
        return username.length() >= 8;
    }

    public boolean isEmailValid(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean isPasswordValid(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public boolean isCodiceUnivocoValido(String codice) {
        LOGGER.log(Level.INFO, "📢 Confronto codice univoco inserito: {0} con codice salvato: {1}", new Object[]{codice, codiceUnivoco});

        if (!codice.equals(codiceUnivoco)) {
            tentativiErrati++;
            LOGGER.log(Level.WARNING, "❌ Codice Univoco errato! Tentativi rimanenti: {0}", getTentativiRimasti());
            return false;
        }
        return true;
    }

    public int getTentativiRimasti() {
        return MAX_TENTATIVI - tentativiErrati;
    }

    public boolean isBloccatoPerTroppiTentativi() {
        return tentativiErrati >= MAX_TENTATIVI;
    }

    public void resetTentativiErrati() {
        tentativiErrati = 0;
    }

    public void registraCliente(String username, String nome, String cognome, String password, String email) throws RegistrazioneException {
        if (!isUsernameValid(username)) {
            throw new RegistrazioneException("Lo username deve essere di almeno 8 caratteri.");
        }
        if (!isEmailValid(email)) {
            throw new RegistrazioneException("L'email non è valida.");
        }
        if (!isPasswordValid(password)) {
            throw new RegistrazioneException("La password non rispetta i requisiti di sicurezza.");
        }
        if (isBloccatoPerTroppiTentativi()) {
            throw new RegistrazioneException("Hai superato il numero massimo di tentativi per il codice univoco.");
        }

        // Controllo se lo username o l'email esistono già
        if (clienteDAO.findByUsername(username) != null) {
            throw new RegistrazioneException("Lo username è già in uso. Scegli un altro username.");
        }
        if (clienteDAO.findByEmail(email) != null) {
            throw new RegistrazioneException("L'email è già registrata. Usa un'altra email.");
        }

        Cliente nuovoCliente = new Cliente(username, nome, cognome, password, email);

        try {
            clienteDAO.saveCliente(nuovoCliente);
        } catch (Exception e) {
            throw new RegistrazioneException("Errore durante la registrazione del cliente: " + e.getMessage());
        }

        LOGGER.log(Level.INFO, "Cliente registrato con successo: {0}", username);
        resetTentativiErrati();
    }
}
