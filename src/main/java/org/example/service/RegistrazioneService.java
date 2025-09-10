package org.example.service;
import org.example.bean.ClienteBean;
import org.example.controllerapplicativo.SessionController;
import org.example.dao.ClienteDAO;
import org.example.dao.PagamentoDAOImpl;
import org.example.exception.RegistrazioneException;
import org.example.facade.ClienteFacade;
import org.example.model.Cliente;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrazioneService {
    private static final Logger LOGGER = Logger.getLogger(RegistrazioneService.class.getName());
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    private static final Pattern PARTITA_IVA_PATTERN = Pattern.compile("^\\d{11}$");
    private static final Pattern INDIRIZZO_PATTERN = Pattern.compile("^(?i)(Via|Piazza)\\s+[\\p{L}'\\s]{1,50}(?:\\s\\d{1,5}[a-z]?)?$");
    private static final Pattern CIVICO_PATTERN = Pattern.compile("^[0-9A-Za-z]{1,10}$");
    private static final Pattern CAP_PATTERN = Pattern.compile("^\\d{5}$");
    private final ClienteDAO clienteDAO;
    private final String codiceUnivoco;
    private int tentativiErrati = 0;
    private static final int MAX_TENTATIVI = 3;


    public RegistrazioneService(ClienteDAO clienteDAO, String codiceUnivoco) {
        this.clienteDAO = clienteDAO;
        this.codiceUnivoco = codiceUnivoco;
        LOGGER.log(Level.INFO, "Codice Univoco caricato in RegistrazioneService: {0}", codiceUnivoco);
    }


    public boolean isUsernameValid(String username) {
        return username.length() >= 8;
    }
    public boolean isPartitaIvaValid(String partitaIva) {
        return PARTITA_IVA_PATTERN.matcher(partitaIva).matches();
    }

    public boolean isEmailValid(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean isPasswordValid(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }
    public boolean isIndirizzoValid(String indirizzo) {
        return INDIRIZZO_PATTERN.matcher(indirizzo).matches();
    }

    public boolean isCivicoValid(String civico) {
        return CIVICO_PATTERN.matcher(civico).matches();
    }

    public boolean isCapValid(String cap) {
        return CAP_PATTERN.matcher(cap).matches();
    }

    public boolean isCodiceUnivocoValido(String codice) {
        LOGGER.log(Level.INFO, "Confronto codice univoco inserito: {0} con codice salvato: {1}", new Object[]{codice, codiceUnivoco});

        if (!codice.equals(codiceUnivoco)) {
            tentativiErrati++;
            LOGGER.log(Level.WARNING, "Codice Univoco errato! Tentativi rimanenti: {0}", getTentativiRimasti());
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

    public void registraCliente(ClienteBean clienteBean) throws RegistrazioneException {

        Cliente cliente = clienteBean.toCliente();
        LOGGER.log(Level.INFO, "Avvio registrazione cliente: {0}", cliente.getUsername());

        if (!isUsernameValid(cliente.getUsername())) {
            throw new RegistrazioneException("Lo username deve essere di almeno 8 caratteri.");
        }
        if (!isEmailValid(cliente.getEmail())) {
            throw new RegistrazioneException("L'email non è valida.");
        }
        if (!isPasswordValid(cliente.getPassword())) {
            throw new RegistrazioneException("La password non rispetta i requisiti di sicurezza.");
        }
        if (isBloccatoPerTroppiTentativi()) {
            throw new RegistrazioneException("Hai superato il numero massimo di tentativi per il codice univoco.");
        }
        if (!isPartitaIvaValid(cliente.getPartitaIva())) {
            throw new RegistrazioneException("La Partita IVA deve contenere 11 cifre.");
        }
        if (!isIndirizzoValid(cliente.getIndirizzo())) {
            throw new RegistrazioneException("L'indirizzo deve iniziare con 'Via' o 'Piazza'!");
        }
        if (!isCivicoValid(cliente.getCivico())) {
            throw new RegistrazioneException("Numero civico non valido!");
        }
        if (!isCapValid(cliente.getCap())) {
            throw new RegistrazioneException("Il CAP deve essere di 5 cifre!");
        }

        if (clienteDAO.findByUsername(cliente.getUsername()) != null) {
            throw new RegistrazioneException("Lo username è già in uso. Scegli un altro username.");
        }
        if (clienteDAO.findByEmail(cliente.getEmail()) != null) {
            throw new RegistrazioneException("L'email è già registrata. Usa un'altra email.");
        }

        try {
            clienteDAO.saveCliente(cliente);
            LOGGER.log(Level.INFO, "Cliente registrato con successo: {0}", cliente.getUsername());

            ClienteFacade facade = new ClienteFacade(new PagamentoDAOImpl(SessionController.getIsOnlineModeStatic()));
            facade.inviaEmailConfermaRegistrazione(cliente.getEmail(), cliente.getUsername());

        } catch (Exception e) {
            throw new RegistrazioneException("Errore durante la registrazione del cliente " + cliente.getUsername() +
                    ": " + e.getMessage());
        }

        resetTentativiErrati();
    }
}
