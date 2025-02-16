package org.example.controllerapplicativo;

import org.example.dao.GestoreDAOImpl;
import org.example.model.Gestore;

import java.util.logging.Logger;

public class AuthController {
    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());
    private final GestoreDAOImpl gestoreDAO;

    public AuthController() {
        this.gestoreDAO = GestoreDAOImpl.getInstance(); // Nessuna eccezione da gestire
    }

    public void logout() {
        gestoreDAO.resetToOfflineGestore(); // Nessuna eccezione da gestire
        LOGGER.info("Logout effettuato, credenziali offline ripristinate.");
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
