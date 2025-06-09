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

                            psProdotti.setInt(1, ordineId);  // fuori dal ciclo: loop-invariant

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
                LOGGER.log(Level.SEVERE, "Errore nel salvataggio dell''ordine: {0}", e.getMessage());
            }
        } else {
            if (ordine.getCliente() == null || ordine.getCliente().getUsername() == null) {
                LOGGER.warning("Ordine offline senza cliente associato. Ignorato.");
                return;
            }

            ordiniOffline.add(ordine);

            LOGGER.log(Level.INFO,
                    "Ordine salvato in modalità offline per cliente: {0}", ordine.getCliente().getUsername());
        }
    }

    @Override
    public List<Ordine> getOrdiniPerCliente(String username) {
        if (!isOnlineMode) {
            return getOrdiniOffline(username);
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            Map<Integer, Ordine> ordiniMappa = recuperaOrdiniBase(conn, username);
            if (ordiniMappa.isEmpty()) return new ArrayList<>();

            Map<Integer, Map<Integer, Integer>> prodottiPerOrdine = recuperaProdottiPerOrdine(conn, ordiniMappa.keySet());
            Map<Integer, Prodotto> dettagliProdotti = recuperaDettagliProdotti(conn, prodottiPerOrdine);

            return costruisciOrdiniComposti(ordiniMappa, prodottiPerOrdine, dettagliProdotti);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante getOrdiniPerCliente", e);
            return new ArrayList<>();
        }
    }

    private List<Ordine> getOrdiniOffline(String username) {
        List<Ordine> ordini = new ArrayList<>();
        for (Ordine o : ordiniOffline) {
            if (o.getCliente() != null && o.getCliente().getUsername().equalsIgnoreCase(username)) {
                ordini.add(o);
            }
        }
        LOGGER.log(Level.INFO, "Recuperati {0} ordini in offline per {1}", new Object[]{ordini.size(), username});
        return ordini;
    }

    private Map<Integer, Ordine> recuperaOrdiniBase(Connection conn, String username) throws SQLException {
        Map<Integer, Ordine> ordini = new HashMap<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id, data, totale FROM ordini WHERE cliente_username = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Ordine ordine = new Ordine();
                ordine.setId(rs.getInt("id"));
                ordine.setData(rs.getTimestamp("data").toLocalDateTime());
                ordine.setTotale(rs.getDouble("totale"));
                ordine.setCliente(new Cliente.Builder().username(username).build());
                ordini.put(ordine.getId(), ordine);
            }
        }
        return ordini;
    }

    private Map<Integer, Map<Integer, Integer>> recuperaProdottiPerOrdine(Connection conn, Set<Integer> ordineIds) throws SQLException {
        Map<Integer, Map<Integer, Integer>> prodottiPerOrdine = new HashMap<>();
        if (ordineIds.isEmpty()) return prodottiPerOrdine;

        String query = "SELECT ordine_id, prodotto_id, quantita FROM ordine_prodotti WHERE ordine_id IN (" +
                String.join(",", Collections.nCopies(ordineIds.size(), "?")) + ")";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            int index = 1;
            for (Integer id : ordineIds) {
                ps.setInt(index++, id);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int ordineId = rs.getInt("ordine_id");
                int prodottoId = rs.getInt("prodotto_id");
                int quantita = rs.getInt("quantita");

                prodottiPerOrdine.computeIfAbsent(ordineId, k -> new HashMap<>()).put(prodottoId, quantita);
            }
        }
        return prodottiPerOrdine;
    }

    private Map<Integer, Prodotto> recuperaDettagliProdotti(Connection conn, Map<Integer, Map<Integer, Integer>> prodottiPerOrdine) throws SQLException {
        Set<Integer> prodottoIds = new HashSet<>();
        for (Map<Integer, Integer> prodotti : prodottiPerOrdine.values()) {
            prodottoIds.addAll(prodotti.keySet());
        }
        if (prodottoIds.isEmpty()) return Collections.emptyMap();

        Map<Integer, Prodotto> prodotti = new HashMap<>();
        String query = "SELECT id, nome, quantita, scaffale, codiceAbarre, soglia, prezzoAcquisto, prezzoVendita, categoria, immagine " +
                "FROM prodotti WHERE id IN (" + String.join(",", Collections.nCopies(prodottoIds.size(), "?")) + ")";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            int index = 1;
            for (Integer id : prodottoIds) {
                ps.setInt(index++, id);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Prodotto prodotto = new Prodotto.Builder()
                        .id(rs.getInt("id"))
                        .nome(rs.getString("nome"))
                        .quantita(rs.getInt("quantita"))
                        .scaffale(rs.getString("scaffale"))
                        .codiceAbarre(rs.getString("codiceAbarre"))
                        .soglia(rs.getInt("soglia"))
                        .prezzoAcquisto(rs.getDouble("prezzoAcquisto"))
                        .prezzoVendita(rs.getDouble("prezzoVendita"))
                        .categoria(rs.getString("categoria"))
                        .immagine(rs.getBytes("immagine"))
                        .build();
                prodotti.put(prodotto.getId(), prodotto);
            }
        }
        return prodotti;
    }

    private List<Ordine> costruisciOrdiniComposti(Map<Integer, Ordine> ordiniMappa,
                                                  Map<Integer, Map<Integer, Integer>> prodottiPerOrdine,
                                                  Map<Integer, Prodotto> prodottiDettagliati) {
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : prodottiPerOrdine.entrySet()) {
            Ordine ordine = ordiniMappa.get(entry.getKey());
            Map<Prodotto, Integer> prodotti = new HashMap<>();
            for (Map.Entry<Integer, Integer> prodottoEntry : entry.getValue().entrySet()) {
                Prodotto prodotto = prodottiDettagliati.get(prodottoEntry.getKey());
                if (prodotto != null) {
                    prodotti.put(prodotto, prodottoEntry.getValue());
                }
            }
            ordine.setProdotti(prodotti);
        }
        return new ArrayList<>(ordiniMappa.values());
    }
}
