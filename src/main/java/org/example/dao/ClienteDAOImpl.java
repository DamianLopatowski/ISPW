package org.example.dao;

import org.example.model.Cliente;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ClienteDAOImpl implements ClienteDAO {
    private static final Logger LOGGER = Logger.getLogger(ClienteDAOImpl.class.getName());
    private static final Map<String, Cliente> clientiOffline = new HashMap<>();
    private final boolean isOnlineMode;
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    public ClienteDAOImpl(boolean isOnlineMode) {
        this.isOnlineMode = isOnlineMode;
        loadDatabaseConfig();
    }

    private void loadDatabaseConfig() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            this.dbUrl = properties.getProperty("db.url");
            this.dbUsername = properties.getProperty("db.username");
            this.dbPassword = properties.getProperty("db.password");
            LOGGER.info("✅ Configurazione database caricata con successo!");
        } catch (IOException e) {
            LOGGER.severe("❌ Errore nel caricamento del file di configurazione: " + e.getMessage());
        }
    }

    @Override
    public void saveCliente(Cliente cliente) {
        String emailPulita = cliente.getEmail().trim().toLowerCase();
        String usernameOriginale = cliente.getUsername().trim();
        String passwordOriginale = cliente.getPassword().trim();

        if (isOnlineMode) {
            try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO usercliente (username, nome, cognome, password, email) VALUES (?, ?, ?, ?, ?)")) {

                LOGGER.log(Level.INFO, "🔄 Tentativo di salvataggio cliente nel DATABASE: {0}", usernameOriginale);

                stmt.setString(1, usernameOriginale);
                stmt.setString(2, cliente.getNome());
                stmt.setString(3, cliente.getCognome());
                stmt.setString(4, passwordOriginale);
                stmt.setString(5, emailPulita);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    LOGGER.info("✅ Cliente registrato nel database.");
                } else {
                    LOGGER.warning("❌ Nessuna riga inserita, possibile errore.");
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                LOGGER.severe("❌ Errore: Username già esistente!");
            } catch (SQLException e) {
                LOGGER.severe("❌ Errore SQL nella registrazione del cliente: " + e.getMessage());
            }
        } else {
            LOGGER.log(Level.INFO, "🔄 Salvataggio cliente in RAM (OFFLINE): {0}", usernameOriginale);

            if (clientiOffline.containsKey(usernameOriginale)) {
                LOGGER.warning("❌ Cliente già esistente in RAM!");
            } else {
                clientiOffline.put(usernameOriginale, cliente);
                LOGGER.info("✅ Cliente registrato in RAM.");
            }
        }
    }

    @Override
    public Cliente findByUsername(String username) {

        if (isOnlineMode) {  // ✅ Controlliamo la modalità scelta all'inizio!
            LOGGER.log(Level.INFO, "🔎 Ricerca cliente nel DATABASE: {0}", username);
            try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT username, nome, cognome, password, email FROM usercliente WHERE LOWER(username) = ?"
                 )){
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new Cliente(rs.getString("username"), rs.getString("nome"), rs.getString("cognome"), rs.getString("password"), rs.getString("email"));
                }
            } catch (SQLException e) {
                LOGGER.severe("❌ Errore nella ricerca del cliente: " + e.getMessage());
            }
        } else {
            LOGGER.log(Level.INFO, "🔎 Ricerca cliente in RAM (OFFLINE): {0}", username);
            Cliente cliente = clientiOffline.get(username);
            if (cliente != null) {
                LOGGER.log(Level.INFO, "✅ Cliente trovato in RAM: {0}", username);
            } else {
                LOGGER.log(Level.WARNING, "❌ Cliente NON trovato in RAM: {0}", username);
            }
            return cliente;
        }
        return null;
    }
}
