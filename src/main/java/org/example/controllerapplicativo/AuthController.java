package org.example.controllerapplicativo;

import org.example.dao.GestoreDAOImpl;
import org.example.exception.DatabaseConfigurationException;
import org.example.model.Gestore;

import java.util.logging.Logger;

public class AuthController {
    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());
    private final GestoreDAOImpl gestoreDAO;

    public AuthController() {
        GestoreDAOImpl tempGestoreDAO;
        try {
            tempGestoreDAO = GestoreDAOImpl.getInstance();
        } catch (DatabaseConfigurationException e) {
            LOGGER.severe("Errore nella configurazione del database: " + e.getMessage());
            tempGestoreDAO = null; // O gestisci un'istanza di fallback
        }
        this.gestoreDAO = tempGestoreDAO;
    }


    public void logout() {
        try {
            gestoreDAO.resetToOfflineGestore();
            LOGGER.info("Logout effettuato, credenziali offline ripristinate.");
        } catch (DatabaseConfigurationException e) {
            LOGGER.severe("Errore nel ripristino delle credenziali offline: " + e.getMessage());
        }
    }

    public boolean handleLogin(String username, String password, boolean isOfflineMode) {
        if (isOfflineMode) {
            Gestore gestore = gestoreDAO.getGestore();
            return gestore.getUsername().equals(username) && gestore.getPassword().equals(password);
        } else {
            return gestoreDAO.authenticateOnline(username, password);
        }
    }
}
