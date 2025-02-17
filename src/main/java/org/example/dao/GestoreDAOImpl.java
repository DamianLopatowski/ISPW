package org.example.dao;

import org.example.exception.DatabaseConfigurationException;
import org.example.exception.GestoreInitializationException;
import org.example.model.Gestore;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestoreDAOImpl implements GestoreDAO {
    private static final Logger LOGGER = Logger.getLogger(GestoreDAOImpl.class.getName());

    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private Gestore gestore;

    // Costruttore pubblico per Dependency Injection
    public GestoreDAOImpl() {
        try {
            loadDatabaseConfig();
            loadOfflineGestore();
        } catch (DatabaseConfigurationException e) {
            throw new GestoreInitializationException("Errore durante l'inizializzazione di GestoreDAOImpl", e);
        }
    }

    private void loadDatabaseConfig() throws DatabaseConfigurationException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            this.dbUrl = properties.getProperty("db.url");
            this.dbUsername = properties.getProperty("db.username");
            this.dbPassword = properties.getProperty("db.password");
        } catch (IOException e) {
            throw new DatabaseConfigurationException("Errore nel caricamento delle credenziali del database", e);
        }
    }

    private void loadOfflineGestore() throws DatabaseConfigurationException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            String offlineUsername = properties.getProperty("username");
            String offlinePassword = properties.getProperty("password");

            this.gestore = new Gestore(offlineUsername, offlinePassword);
            LOGGER.log(Level.INFO, "Caricato gestore offline: {0}", offlineUsername);
        } catch (IOException e) {
            throw new DatabaseConfigurationException("Errore nel caricamento del gestore offline", e);
        }
    }

    @Override
    public Gestore findByUsername(String username) {
        String query = "SELECT username, password FROM users WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Gestore(resultSet.getString("username"), resultSet.getString("password"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nel recupero del gestore dal database: {0}", e.getMessage());
        }
        return null;
    }

    public boolean authenticateOnline(String username, String password) {
        LOGGER.info("🔄 Controllo credenziali nel database per: " + username);
        Gestore dbGestore = findByUsername(username);

        if (dbGestore != null) {
            LOGGER.info("✅ Utente trovato nel database: " + dbGestore.getUsername());
            boolean success = dbGestore.getPassword().equals(password);
            LOGGER.info(success ? "✅ Accesso online riuscito" : "❌ Password errata.");
            return success;
        } else {
            LOGGER.warning("❌ Utente non trovato nel database.");
            return false;
        }
    }

    public void refreshOnlineCredentials() {
        if (dbUrl == null) {
            LOGGER.info("🔄 Modalità offline, nessuna necessità di ricaricare le credenziali online.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement statement = connection.prepareStatement("SELECT username, password FROM users")) {

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                gestore = new Gestore(resultSet.getString("username"), resultSet.getString("password"));
                LOGGER.info("🔄 Credenziali online ricaricate: " + gestore.getUsername());
            }
        } catch (SQLException e) {
            LOGGER.severe("❌ Errore nel ricaricamento delle credenziali online: " + e.getMessage());
        }
    }


    public void resetToOfflineGestore() {
        if (dbUrl != null) {  // 🔄 **Se sei online, non resettare nulla**
            LOGGER.info("🔄 Modalità online attiva, non ripristino credenziali offline.");
            return;
        }

        try {
            loadOfflineGestore();
            LOGGER.info("✅ Ripristinato il gestore offline dopo il logout.");
        } catch (DatabaseConfigurationException e) {
            LOGGER.severe("❌ Errore nel ripristino delle credenziali offline: " + e.getMessage());
        }
    }

    public Gestore getGestore() {
        return gestore;
    }
}
