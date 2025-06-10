package org.example.dao;

import org.example.model.Cliente;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            LOGGER.info("Configurazione database caricata con successo!");
        } catch (IOException e) {
            LOGGER.severe("Errore nel caricamento del file di configurazione: " + e.getMessage());
        }
    }

    @Override
    public void saveCliente(Cliente cliente) {
        if (isOnlineMode) {
            try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO usercliente (username, nome, cognome, password, email, partita_iva, indirizzo, civico, cap, citta) " +
                                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                LOGGER.log(Level.INFO, "Tentativo di salvataggio cliente nel DATABASE: {0}", cliente.getUsername());

                stmt.setString(1, cliente.getUsername());
                stmt.setString(2, cliente.getNome());
                stmt.setString(3, cliente.getCognome());
                stmt.setString(4, cliente.getPassword());
                stmt.setString(5, cliente.getEmail());
                stmt.setString(6, cliente.getPartitaIva());
                stmt.setString(7, cliente.getIndirizzo());
                stmt.setString(8, cliente.getCivico());
                stmt.setString(9, cliente.getCap());
                stmt.setString(10, cliente.getCitta());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    LOGGER.info("Cliente registrato nel database.");
                } else {
                    LOGGER.warning("Nessuna riga inserita, possibile errore.");
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                LOGGER.severe("Errore: Username già esistente!");
            } catch (SQLException e) {
                LOGGER.severe("Errore SQL nella registrazione del cliente: " + e.getMessage());
            }
        } else {
            LOGGER.log(Level.INFO, "Salvataggio cliente in RAM (OFFLINE): {0}", cliente.getUsername());

            if (clientiOffline.containsKey(cliente.getUsername())) {
                LOGGER.warning("Cliente già esistente in RAM!");
            } else {
                clientiOffline.put(cliente.getUsername(), cliente);
                LOGGER.info("Cliente registrato in RAM.");
            }
        }
    }

    @Override
    public Cliente findByEmail(String email) {
        if (isOnlineMode) {
            LOGGER.log(Level.INFO, "Ricerca cliente nel DATABASE tramite email: {0}", email);
            try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT username, nome, cognome, password, email, partita_iva, indirizzo, civico, cap, citta " +
                                 "FROM usercliente WHERE LOWER(email) = ?")) {
                stmt.setString(1, email.toLowerCase());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new Cliente.Builder()
                            .username(rs.getString("username"))
                            .nome(rs.getString("nome"))
                            .cognome(rs.getString("cognome"))
                            .password(rs.getString("password"))
                            .email(rs.getString("email"))
                            .partitaIva(rs.getString("partita_iva"))
                            .indirizzo(rs.getString("indirizzo"))
                            .civico(rs.getString("civico"))
                            .cap(rs.getString("cap"))
                            .citta(rs.getString("citta"))
                            .build();
                }
            } catch (SQLException e) {
                LOGGER.severe("Errore nella ricerca del cliente per email: " + e.getMessage());
            }
        } else {
            LOGGER.log(Level.INFO, "Ricerca cliente in RAM tramite email: {0}", email);
            for (Cliente cliente : clientiOffline.values()) {
                if (cliente.getEmail().equalsIgnoreCase(email)) {
                    return cliente;
                }
            }
        }
        return null;
    }

    @Override
    public Cliente findByUsername(String username) {
        if (isOnlineMode) {
            LOGGER.log(Level.INFO, "Ricerca cliente nel DATABASE: {0}", username);
            try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT username, nome, cognome, password, email, partita_iva, indirizzo, civico, cap, citta " +
                                 "FROM usercliente WHERE LOWER(username) = ?")) {
                stmt.setString(1, username.toLowerCase());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new Cliente.Builder()
                            .username(rs.getString("username"))
                            .nome(rs.getString("nome"))
                            .cognome(rs.getString("cognome"))
                            .password(rs.getString("password"))
                            .email(rs.getString("email"))
                            .partitaIva(rs.getString("partita_iva"))
                            .indirizzo(rs.getString("indirizzo"))
                            .civico(rs.getString("civico"))
                            .cap(rs.getString("cap"))
                            .citta(rs.getString("citta"))
                            .build();
                }
            } catch (SQLException e) {
                LOGGER.severe("Errore nella ricerca del cliente per username: " + e.getMessage());
            }
        } else {
            LOGGER.log(Level.INFO, "Ricerca cliente in RAM (OFFLINE): {0}", username);

            Cliente cliente = clientiOffline.get(username);
            if (cliente == null) {
                LOGGER.log(Level.WARNING, "Cliente offline non trovato: {0}", username);
            }
            return cliente;
        }
        return null;
    }


    @Override
    public boolean update(Cliente cliente, String vecchioUsername) {
        loadDatabaseConfig();

        if (isOnlineMode) {
            String sql = "UPDATE usercliente SET nome = ?, cognome = ?, username = ?, password = ? WHERE username = ?";
            try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, cliente.getNome());
                stmt.setString(2, cliente.getCognome());
                stmt.setString(3, cliente.getUsername());
                stmt.setString(4, cliente.getPassword());
                stmt.setString(5, vecchioUsername);

                int rows = stmt.executeUpdate();
                LOGGER.log(Level.INFO, "Cliente aggiornato nel database. Righe modificate: {0}", rows);
                return rows > 0;
            } catch (SQLException e) {
                LOGGER.severe("Errore durante l'update del cliente: " + e.getMessage());
                return false;
            }
        } else {
            LOGGER.log(Level.INFO, "Aggiornamento cliente in RAM (OFFLINE): {0}", cliente.getUsername());
            clientiOffline.put(cliente.getUsername(), cliente);
            return true;
        }
    }

    public static void resetClientiOffline() {
        clientiOffline.clear();
        LOGGER.info("Clienti offline azzerati all'avvio in modalità OFFLINE.");
    }

}