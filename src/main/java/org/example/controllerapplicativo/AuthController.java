package org.example.controllerapplicativo;

import org.example.dao.GestoreDAOImpl;
import org.example.model.Gestore;

import java.util.logging.Logger;

public class AuthController {
    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());
    private final GestoreDAOImpl gestoreDAO;

    public AuthController(GestoreDAOImpl gestoreDAO) {
        this.gestoreDAO = gestoreDAO;
    }

    public void logout() {
        gestoreDAO.resetToOfflineGestore();
        LOGGER.info("Logout effettuato, credenziali offline ripristinate.");
    }

    public boolean handleLogin(String username, String password, boolean isOfflineMode) {
        LOGGER.info("Tentativo di login con username: " + username);

        if (isOfflineMode) {
            Gestore gestore = gestoreDAO.getGestore();
            LOGGER.info("Credenziali offline: " + gestore.getUsername());
            boolean success = gestore.getUsername().equals(username) && gestore.getPassword().equals(password);
            LOGGER.info(success ? "Accesso offline riuscito" : "Accesso offline fallito");
            return success;
        } else {
            boolean success = gestoreDAO.authenticateOnline(username, password);
            LOGGER.info(success ? "Accesso online riuscito" : "Accesso online fallito");
            return success;
        }
    }
}
