package org.example.dao;

import org.example.model.Cliente;
import org.example.model.Ordine;
import org.example.model.Prodotto;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdineDAOImpl implements OrdineDAO {
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
            LOGGER.info("Configurazione database caricata con successo!");
        } catch (IOException e) {
            LOGGER.severe("Errore nel caricamento del file di configurazione: " + e.getMessage());
        }
    }

    @Override
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

                            psProdotti.setInt(1, ordineId); // loop-invariant

                            for (Map.Entry<Prodotto, Integer> entry : ordine.getProdotti().entrySet()) {
                                psProdotti.setInt(2, entry.getKey().getId());
                                psProdotti.setInt(3, entry.getValue());
                                psProdotti.addBatch();
                            }

                            psProdotti.executeBatch();
                        }

                        LOGGER.info("Ordine salvato con successo nel database.");
                    }
                }

            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Errore nel salvataggio dell'ordine: {0}", e.getMessage());
            }
        } else {
            if (ordine.getCliente() == null || ordine.getCliente().getUsername() == null) {
                LOGGER.warning("Ordine offline senza cliente associato. Ignorato.");
                return;
            }

            ordiniOffline.add(ordine);

            LOGGER.log(Level.INFO,
                    "Ordine salvato in modalita offline per cliente: {0}", ordine.getCliente().getUsername());
        }
    }

    @Override
    public List<Ordine> getOrdiniPerCliente(String username) {
        List<Ordine> ordini = new ArrayList<>();

        if (!isOnlineMode) {
            for (Ordine o : ordiniOffline) {
                if (o.getCliente() != null && o.getCliente().getUsername().equalsIgnoreCase(username)) {
                    ordini.add(o);
                }
            }
            LOGGER.log(Level.INFO, "Recuperati {0} ordini in offline per {1}", new Object[]{ordini.size(), username});
            return ordini;
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT id, data, totale FROM ordini WHERE cliente_username = ?")) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            try (PreparedStatement psDettagli = conn.prepareStatement(
                    "SELECT id, nome, quantita, scaffale, codiceAbarre, soglia, prezzoAcquisto, prezzoVendita, categoria, immagine FROM prodotti WHERE id = ?")) {

                while (rs.next()) {
                    int ordineId = rs.getInt("id");

                    Ordine ordine = new Ordine();
                    ordine.setId(ordineId);
                    ordine.setData(rs.getTimestamp("data").toLocalDateTime());
                    ordine.setTotale(rs.getDouble("totale"));
                    ordine.setCliente(new Cliente.Builder().username(username).build());

                    Map<Prodotto, Integer> prodotti = new HashMap<>();
                    try (PreparedStatement psProdotti = conn.prepareStatement(
                            "SELECT prodotto_id, quantita FROM ordine_prodotti WHERE ordine_id = ?")) {
                        psProdotti.setInt(1, ordineId);
                        ResultSet rsProd = psProdotti.executeQuery();

                        while (rsProd.next()) {
                            int prodottoId = rsProd.getInt("prodotto_id");
                            int quantita = rsProd.getInt("quantita");

                            psDettagli.setInt(1, prodottoId);
                            ResultSet rsDett = psDettagli.executeQuery();

                            if (rsDett.next()) {
                                Prodotto prodotto = new Prodotto.Builder()
                                        .id(rsDett.getInt("id"))
                                        .nome(rsDett.getString("nome"))
                                        .quantita(rsDett.getInt("quantita"))
                                        .scaffale(rsDett.getString("scaffale"))
                                        .codiceAbarre(rsDett.getString("codiceAbarre"))
                                        .soglia(rsDett.getInt("soglia"))
                                        .prezzoAcquisto(rsDett.getDouble("prezzoAcquisto"))
                                        .prezzoVendita(rsDett.getDouble("prezzoVendita"))
                                        .categoria(rsDett.getString("categoria"))
                                        .immagine(rsDett.getBytes("immagine"))
                                        .build();
                                prodotti.put(prodotto, quantita);
                            }
                        }
                    }

                    ordine.setProdotti(prodotti);
                    ordini.add(ordine);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante getOrdiniPerCliente", e);
        }

        return ordini;
    }
}
