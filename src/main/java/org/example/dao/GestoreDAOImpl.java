package org.example.dao;

import org.example.exception.DatabaseConfigurationException;
import org.example.model.Gestore;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestoreDAOImpl implements GestoreDAO {
    private static final Logger LOGGER = Logger.getLogger(GestoreDAOImpl.class.getName());
    private static volatile GestoreDAOImpl instance; // Volatile per il double-checked locking

    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private Gestore gestore; // Unico riferimento al gestore

    private GestoreDAOImpl() throws DatabaseConfigurationException {
        loadDatabaseConfig();
        loadOfflineGestore(); // Carica il gestore offline inizialmente
    }

    public static GestoreDAOImpl getInstance() throws DatabaseConfigurationException {
        if (instance == null) {
            synchronized (GestoreDAOImpl.class) {
                if (instance == null) {
                    instance = new GestoreDAOImpl();
                }
            }
        }
        return instance;
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
        Gestore dbGestore = findByUsername(username);
        if (dbGestore != null && dbGestore.getPassword().equals(password)) {
            this.gestore = dbGestore; // Aggiorna il riferimento al gestore con quello online
            LOGGER.log(Level.INFO, "Login online riuscito: {0}", username);
            return true;
        }
        LOGGER.warning("Credenziali errate per login online.");
        return false;
    }

    public void resetToOfflineGestore() throws DatabaseConfigurationException {
        loadOfflineGestore(); // Ricarica il gestore offline da config.properties
        LOGGER.info("Ripristinato il gestore offline dopo il logout.");
    }

    public Gestore getGestore() {
        return gestore;
    }
}
