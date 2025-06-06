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
            try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
                String ordineSQL = "INSERT INTO ordini (cliente_username, data, totale) VALUES (?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(ordineSQL, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, ordine.getCliente().getUsername());
                stmt.setTimestamp(2, Timestamp.valueOf(ordine.getData()));
                stmt.setDouble(3, ordine.getTotale());
                stmt.executeUpdate();

                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int ordineId = generatedKeys.getInt(1);
                    ordine.setId(ordineId);

                    String prodottiSQL = "INSERT INTO ordine_prodotti (ordine_id, prodotto_id, quantita) VALUES (?, ?, ?)";
                    PreparedStatement psProdotti = connection.prepareStatement(prodottiSQL);

                    for (Map.Entry<Prodotto, Integer> entry : ordine.getProdotti().entrySet()) {
                        psProdotti.setInt(1, ordineId);
                        psProdotti.setInt(2, entry.getKey().getId());
                        psProdotti.setInt(3, entry.getValue());
                        psProdotti.addBatch();
                    }

                    psProdotti.executeBatch();
                    LOGGER.info("✅ Ordine salvato con successo nel database.");
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "❌ Errore nel salvataggio dell'ordine: " + e.getMessage(), e);
            }
        } else {
            ordiniOffline.add(ordine);
            LOGGER.info("🟢 Ordine salvato in modalità offline in memoria.");
        }
    }

    public List<Ordine> getOrdiniOffline() {
        return ordiniOffline;
    }
}
