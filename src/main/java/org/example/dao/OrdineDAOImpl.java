package org.example.dao;

import org.example.model.Ordine;
import org.example.model.Prodotto;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdineDAOImpl {
    private static final Logger LOGGER = Logger.getLogger(OrdineDAOImpl.class.getName());
    private static final List<Ordine> ordiniOffline = new ArrayList<>();

    private final boolean isOnlineMode;
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    public OrdineDAOImpl(boolean isOnlineMode) {
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

    public void salvaOrdine(Ordine ordine) {
        if (isOnlineMode) {
            try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                 PreparedStatement stmt = connection.prepareStatement(
                         "INSERT INTO ordini (cliente_username, data, totale) VALUES (?, ?, ?)",
                         Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, ordine.getCliente().getUsername());
                stmt.setTimestamp(2, Timestamp.valueOf(ordine.getData()));
                stmt.setDouble(3, ordine.getTotale());
                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int ordineId = generatedKeys.getInt(1);
                        ordine.setId(ordineId);

                        try (PreparedStatement psProdotti = connection.prepareStatement(
                                "INSERT INTO ordine_prodotti (ordine_id, prodotto_id, quantita) VALUES (?, ?, ?)")) {

                            for (Map.Entry<Prodotto, Integer> entry : ordine.getProdotti().entrySet()) {
                                psProdotti.setInt(2, entry.getKey().getId());
                                psProdotti.setInt(3, entry.getValue());
                                psProdotti.setInt(1, ordineId); // spostato dopo per chiarezza ma fuori dal ciclo non è possibile con JDBC
                                psProdotti.addBatch();
                            }

                            psProdotti.executeBatch();
                        }

                        LOGGER.info("✅ Ordine salvato con successo nel database.");
                    }
                }

            } catch (SQLException e) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, String.format("❌ Errore nel salvataggio dell'ordine: %s", e.getMessage()), e);
                }
            }
        } else {
            ordiniOffline.add(ordine);
            LOGGER.info("🟢 Ordine salvato in modalità offline in memoria.");
        }
    }
}
