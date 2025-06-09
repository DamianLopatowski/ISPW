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

            Map<Integer, Ordine> ordiniMappa = new HashMap<>();
            Set<Integer> tuttiProdottoId = new HashSet<>();

            while (rs.next()) {
                int ordineId = rs.getInt("id");
                Ordine ordine = new Ordine();
                ordine.setId(ordineId);
                ordine.setData(rs.getTimestamp("data").toLocalDateTime());
                ordine.setTotale(rs.getDouble("totale"));
                ordine.setCliente(new Cliente.Builder().username(username).build());
                ordiniMappa.put(ordineId, ordine);
            }

            if (ordiniMappa.isEmpty()) return ordini;

            // Recupera relazioni ordine-prodotti
            try (PreparedStatement psProdotti = conn.prepareStatement(
                    "SELECT ordine_id, prodotto_id, quantita FROM ordine_prodotti WHERE ordine_id IN (" +
                            String.join(",", Collections.nCopies(ordiniMappa.size(), "?")) + ")")) {

                int index = 1;
                for (Integer ordineId : ordiniMappa.keySet()) {
                    psProdotti.setInt(index++, ordineId);
                }

                ResultSet rsProdotti = psProdotti.executeQuery();
                Map<Integer, Map<Integer, Integer>> mappaOrdineProdotti = new HashMap<>();

                while (rsProdotti.next()) {
                    int ordineId = rsProdotti.getInt("ordine_id");
                    int prodottoId = rsProdotti.getInt("prodotto_id");
                    int quantita = rsProdotti.getInt("quantita");

                    tuttiProdottoId.add(prodottoId);
                    mappaOrdineProdotti
                            .computeIfAbsent(ordineId, k -> new HashMap<>())
                            .put(prodottoId, quantita);
                }

                if (tuttiProdottoId.isEmpty()) return new ArrayList<>(ordiniMappa.values());

                // Recupera tutti i prodotti in un'unica query
                try (PreparedStatement psDettagli = conn.prepareStatement(
                        "SELECT id, nome, quantita, scaffale, codiceAbarre, soglia, prezzoAcquisto, prezzoVendita, categoria, immagine " +
                                "FROM prodotti WHERE id IN (" +
                                String.join(",", Collections.nCopies(tuttiProdottoId.size(), "?")) + ")")) {

                    index = 1;
                    for (Integer prodottoId : tuttiProdottoId) {
                        psDettagli.setInt(index++, prodottoId);
                    }

                    ResultSet rsDettagli = psDettagli.executeQuery();
                    Map<Integer, Prodotto> mappaProdotti = new HashMap<>();

                    while (rsDettagli.next()) {
                        Prodotto prodotto = new Prodotto.Builder()
                                .id(rsDettagli.getInt("id"))
                                .nome(rsDettagli.getString("nome"))
                                .quantita(rsDettagli.getInt("quantita"))
                                .scaffale(rsDettagli.getString("scaffale"))
                                .codiceAbarre(rsDettagli.getString("codiceAbarre"))
                                .soglia(rsDettagli.getInt("soglia"))
                                .prezzoAcquisto(rsDettagli.getDouble("prezzoAcquisto"))
                                .prezzoVendita(rsDettagli.getDouble("prezzoVendita"))
                                .categoria(rsDettagli.getString("categoria"))
                                .immagine(rsDettagli.getBytes("immagine"))
                                .build();
                        mappaProdotti.put(prodotto.getId(), prodotto);
                    }

                    // Ricostruisci ordini completi
                    for (Map.Entry<Integer, Map<Integer, Integer>> entry : mappaOrdineProdotti.entrySet()) {
                        Ordine ordine = ordiniMappa.get(entry.getKey());
                        Map<Prodotto, Integer> prodottiOrdine = new HashMap<>();
                        for (Map.Entry<Integer, Integer> prodottoEntry : entry.getValue().entrySet()) {
                            Prodotto prodotto = mappaProdotti.get(prodottoEntry.getKey());
                            if (prodotto != null) {
                                prodottiOrdine.put(prodotto, prodottoEntry.getValue());
                            }
                        }
                        ordine.setProdotti(prodottiOrdine);
                    }

                    ordini.addAll(ordiniMappa.values());
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante getOrdiniPerCliente", e);
        }

        return ordini;
    }
}
